import java.awt.print.Book;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
//import Library //.library;

public class BorrowBookControl {
	private BorrowBookUi borrowBookUi;
	private Library library;
	private Member member;
	private enum ControlState { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
	private ControlState controlState;
	private List<Book> booksPending;
	private List<Loan> booksCompleted;
	private Book book;
	
	public BorrowBookControl() {
		this.library = Library.getInstance();
		controlState = ControlState.INITIALISED;
	}
	
	public void SetUi(BorrowBookUi ui) {
		if (!controlState.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
		}
		this.borrowBookUi = ui;
		ui.setState(BorrowBookUi.UiState.READY);
		controlState = ControlState.READY;		
	}
		
	public void isSwiped(int memberId) {
		if (!controlState.equals(ControlState.READY)) {
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
		}
		Member cardedMember = Library.member(memberId);
		if (cardedMember == null) {
			borrowBookUi.display("Invalid memberId");
			return;
		}
		if (library.memberCanBorrow(member)) {
			booksPending = new ArrayList<>();
		//	borrowBookUi.setState(BorrowBookUi.UiState.SCANNING);
			borrowBookUi.setState(borrowBookUi.uiState.SCANNING);
			controlState = ControlState.SCANNING;
		} else {
			borrowBookUi.display("Member cannot borrow at this time");
		//	borrowBookUi.setState(BorrowBookUi.UiState.RESTRICTED);
			borrowBookUi.setState(borrowBookUi.uiState.RESTRICTED);
		}
	}
		
	public void isScanned(int bookId) {
		book = null;
		if (!controlState.equals(ControlState.SCANNING)) {
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
		}	
		book = library.book(bookId);
		if (book == null) {
			borrowBookUi.display("Invalid bookId");
			return;
		}
		if (!book.isAvailable()) {
			borrowBookUi.display("Book cannot be borrowed");
			return;
		}
		booksPending.add(book);
		for (Book thisBook : booksPending) {
			borrowBookUi.display(thisBook.toString());
		}
		if (library.loansRemainingForMember(member) - booksPending.size() == 0) {
			borrowBookUi.display("Loan limit reached");
			isComplete();
		}
	}
	
	public void isComplete() {
		if (booksPending.size() == 0) {
			cancel();
		} else {
			borrowBookUi.display("\nFinal Borrowing List");
			for (Book thisBook : booksPending) {
				borrowBookUi.display(thisBook.toString());
			}
			booksCompleted = new ArrayList<Loan>();
			borrowBookUi.setState(borrowBookUi.UiState.FINALISING);
			controlState = ControlState.FINALISING;
		}
	}

	public void commitLoans() {
		if (!controlState.equals(ControlState.FINALISING)) {
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
		}	
		for (Book thisBook : booksPending) {
			Loan loan = library.issueLoan(thisBook, member);
			booksCompleted.add(loan);			
		}
		borrowBookUi.display("Completed Loan Slip");
		for (Loan loan : booksCompleted) {
			borrowBookUi.display(loan.toString());
		}
		borrowBookUi.setState(borrowBookUi.uiState.COMPLETED);
		controlState = ControlState.COMPLETED;
	}
	
	public void Cancel() {
		borrowBookUi.setState(borrowBookUi.uiState.CANCELLED);
		controlState = ControlState.CANCELLED;
	}
}
