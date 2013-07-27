import com.jgoodies.forms.layout.CellConstraints;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import panel.*;

public class Tq3 implements WindowListener, ChangeListener, ListSelectionListener {
	JFrame frame;
	Main panelMain = new Main();
	Today panelToday = new Today();
	Tasks panelTasks = new Tasks();
	Db db = new Db();
	List<Category> categories;
	JTabbedPane ptab;
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	DateFormat dbdf = new SimpleDateFormat("yyyy/MM/dd");
	Category curCat;
	Task curTask;

	// table model for "Today's" tasks
	TaskTableModel todayTable;
	// table model for "one-time" tasks
	TaskTableModel onlyOnceTable;

	public Tq3() {
		try {
			db.connect();
			categories = db.getCategories();

			ptab = panelMain.tabbedPane;

			///////////////////////// Category tables
			for(Category cat : categories) {
				cat.tableModel = new TaskTableModel(db.getTasks(cat.id));
				JTable t = new JTable(cat.tableModel);
				cat.table = t;
				// widen the second column (name)
				t.getColumnModel().getColumn(1).setPreferredWidth(250);
				cat.tableModel.origRenderer = t.getDefaultRenderer(String.class);
				t.setDefaultRenderer(String.class, cat.tableModel);

				t.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				t.getSelectionModel().addListSelectionListener(this);
				ptab.add(cat.name, new JScrollPane(t));
			}

			///////////////////////// Only once table
			{
			onlyOnceTable = new TaskTableModel(db.getOnlyOnceTasks(), true);
			JTable onlyOnceJTable = new JTable(onlyOnceTable);
			// widen the second column (name)
			onlyOnceJTable.getColumnModel().getColumn(1).setPreferredWidth(250);
			onlyOnceTable.origRenderer = onlyOnceJTable.getDefaultRenderer(String.class);
			onlyOnceJTable.setDefaultRenderer(String.class, onlyOnceTable);

			ptab.addTab("One Time", new JScrollPane(onlyOnceJTable));
			onlyOnceJTable.setRowSelectionAllowed(false);
			onlyOnceJTable.setColumnSelectionAllowed(false);
			onlyOnceJTable.setCellSelectionEnabled(false);
			}

			///////////////////////// Today table
			{
			todayTable = new TaskTableModel(db.getTodaysTasks(new Date()), true);
			JTable todayJTable = new JTable(todayTable);
			// widen the second column (name)
			todayJTable.getColumnModel().getColumn(1).setPreferredWidth(250);
			todayTable.origRenderer = todayJTable.getDefaultRenderer(String.class);
			todayJTable.setDefaultRenderer(String.class, todayTable);

			ptab.addTab("Today", new JScrollPane(todayJTable));
			todayJTable.setRowSelectionAllowed(false);
			todayJTable.setColumnSelectionAllowed(false);
			todayJTable.setCellSelectionEnabled(false);
			}

		} catch(SQLException ex) {
			error(ex);
			return;
		}
		frame = new JFrame("Task Q III");
		frame.add(panelMain);
		frame.addWindowListener(this);
		panelToday.dateToday.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if (!e.getPropertyName().equals("date"))
					return;
				try {
					String notes = db.getTodaysNotes(dbdf.format(panelToday.dateToday.getDate()));
					if (notes == null) {
						panelToday.dailyNotes.setText("No notes for " + df.format(panelToday.dateToday.getDate()));
					} else {
						panelToday.dailyNotes.setText(notes);
					}
					panelToday.btnSaveNotes.setEnabled(false);
					panelToday.dailyNotes.setCaretPosition(panelToday.dailyNotes.getDocument().getLength());
				} catch(SQLException ex) {
					error(ex);
					return;
				}
			}
		});
		panelToday.dateToday.setDate(new Date());
		panelToday.dailyNotes.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				dailyNotesChanged(e);
			}
			public void removeUpdate(DocumentEvent e) {
				dailyNotesChanged(e);
			}
			public void changedUpdate(DocumentEvent e) {
				dailyNotesChanged(e);
			}
		});
		panelToday.btnSaveNotes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveNotes();
			}
		});
		panelTasks.btnDoneToday.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doneToday();
			}
		});
		panelTasks.btnCloseTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeTask();
			}
		});
		panelTasks.btnNewTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newTask();
			}
		});
		panelTasks.btnSaveNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveTaskNote();
			}
		});
		panelTasks.btnReorder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reorderTasks();
			}
		});
		ptab.addChangeListener(this);
		ptab.setSelectedIndex(ptab.getTabCount() - 1);
	}

	/**
	 * Implement ChangeListener to keep state consistent with tab being viewed
	 */
	public void stateChanged(ChangeEvent e) {
		curTask = null;
		// clear current selected item if applicable
		if (curCat != null) {
			curCat.table.getSelectionModel().removeSelectionInterval(0, curCat.tableModel.getRowCount() - 1);
		}
		panelMain.topPanel.removeAll();
		if (ptab.getSelectedIndex() == (ptab.getTabCount() - 1)) {
			switchToTodayPanel();
			panelMain.stats.setStats(null);
		} else if (ptab.getSelectedIndex() == (ptab.getTabCount() - 2)) {
			switchToOnlyOncePanel();
			panelMain.stats.setStats(null);
		} else {
			//panelMain.topPanel.add(new JLabel(ptab.getTitleAt(ptab.getSelectedIndex())), new CellConstraints(1, 1));
			switchToTaskPanel();
		}
		// dunno why this is needed... but the top panel fucks up if it's not here
		panelMain.repaint();
	}

	private void switchToTodayPanel() {
		try {
			Date today = panelToday.dateToday.getDate();
			List<Task> tasks = db.getTodaysTasks(today);
			// TODO: these two should be updated when checking the 'done' button
			// TODO: and changing of day
			panelToday.txtDone.setText(((Integer)db.getTodaysDoneCount(today)).toString());
			panelToday.txtStill.setText(((Integer)db.getTodaysTasks(today).size()).toString());
			todayTable.setNewData(tasks);
		} catch(SQLException ex) {
			error(ex);
			return;
		}
		panelMain.topPanel.add(panelToday, new CellConstraints(1, 1));
		curCat = null;
	}

	private void switchToOnlyOncePanel() {
		try {
			onlyOnceTable.setNewData(db.getOnlyOnceTasks());
		} catch(SQLException ex) {
			error(ex);
			return;
		}
		curCat = null;
		String name = ptab.getTitleAt(ptab.getSelectedIndex());
		// for some reason this has to be before the setting the text on the JLabel
		// else it doesn't do anything....
		panelMain.topPanel.add(panelTasks, new CellConstraints(1, 1));
		panelTasks.txtTitle.setText(name);
		clearTaskPanel();
		// only allow creating a task on it's respective category panel
		panelTasks.btnNewTask.setEnabled(false);
	}

	private void switchToTaskPanel() {
		curCat = categories.get(ptab.getSelectedIndex());
		try {
			curCat.tableModel.setNewData(db.getTasks(curCat.id));
		} catch(SQLException ex) {
			error(ex);
		}
		String name = ptab.getTitleAt(ptab.getSelectedIndex());
		// for some reason this has to be before the setting the text on the JLabel
		// else it doesn't do anything....
		panelMain.topPanel.add(panelTasks, new CellConstraints(1, 1));
		panelTasks.txtTitle.setText(name);
		clearTaskPanel();
		panelTasks.btnNewTask.setEnabled(true);
	}

	private void clearTaskPanel() {
		panelTasks.btnDoneToday.setEnabled(false);
		panelTasks.btnCloseTask.setEnabled(false);
		panelTasks.txtName.setText("");
		panelTasks.txtDaysBetween.setText("");
		panelTasks.txtNote.setText("");
		panelMain.stats.setStats(null);
		panelMain.stats.repaint();
	}

	private void dailyNotesChanged(DocumentEvent e) {
		panelToday.btnSaveNotes.setEnabled(true);
		//panelToday.btnSaveNotes.setForeground(java.awt.Color.RED);
	}

	private void saveNotes() {
		try {
			db.saveNotes(dbdf.format(panelToday.dateToday.getDate()), panelToday.dailyNotes.getText());
			panelToday.btnSaveNotes.setEnabled(false);
		} catch(SQLException ex) {
			error(ex);
		}
	}

	private void newTask() {
		final Task t = new Task();
		t.catId = curCat.id;
		final JDialog d = new JDialog(frame);
		final NewTask pan = new NewTask();
		final List retval = new ArrayList();
		pan.dateStart.setDate(new Date());
		pan.btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// input validation
				t.name = pan.txtName.getText();
				t.nextScheduled = pan.dateStart.getDate();
				t.isOnlyOnce = pan.cbOnlyOnce.isSelected();
				if (!t.isOnlyOnce) {
					try {
						t.daysBetween = Integer.parseInt(pan.txtDaysBetween.getText());
					} catch(NumberFormatException ex) {
						JOptionPane.showMessageDialog(d, "Invalid value for days between", "Invalid Data", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				if (t.name.equals("")) {
					JOptionPane.showMessageDialog(d, "Invalid value for name", "Invalid Data", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (t.nextScheduled == null) {
					JOptionPane.showMessageDialog(d, "Invalid value for start date", "Invalid Data", JOptionPane.ERROR_MESSAGE);
					return;
				}

				retval.add(1);
				d.hide();
			}
		});
		pan.btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				d.hide();
			}
		});
		d.getContentPane().add(pan);
		d.setModal(true);
		d.setTitle("New Task");
		d.pack();
		d.setLocationRelativeTo(null);
		d.show();
		if (retval.size() == 0) {
			return;
		}
		try {
			db.saveTask(t);
			curCat.tableModel.setNewData(db.getTasks(curCat.id));
		} catch(SQLException ex) {
			error(ex);
			return;
		}
	}

	private void doneToday() {
		try {
			curTask.lastDone = panelToday.dateToday.getDate();
			db.addEntry(curTask);
			curCat.tableModel.setNewData(db.getTasks(curCat.id));
		} catch(SQLException ex) {
			error(ex);
			return;
		}
	}

	private void closeTask() {
		try {
			db.closeTask(curTask.id);
			curCat.tableModel.setNewData(db.getTasks(curCat.id));
		} catch(SQLException ex) {
			error(ex);
			return;
		}
	}

	private void saveTaskNote() {
		if (curTask == null)
			return;
		curTask.note = panelTasks.txtNote.getText();
		try {
			db.saveTaskNote(curTask);
			panelToday.btnSaveNotes.setEnabled(false);
		} catch(SQLException ex) {
			error(ex);
		}
	}

	private void reorderTasks() {
		curCat.tableModel.lexOrder = !curCat.tableModel.lexOrder;
		try {
			curCat.tableModel.setNewData(db.getTasks(curCat.id));
		} catch(SQLException ex) {
			error(ex);
			return;
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		int row = ((ListSelectionModel)e.getSource()).getMinSelectionIndex();
		if (row < 0) {
			curTask = null;
			clearTaskPanel();
			return;
		}
		curTask = curCat.tableModel.tasks.get(row);
		panelTasks.txtName.setText(curTask.name);
		panelTasks.txtDaysBetween.setText(((Integer)curTask.daysBetween).toString());
		if (curTask.note == null) {
			panelTasks.txtNote.setText("no notes, mate");
		} else {
			panelTasks.txtNote.setText(curTask.note);
		}
		if (curTask.lastDone == null ||
				!df.format(curTask.lastDone).equals(df.format(panelToday.dateToday.getDate()))) {
			panelTasks.btnDoneToday.setEnabled(true);
		} else {
			panelTasks.btnDoneToday.setEnabled(false);
		}
		panelTasks.btnCloseTask.setEnabled(true);
		try {
			panelMain.stats.setStats(db.getStats(curTask.id));
			panelMain.stats.setAggStats(db.getAggStats(curTask.id, curTask.daysBetween));
		} catch(SQLException ex) {
			error(ex);
		}
		panelMain.stats.repaint();
	}

	/////////////////////////////////////////////
	// Implement WindowListener to close the   //
	// db connection when the window is closed //
	/////////////////////////////////////////////
	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		try {
			db.disconnect();
		} catch(SQLException ex) {
			error(ex);
		}
		frame.dispose();
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	//////////////////////////////////
	private void error(Exception ex) {
		ex.printStackTrace();
		System.err.println("----------------- END ORIG EXC");
		JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		//throw new RuntimeException(ex);
	}

	public static void main(String args[]) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		final Tq3 app = new Tq3();
		SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					app.frame.pack();
					app.frame.setSize(600, 600);
					app.frame.setLocationRelativeTo(null);
					app.frame.setVisible(true);
				}
			});
	}

	/* bleh */
	static final int COL_DONE = 0;
	static final int COL_NAME = 1;
	static final int COL_TIMES_MISSED = 2;
	static final int COL_LAST_DONE = 3;
	static final int COL_DO_NEXT = 4;
	static final int COL_NEXT_DATE = 5;
	static final int COL_CAT = 6;
	static String colNames[] = {"Done", "Name", "Times Missed", "Last Done", "Days til Next", "Next Date", "Category"};
	static Class colClasses[] = {Boolean.class, String.class, Integer.class, String.class, Integer.class, String.class, String.class};


	class TaskTableModel implements TableModel, TableCellRenderer {
		List<Task> tasks;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		List<TableModelListener> listeners = new ArrayList<TableModelListener>();
		TableCellRenderer origRenderer;
		boolean extraCols = false;
		boolean lexOrder = false;

		public TaskTableModel(List<Task> tasklist) {
			setNewData(tasklist);
		}

		public TaskTableModel(List<Task> tasklist, boolean extraCols) {
			setNewData(tasklist);
			this.extraCols = extraCols;
		}

		public void addTableModelListener(TableModelListener l) {
			listeners.add(l);
		}

		public Class getColumnClass(int columnIndex) {
			return colClasses[columnIndex];
		}

		public int getColumnCount() {
			if (extraCols)
				return colNames.length;
			else
				return colNames.length - 1;
		}

		public String getColumnName(int columnIndex) {
			return colNames[columnIndex];
		}

		public int getRowCount() {
			return tasks.size();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			Task t = tasks.get(rowIndex);
			switch (columnIndex) {
			case COL_DONE:
				return (t.lastDone != null) && (df.format(t.lastDone).equals(df.format(panelToday.dateToday.getDate())));
			case COL_NAME:
				return t.name;
			case COL_TIMES_MISSED:
				return t.timesMissed;
			case COL_LAST_DONE:
				if (t.lastDone != null)
					return df.format(t.lastDone);
				else if (t.isOnlyOnce)
					return "<one-time>";
				else
					return "<never>";
			case COL_DO_NEXT:
				return t.daysTilNextSchedule;
			case COL_NEXT_DATE:
				return df.format(t.nextScheduled);
			case COL_CAT:
				return t.catName;
			default:
				return "<error>";
			}
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex == COL_DONE)
				return true;
			else
				return false;
		}

		public void removeTableModelListener(TableModelListener l) {
			listeners.remove(l);
		}

		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (!aValue.equals(true) || columnIndex != COL_DONE)
				return;
			// ugh - db needed only for this
			Task t = tasks.get(rowIndex);
			try {
				Date today = panelToday.dateToday.getDate();
				t.lastDone = today;
				db.addEntry(t);
				if (this == todayTable)
					setNewData(db.getTodaysTasks(today));
				else if (this == onlyOnceTable)
					setNewData(db.getOnlyOnceTasks());
			} catch(SQLException ex) {
				// hack-copy of error()
				ex.printStackTrace();
				System.err.println("----------------- END ORIG EXC");
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		public void setNewData(List<Task> tasklist) {
			tasks = tasklist;
			if (lexOrder)
				Collections.sort(tasks);
			TableModelEvent ev = new TableModelEvent(this);
			for (TableModelListener l : listeners) {
				l.tableChanged(ev);
			}
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			Component c = origRenderer.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			Task t = tasks.get(row);
			if (t.timesMissed > 0) {
				c.setForeground(Color.RED);
			} else if (df.format(t.nextScheduled).equals(df.format(panelToday.dateToday.getDate()))) {
				if (isSelected)
					c.setForeground(Color.GREEN);
				else
					c.setForeground(new Color(48, 163, 53));
			} else {
				// Technically should get a default frmo L&F or smth
				if (isSelected)
					c.setForeground(Color.WHITE);
				else
					c.setForeground(Color.BLACK);
			}
			return c;
		}

	}

}
