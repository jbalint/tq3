/*
 * Created by JFormDesigner on Thu Nov 26 15:11:57 CST 2009
 */

package panel;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Jess Balint
 */
public class Tasks extends JPanel {
	public Tasks() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		txtTitle = new JLabel();
		label1 = new JLabel();
		txtName = new JTextField();
		btnDoneToday = new JButton();
		label2 = new JLabel();
		txtDaysBetween = new JTextField();
		btnCloseTask = new JButton();
		label3 = new JLabel();
		checkBox1 = new JCheckBox();
		btnNewTask = new JButton();
		label4 = new JLabel();
		scrollPane1 = new JScrollPane();
		txtNote = new JTextArea();
		panel1 = new JPanel();
		btnSaveNote = new JButton();
		btnReorder = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.TABBED_DIALOG_BORDER);
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC
			},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			}));

		//---- txtTitle ----
		txtTitle.setText("fooey");
		txtTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
		txtTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(txtTitle, cc.xywh(1, 1, 5, 1));

		//---- label1 ----
		label1.setText("Name");
		add(label1, cc.xy(1, 3));

		//---- txtName ----
		txtName.setEditable(false);
		add(txtName, cc.xy(3, 3));

		//---- btnDoneToday ----
		btnDoneToday.setText("Done Today");
		add(btnDoneToday, cc.xy(5, 3));

		//---- label2 ----
		label2.setText("Days Between");
		add(label2, cc.xy(1, 5));

		//---- txtDaysBetween ----
		txtDaysBetween.setEditable(false);
		add(txtDaysBetween, cc.xy(3, 5));

		//---- btnCloseTask ----
		btnCloseTask.setText("Close Task");
		add(btnCloseTask, cc.xy(5, 5));

		//---- label3 ----
		label3.setText("text");
		add(label3, cc.xy(1, 7));
		add(checkBox1, cc.xy(3, 7));

		//---- btnNewTask ----
		btnNewTask.setText("New Task");
		add(btnNewTask, cc.xy(5, 7));

		//---- label4 ----
		label4.setText("text");
		add(label4, cc.xy(1, 9));

		//======== scrollPane1 ========
		{
			
			//---- txtNote ----
			txtNote.setText("type a note here, mate");
			txtNote.setFont(new Font("Tahoma", Font.PLAIN, 11));
			scrollPane1.setViewportView(txtNote);
		}
		add(scrollPane1, cc.xy(3, 9));

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
				ColumnSpec.decodeSpecs("default:grow"),
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));
			
			//---- btnSaveNote ----
			btnSaveNote.setText("Save Note");
			panel1.add(btnSaveNote, cc.xy(1, 1));
			
			//---- btnReorder ----
			btnReorder.setText("reOrder");
			panel1.add(btnReorder, cc.xy(1, 3));
		}
		add(panel1, cc.xy(5, 9));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	public JLabel txtTitle;
	private JLabel label1;
	public JTextField txtName;
	public JButton btnDoneToday;
	private JLabel label2;
	public JTextField txtDaysBetween;
	public JButton btnCloseTask;
	private JLabel label3;
	public JCheckBox checkBox1;
	public JButton btnNewTask;
	private JLabel label4;
	private JScrollPane scrollPane1;
	public JTextArea txtNote;
	private JPanel panel1;
	public JButton btnSaveNote;
	public JButton btnReorder;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
