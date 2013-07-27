/*
 * Created by JFormDesigner on Thu Nov 26 18:28:58 CST 2009
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
public class NewTask extends JPanel {
	public NewTask() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		label1 = new JLabel();
		txtName = new JTextField();
		label2 = new JLabel();
		txtDaysBetween = new JTextField();
		label3 = new JLabel();
		dateStart = new JDateChooser();
		label4 = new JLabel();
		cbOnlyOnce = new JCheckBox();
		panel1 = new JPanel();
		btnOk = new JButton();
		btnCancel = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.TABBED_DIALOG_BORDER);
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec("max(pref;96dlu)")
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
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- label1 ----
		label1.setText("Name");
		add(label1, cc.xy(1, 1));
		add(txtName, cc.xy(3, 1));

		//---- label2 ----
		label2.setText("Days Between Task");
		add(label2, cc.xy(1, 3));
		add(txtDaysBetween, cc.xy(3, 3));

		//---- label3 ----
		label3.setText("Start Date");
		add(label3, cc.xy(1, 5));

		//---- dateStart ----
		dateStart.setDateFormatString("MM/dd/yyyy");
		add(dateStart, cc.xy(3, 5));

		//---- label4 ----
		label4.setText("One Time?");
		add(label4, cc.xy(1, 7));
		add(cbOnlyOnce, cc.xy(3, 7));

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC
				},
				RowSpec.decodeSpecs("default")));
			
			//---- btnOk ----
			btnOk.setText("OK");
			btnOk.setMaximumSize(new Dimension(80, 23));
			btnOk.setMinimumSize(new Dimension(80, 23));
			btnOk.setPreferredSize(new Dimension(80, 23));
			panel1.add(btnOk, cc.xy(1, 1));
			
			//---- btnCancel ----
			btnCancel.setText("Cancel");
			btnCancel.setMaximumSize(new Dimension(80, 23));
			btnCancel.setMinimumSize(new Dimension(80, 23));
			btnCancel.setPreferredSize(new Dimension(80, 23));
			panel1.add(btnCancel, cc.xy(3, 1));
		}
		add(panel1, cc.xywh(1, 9, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel label1;
	public JTextField txtName;
	private JLabel label2;
	public JTextField txtDaysBetween;
	private JLabel label3;
	public JDateChooser dateStart;
	private JLabel label4;
	public JCheckBox cbOnlyOnce;
	private JPanel panel1;
	public JButton btnOk;
	public JButton btnCancel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
