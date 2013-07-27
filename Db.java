import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Db {
	Connection con;
	DateFormat dbdf = new SimpleDateFormat("yyyy/MM/dd");

	public Db() {
	}

	public void connect() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost/tq3", "tq3", "tq3_BOBBY");
		// TODO: lazy putting this here... should be a better time to do this
		// but it kind of has to be async.... :(
		updateTimesMissed();
	}

	public void disconnect() throws SQLException {
		con.close();
	}

	private Task getTaskFromRow(ResultSet rs) throws SQLException {
		Task t = new Task();
		t.id = rs.getInt(1);
		t.catId = rs.getInt(2);
		t.name = rs.getString(3);
		t.open = rs.getBoolean(4);
		t.daysBetween = rs.getInt(5);
		t.setNextScheduled(rs.getDate(6));
		t.timesMissed = rs.getInt(7);
		t.note = rs.getString(8);
		t.isOnlyOnce = rs.getBoolean(9);
		t.lastDone = rs.getDate(10);
		return t;
	}

	// batch thing to update the number of times an item has been missed
	public void updateTimesMissed() throws SQLException {
		con.createStatement().executeUpdate("update task set times_missed = ceil(datediff(now(), next_sched) / if(days_between = 0, 1, days_between)) where datediff(now(), next_sched) > 0");
		//con.createStatement().executeUpdate("update task inner join (select task_entry.task_id, max(entry_date) as entry_date from task_entry group by 1) as task_en2 on task.id = task_en2.task_id set task.times_missed = floor(datediff(next_sched, entry_date) / days_between) where datediff(now(), next_sched) > 0");
//select task.id, floor(datediff(next_sched, coalesce(entry_date, next_sched)) / days_between) from task left join (select task_entry.task_id, max(entry_date) as entry_date from task_entry group by 1) as task_en2 on task.id = task_en2.task_id
//select task.id, datediff(next_sched, coalesce(entry_date, next_sched)) from task left join (select task_entry.task_id, max(entry_date) as entry_date from task_entry group by 1) as task_en2 on task.id = task_en2.task_id where entry_date is not null;
	}

	public List<Category> getCategories() throws SQLException {
		ResultSet rs = con.createStatement().executeQuery("select id,name from category where active = 1 order by order_val");
		List<Category> cats = new ArrayList<Category>();
		while(rs.next()) {
			cats.add(new Category(rs.getInt(1), rs.getString(2)));
		}
		rs.close();
		return cats;
	}

	public List<Task> getTasks(int categoryId) throws SQLException {
		List<Task> tasks = new ArrayList<Task>();
		PreparedStatement ps = con.prepareStatement(
				"select task.id, cat_id, name, open, days_between, next_sched, " +
				     "times_missed, note, is_only_once, max(entry_date) as last_date " +
				"from task left join task_entry on task.id = task_entry.task_id " +
				"where cat_id = ? and open = 1 " +
				"group by 1,2,3,4,5,6,7,8,9 " +
				"order by times_missed desc, next_sched asc, name asc");
		ps.setInt(1, categoryId);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			tasks.add(getTaskFromRow(rs));
		}
		rs.close();
		ps.close();
		return tasks;
	}

	public String getTodaysNotes(String date) throws SQLException {
		String notes = null;
		PreparedStatement ps = con.prepareStatement("select note from daily_notes where notedate = ?");
		ps.setString(1, date);
		ResultSet rs = ps.executeQuery();
		if(rs.next())
			notes = rs.getString(1);
		rs.close();
		ps.close();
		return notes;
	}

	public int getTodaysDoneCount(Date today) throws SQLException {
		PreparedStatement ps = con.prepareStatement("select count(id) from task_entry where datediff(?, entry_date) = 0");
		ps.setString(1, dbdf.format(today));
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		ps.close();
		return count;
	}

	public List<Task> getTodaysTasks(Date today) throws SQLException {
		List<Task> tasks = new ArrayList<Task>();
		PreparedStatement ps = con.prepareStatement("select task.id, cat_id, task.name, open, days_between, next_sched, times_missed, note, is_only_once, max(entry_date) as last_date, category.name from task inner join category on task.cat_id = category.id left join task_entry on task.id = task_entry.task_id where open = 1 and datediff(next_sched, ?) <= 0 group by 1, 2, 3, 4, 5, 6, 7, 8, 9, 11 order by times_missed desc, next_sched asc, task.name asc");
		ps.setString(1, dbdf.format(today));
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Task t = getTaskFromRow(rs);
			t.catName = rs.getString(11);
			tasks.add(t);
		}
		rs.close();
		ps.close();
		return tasks;
	}

	public List<Task> getOnlyOnceTasks() throws SQLException {
		List<Task> tasks = new ArrayList<Task>();
		PreparedStatement ps = con.prepareStatement("select task.id, cat_id, task.name, open, days_between, next_sched, times_missed, note, is_only_once, max(entry_date) as last_date, category.name from task inner join category on task.cat_id = category.id left join task_entry on task.id = task_entry.task_id where open = 1 and is_only_once = 1 group by 1, 2, 3, 4, 5, 6, 7, 8, 9, 11 order by times_missed desc, next_sched asc, task.name asc");
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Task t = getTaskFromRow(rs);
			t.catName = rs.getString(11);
			tasks.add(t);
		}
		rs.close();
		ps.close();
		return tasks;
	}

	public void saveNotes(String date, String notes) throws SQLException {
		PreparedStatement ps = con.prepareStatement("insert into daily_notes (notedate, note) values (?, ?) on duplicate key update note = ?");
		ps.setString(1, date);
		ps.setString(2, notes);
		ps.setString(3, notes);
		ps.executeUpdate();
		ps.close();
	}

	public void saveTask(Task t) throws SQLException {
		PreparedStatement ps = con.prepareStatement("insert into task (cat_id, name, days_between, next_sched, is_only_once) values (?, ?, ?, ?, ?)");
		ps.setInt(1, t.catId);
		ps.setString(2, t.name);
		ps.setInt(3, t.daysBetween);
		ps.setString(4, dbdf.format(t.nextScheduled));
		ps.setBoolean(5, t.isOnlyOnce);
		ps.executeUpdate();
		ps.close();

		ps = con.prepareStatement("select last_insert_id()");
		ResultSet rs = ps.executeQuery();
		rs.next();
		t.id = rs.getInt(1);
		rs.close();
	}

	public void saveTaskNote(Task t) throws SQLException {
		// TODO : meh - save the whole task and edit it?...
		PreparedStatement ps = con.prepareStatement("update task set note = ? where id = ?");
		ps.setString(1, t.note);
		ps.setInt(2, t.id);
		ps.executeUpdate();
		ps.close();
	}

	public void closeTask(int taskId) throws SQLException {
		PreparedStatement ps = con.prepareStatement("update task set open = 0 where id = ?");
		ps.setInt(1, taskId);
		ps.executeUpdate();
		ps.close();
	}

	public void addEntry(Task task) throws SQLException {
		PreparedStatement ps = con.prepareStatement("insert into task_entry (task_id, entry_date) values (?, ?)");
		ps.setInt(1, task.id);
		ps.setString(2, dbdf.format(task.lastDone));
		ps.executeUpdate();
		ps.close();
		ps = con.prepareStatement("update task set times_missed = 0, next_sched = date_add(?, interval days_between day) where id = ?");
		ps.setString(1, dbdf.format(task.lastDone));
		ps.setInt(2, task.id);
		ps.executeUpdate();
		ps.close();
		// close if one-timer
		if (task.isOnlyOnce) {
			task.open = false;
			closeTask(task.id);
		}
	}

	public List getStats(int taskId) throws SQLException {
		PreparedStatement ps = con.prepareStatement("select datediff(now(), entry_date) as days_ago from task_entry where " +
				" entry_date > date_sub(now(), interval 30 day) and task_id = ?");
		ps.setInt(1, taskId);
		ResultSet rs = ps.executeQuery();
		List stats = new ArrayList();
		while(rs.next()) {
			stats.add(rs.getInt(1));
		}
		rs.close();
		ps.close();
		return stats;
	}

	public List getAggStats(int taskId, int daysBetween) throws SQLException {
		PreparedStatement ps = 
		con.prepareStatement("select (sum(days_ago < 7) / 7.0) as d7, " +
								    "(sum(days_ago < 10) / 10.0) as d10," +
       								"(sum(days_ago < 30) / 30.0) as d30, " +
       								"(sum(days_ago < 60) / 60.0) as d60 from ( " +
"select datediff(now(), entry_date) as days_ago from task_entry where entry_date > date_sub(now(), interval 60 day) and task_id = ?" +
") as xyz1");
		ps.setInt(1, taskId);
		ResultSet rs = ps.executeQuery();
		List stats = new ArrayList();
		rs.next();
		stats.add(rs.getDouble(1) * daysBetween);
		stats.add(rs.getDouble(2) * daysBetween);
		stats.add(rs.getDouble(3) * daysBetween);
		stats.add(rs.getDouble(4) * daysBetween);
		rs.close();
		ps.close();
		return stats;
	}
}
