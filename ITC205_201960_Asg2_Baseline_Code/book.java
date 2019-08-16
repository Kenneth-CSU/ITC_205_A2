import java.io.Serializable;


@SuppressWarnings("serial")
public class book implements Serializable {
	
	private String title;
	private String author;
	private String callNumber;
	private int idNumber;
	
	private enum STATE { AVAILABLE, ON_LOAN, DAMAGED, RESERVED };
	private STATE bookState;
	
	
	public book(String author, String title, String callNumber, int idNumber) {
		this.author = author;
		this.title = title;
		this.callNumber = callNumber;
		this.idNumber = idNumber;
		this.bookState = STATE.AVAILABLE;
	}
	
	public String toString() {
		StringBuilder newString = new StringBuilder();
		newString.append("Book: ").append(idNumber).append("\n")
		  .append("  Title:  ").append(title).append("\n")
		  .append("  Author: ").append(author).append("\n")
		  .append("  CallNo: ").append(callNumber).append("\n")
		  .append("  State:  ").append(bookState);
		
		return newString.toString();
	}

	public Integer ID() {
		return idNumber;
	}

	public String TITLE() {
		return title;
	}


	
	public boolean AVAILABLE() {
		return bookState == STATE.AVAILABLE;
	}

	
	public boolean On_loan() {
		return bookState == STATE.ON_LOAN;
	}

	
	public boolean IS_Damaged() {
		return bookState == STATE.DAMAGED;
	}

	
	public void Borrow() {
		if (bookState.equals(STATE.AVAILABLE)) {
			bookState = STATE.ON_LOAN;
		}
		else {
			throw new RuntimeException(String.format("Book: cannot borrow while book is in state: %s", bookState));
		}
		
	}


	public void Return(boolean DAMAGED) {
		if (bookState.equals(STATE.ON_LOAN)) {
			if (DAMAGED) {
				bookState = STATE.DAMAGED;
			}
			else {
				bookState = STATE.AVAILABLE;
			}
		}
		else {
			throw new RuntimeException(String.format("Book: cannot Return while book is in state: %s", bookState));
		}		
	}

	
	public void Repair() {
		if (bookState.equals(STATE.DAMAGED)) {
			bookState = STATE.AVAILABLE;
		}
		else {
			throw new RuntimeException(String.format("Book: cannot repair while book is in state: %s", bookState));
		}
	}


}
