/*
 * Created by JFormDesigner on Thu Nov 26 15:35:51 CST 2009
 */

package panel;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.toedter.calendar.*;

/**
 * @author Jess Balint
 */
public class Today extends JPanel {
	public Today() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		label3 = new JLabel();
		label1 = new JLabel();
		txtDone = new JLabel();
		label2 = new JLabel();
		txtStill = new JLabel();
		scrollPane1 = new JScrollPane();
		dailyNotes = new JTextArea();
		dateToday = new JDateChooser();
		btnSaveNotes = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.TABBED_DIALOG_BORDER);
		setLayout(new FormLayout(
			new ColumnSpec[] {
				new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
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
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- label3 ----
		label3.setText("Today");
		label3.setFont(new Font("SansSerif", Font.BOLD, 20));
		label3.setHorizontalAlignment(SwingConstants.CENTER);
		add(label3, cc.xywh(1, 1, 3, 1));

		//---- label1 ----
		label1.setText("Tasks completed today:");
		add(label1, cc.xy(1, 3));

		//---- txtDone ----
		txtDone.setText("9999");
		add(txtDone, cc.xy(3, 3));

		//---- label2 ----
		label2.setText("Tasks remaining today:");
		add(label2, cc.xy(1, 5));

		//---- txtStill ----
		txtStill.setText("9999");
		add(txtStill, cc.xy(3, 5));

		//======== scrollPane1 ========
		{
			
			//---- dailyNotes ----
			dailyNotes.setFont(new Font("Tahoma", Font.PLAIN, 11));
			scrollPane1.setViewportView(dailyNotes);
		}
		add(scrollPane1, cc.xywh(1, 7, 3, 1));

		//---- dateToday ----
		dateToday.setDateFormatString("yyyy/MM/dd");
		dateToday.setPreferredSize(new Dimension(120, 20));
		add(dateToday, cc.xy(1, 9));

		//---- btnSaveNotes ----
		btnSaveNotes.setText("Save Notes");
		btnSaveNotes.setEnabled(false);
		add(btnSaveNotes, cc.xy(3, 9));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel label3;
	private JLabel label1;
	public JLabel txtDone;
	private JLabel label2;
	public JLabel txtStill;
	private JScrollPane scrollPane1;
	public JTextArea dailyNotes;
	public JDateChooser dateToday;
	public JButton btnSaveNotes;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
