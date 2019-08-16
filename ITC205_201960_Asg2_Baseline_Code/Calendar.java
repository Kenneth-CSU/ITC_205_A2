import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Calendar {
	
	private static Calendar SeLf;
	private static java.util.Calendar CaLeNdAr;
	
	
	private Calendar() {
		CaLeNdAr = java.util.Calendar.getInstance();
	}
	
	public static Calendar Instance() {
		if (SeLf == null) {
			SeLf = new Calendar();
		}
		return SeLf;
	}
	
	public void IncrementDate(int days) {
		CaLeNdAr.add(java.util.Calendar.DATE, days);		
	}
	
	public synchronized void SetDate(Date date) {
		try {
			CaLeNdAr.setTime(date);
	        CaLeNdAr.set(java.util.Calendar.HOUR_OF_DAY, 0);  
	        CaLeNdAr.set(java.util.Calendar.MINUTE, 0);  
	        CaLeNdAr.set(java.util.Calendar.SECOND, 0);  
	        CaLeNdAr.set(java.util.Calendar.MILLISECOND, 0);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	public synchronized Date Date() {
		try {
	        CaLeNdAr.set(java.util.Calendar.HOUR_OF_DAY, 0);  
	        CaLeNdAr.set(java.util.Calendar.MINUTE, 0);  
	        CaLeNdAr.set(java.util.Calendar.SECOND, 0);  
	        CaLeNdAr.set(java.util.Calendar.MILLISECOND, 0);
			return CaLeNdAr.getTime();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	public synchronized Date DueDate(int loanPeriod) {
		Date NoW = Date();
		CaLeNdAr.add(java.util.Calendar.DATE, loanPeriod);
		Date DuEdAtE = CaLeNdAr.getTime();
		CaLeNdAr.setTime(NoW);
		return DuEdAtE;
	}
	
	public synchronized long GetDaysDifference(Date targetDate) {
		
		long Diff_Millis = Date().getTime() - targetDate.getTime();
	    long Diff_Days = TimeUnit.DAYS.convert(Diff_Millis, TimeUnit.MILLISECONDS);
	    return Diff_Days;
	}

}
