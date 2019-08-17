import java.io.Serializable;


@SuppressWarnings("serial")
public class Book implements Serializable {
	
	private String title;
	private String author;
	private String callNumber;
	private int idNumber;
	
	private enum State { AVAILABLE, ON_LOAN, DAMAGED, RESERVED };
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

	public Integer Id() {
		return idNumber;
	}

	public String Title() {
		return title;

	}
	
	public boolean Available() {
		return bookState == STATE.AVAILABLE;
	}
	
	public boolean OnLoan() {
		return bookState == STATE.ON_LOAN;
	}
	
	public boolean Damaged() {
		return bookState == STATE.DAMAGED;

	}
	
	public void Borrow() {
		if (bookState.equals(State.AVAILABLE)) {
			bookState = State.ON_LOAN;

		}
		else {
			throw new RuntimeException(String.format("Book: cannot borrow while book is in state: %s", bookState));
		}
		
	}


	public void Return(boolean DAMAGED) {
		if (bookState.equals(State.ON_LOAN)) {
			if (DAMAGED) {
				bookState = State.DAMAGED;
			}
			else {
				bookState = State.AVAILABLE;

			}
		}
		else {
			throw new RuntimeException(String.format("Book: cannot Return while book is in state: %s", bookState));
		}		
	}

	
	public void Repair() {
		if (bookState.equals(State.DAMAGED)) {
			bookState = State.AVAILABLE;

		}
		else {
			throw new RuntimeException(String.format("Book: cannot repair while book is in state: %s", bookState));
		}
	}


}
