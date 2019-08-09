import java.io.Serializable;


@SuppressWarnings("serial")
public class Book implements Serializable {
	
	private String TITLE;
	private String AUTHOR;
	private String CALLNO;
	private int ID;
	
	private enum State { AVAILABLE, ON_LOAN, DAMAGED, RESERVED };
	private State State;
	
	
	public Book(String author, String title, String callNo, int id) {
		this.AUTHOR = author;
		this.TITLE = title;
		this.CALLNO = callNo;
		this.ID = id;
		this.State = State.AVAILABLE;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Book: ").append(ID).append("\n")
		  .append("  Title:  ").append(TITLE).append("\n")
		  .append("  Author: ").append(AUTHOR).append("\n")
		  .append("  CallNo: ").append(CALLNO).append("\n")
		  .append("  State:  ").append(State);
		
		return sb.toString();
	}

	public Integer ID() {
		return ID;
	}

	public String Title() {
		return TITLE;
	}


	
	public boolean isAvailable() {
		return State == State.AVAILABLE;
	}

	
	public boolean isOnLoan() {
		return State == State.ON_LOAN;
	}

	
	public boolean isDamaged() {
		return State == State.DAMAGED;
	}

	
	public void Borrow() {
		if (State.equals(State.AVAILABLE)) {
			State = State.ON_LOAN;
		}
		else {
			throw new RuntimeException(String.format("Book: cannot borrow while book is in state: %s", State));
		}
		
	}


	public void Return(boolean DAMAGED) {
		if (State.equals(State.ON_LOAN)) {
			if (DAMAGED) {
				State = State.DAMAGED;
			}
			else {
				State = State.AVAILABLE;
			}
		}
		else {
			throw new RuntimeException(String.format("Book: cannot Return while book is in state: %s", State));
		}		
	}

	
	public void Repair() {
		if (State.equals(State.DAMAGED)) {
			State = State.AVAILABLE;
		}
		else {
			throw new RuntimeException(String.format("Book: cannot repair while book is in state: %s", State));
		}
	}


}
