
import java.util.Date;

public class Task implements Comparable {
	public int id = -1;
	public int catId = -1;
	public String name = "<none>";
	public boolean open = false;
	public int daysBetween = 0;
	public Date nextScheduled = new Date();
	public int timesMissed = 0;
	public String note;
	public boolean isOnlyOnce = false;
	public transient int daysTilNextSchedule = 0;
	public transient Date lastDone = new Date();
	public transient String catName;

	public Task() {
	}

	public Task(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public void setNextScheduled(Date d) {
		nextScheduled = d;
		long msTilNext = d.getTime() - new Date().getTime();
		// seconds per day 86400000 - > just use ceil here like in sql
		if (msTilNext < 0 && msTilNext > -86400000L)
			daysTilNextSchedule = 0;
		else if (msTilNext < 0)
			daysTilNextSchedule = (int)(msTilNext / 86400000L);
		else
			daysTilNextSchedule = 1 + (int)(msTilNext / 86400000L);
	}

	public int compareTo(Object o) {
		if(!Task.class.isAssignableFrom(o.getClass()))
			return -1;
		return name.compareTo(((Task)o).name);
	}
}
