import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Calendar {
	private static Calendar thisCalendar;
	private static java.util.Calendar calendar;
		
	private Calendar() {
		calendar = java.util.Calendar.getInstance();
	}
	
	public static Calendar INSTANCE() {
		if (thisCalendar == null) {
			thisCalendar = new Calendar();
		}
		return thisCalendar;
	}
	
	public void incrementDate(int days) {
		calendar.add(java.util.Calendar.DATE, days);		
	}
	
	public synchronized void SetDate(Date date) {
		try {
			calendar.setTime(date);
	        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);  
	        calendar.set(java.util.Calendar.MINUTE, 0);  
	        calendar.set(java.util.Calendar.SECOND, 0);  
	        calendar.set(java.util.Calendar.MILLISECOND, 0);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	public synchronized Date Date() {
		try {
	        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);  
	        calendar.set(java.util.Calendar.MINUTE, 0);  
	        calendar.set(java.util.Calendar.SECOND, 0);  
	        calendar.set(java.util.Calendar.MILLISECOND, 0);
			return calendar.getTime();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	public synchronized Date dueDate(int loanPeriod) {
		Date currentDate = Date();
		calendar.add(java.util.Calendar.DATE, loanPeriod);
		Date dueDate = calendar.getTime();
		calendar.setTime(currentDate);
		return dueDate;

	}
	
	public synchronized long GetDaysDifference(Date targetDate) {
		
		long differenceInMilli = Date().getTime() - targetDate.getTime();
	    long differenceInDays = TimeUnit.DAYS.convert(differenceInMilli, TimeUnit.MILLISECONDS);
	    return differenceInDays;
	}

}
