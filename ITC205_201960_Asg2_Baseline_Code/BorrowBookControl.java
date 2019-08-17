import java.util.ArrayList;
import java.util.List;

public class BorrowBookControl {
	
	private BorrowBookUI borrowBookUI;
	

	private Library library;
	private Member member;
	private enum ControlState { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
	private ControlState controlState;
	private List<book> booksPending;
	private List<loan> booksCompleted;
	private Book book;
	
	
	public BorrowBookControl() {
		this.library = library.instance();
		controlState = ControlState.INITIALISED;
	}
	
	public void setUI(BorrowBookUI ui) {
		if (!controlState.equals(ControlState.INITIALISED)){
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
		}
		this.borrowBookUI = ui;
		ui.setState(BorrowBookUI.uiState.READY);
		controlState = ControlState.READY;		
	}
		
	public void swiped(int memberId) {
		if (!controlState.equals(ControlState.READY)) 
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
		}
		member = library.member(memberId);
		if (member == null) {
			borrowBookUI.display("Invalid memberId");
			return;
		}
		if (library.MEMBER_CAN_BORROW(member)) {
			booksPending = new ArrayList<>();
			borrowBookUI.setState(BorrowBookUI.uiState.SCANNING);
			controlState = ControlState.SCANNING; }
		else 
		{
			borrowBookUI.display("Member cannot borrow at this time");
			borrowBookUI.setState(BorrowBookUI.uiState.RESTRICTED); }}
		

	public void scanned(int bookId) {
		book = null;
		if (!controlState.equals(ControlState.SCANNING)) {
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
		}	
		book = library.book(bookId);
		if (book == null) {
			borrowBookUI.display("Invalid bookId");
			return;
		}
		if (!book.AVAILABLE()) {
			borrowBookUI.display("Book cannot be borrowed");
			return;
		}
		booksPending.add(book);
		for (book thisBook : booksPending) {
			borrowBookUI.display(thisBook.toString());
		}
		if (library.Loans_Remaining_For_Member(member) - booksPending.size() == 0) {
			borrowBookUI.display("Loan limit reached");
			Complete();
		}
	}
	
	
	public void Complete() {

		if (booksPending.size() == 0) {
			cancel();
		}
		else {
			borrowBookUI.display("\nFinal Borrowing List");
			for (book thisBook : booksPending) {
				borrowBookUI.display(thisBook.toString());
			}
			booksCompleted = new ArrayList<Loan>();
			borrowBookUI.setState(BorrowBookUI.uiState.FINALISING);
			controlState = ControlState.FINALISING;
		}
	}

	public void CommitLoans() {
		if (!controlState.equals(ControlState.FINALISING)) {
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
		}	
		for (book thisBook : booksPending) {
			Loan loan = library.issueLoan(thisBook, member);
			booksCompleted.add(loan);			
		}
		borrowBookUI.display("Completed Loan Slip");
		for (Loan loan : booksCompleted) {
			borrowBookUI.display(loan.toString());
		}
		borrowBookUI.setState(BorrowBookUI.uiState.COMPLETED);
		controlState = ControlState.COMPLETED;
	}
	
	public void cancel() {
		borrowBookUI.setState(BorrowBookUI.uiState.CANCELLED);
		controlState = ControlState.CANCELLED;

	}
}
