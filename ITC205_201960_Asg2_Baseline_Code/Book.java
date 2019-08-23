import java.io.Serializable;


@SuppressWarnings("serial")
public class Book implements Serializable {
	
	private String title;
	private String author;
	private String callNumber;
	private int idNumber;
	
	private enum BookState { AVAILABLE, ON_LOAN, DAMAGED, RESERVED };
	private BookState bookState;
	
	
	public Book(String author, String title, String callNumber, int idNumber) {
		this.author = author;
		this.title = title;
		this.callNumber = callNumber;
		this.idNumber = idNumber;
		this.bookState = BookState.AVAILABLE;
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

	public Integer id() {
		return idNumber;
	}

	public String title() {
		return title;
	}
	
	public boolean isAvailable() {
		return bookState == BookState.AVAILABLE;
	}
	
	public boolean onLoan() {
		return bookState == BookState.ON_LOAN;
	}
	
	public boolean isDamaged() {
		return bookState == BookState.DAMAGED;
	}

	public void borrowBook() {
		if (bookState.equals(BookState.AVAILABLE)) {
			bookState = BookState.ON_LOAN;
		} else {
			throw new RuntimeException(String.format("Book: cannot borrow while book is in state: %s", bookState));
		}
	}

	public void returnBook(boolean isDamaged) {
		if (bookState.equals(BookState.ON_LOAN)) {
			if (isDamaged) {
				bookState = BookState.DAMAGED;
			} else {
				bookState = BookState.AVAILABLE;
			}
		} else {
			throw new RuntimeException(String.format("Book: cannot Return while book is in state: %s", bookState));
		}	
	}

	
	public void repairBook() {
		if (bookState.equals(BookState.DAMAGED)) {
			bookState = BookState.AVAILABLE;
		} else {
			throw new RuntimeException(String.format("Book: cannot repair while book is in state: %s", bookState));
		}
	}

}
