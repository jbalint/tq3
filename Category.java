import javax.swing.JTable;

public class Category implements Comparable {
	public int id;
	public String name;
	// yeah, ugly.. oh well
	public Tq3.TaskTableModel tableModel;
	public JTable table;

	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int compareTo(Object o) {
		if(!Category.class.isAssignableFrom(o.getClass()))
			return -1;
		return name.compareTo(((Category)o).name);
	}
}
