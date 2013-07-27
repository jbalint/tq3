/*
 * Created by JFormDesigner on Thu Nov 26 15:36:07 CST 2009
 */

package panel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Jess Balint
 */
public class Main extends JPanel {
	public Main() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		topPanel = new JPanel();
		label1 = new JLabel();
		stats = new Stats();
		tabbedPane = new JTabbedPane();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.TABBED_DIALOG_BORDER);
		setLayout(new FormLayout(
			ColumnSpec.decodeSpecs("default:grow"),
			new RowSpec[] {
				FormFactory.PREF_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			}));

		//======== topPanel ========
		{
			topPanel.setLayout(new FormLayout(
				"default:grow",
				"fill:max(pref;250px):grow"));
			
			//---- label1 ----
			label1.setText(";klk;l;kl");
			label1.setPreferredSize(new Dimension(33, 150));
			topPanel.add(label1, cc.xy(1, 1));
		}
		add(topPanel, cc.xy(1, 1));

		//---- stats ----
		stats.setBorder(null);
		add(stats, cc.xy(1, 3));
		add(tabbedPane, cc.xy(1, 5));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	public JPanel topPanel;
	private JLabel label1;
	public Stats stats;
	public JTabbedPane tabbedPane;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
