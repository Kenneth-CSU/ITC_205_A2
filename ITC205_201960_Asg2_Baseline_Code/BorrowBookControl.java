import java.util.ArrayList;
import java.util.List;

public class BorrowBookControl {
	
	private BorrowBookUI borrowBookUI;
	
	private library library;
	private member member;
	private enum CONTROL_STATE { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
	private CONTROL_STATE controlState;
	
	private List<book> booksPending;
	private List<loan> booksCompleted;
	private book book;
	
	
	public BorrowBookControl() {
		this.library = library.INSTANCE();
		controlState = CONTROL_STATE.INITIALISED;
	}
	

	public void setUI(BorrowBookUI ui) {
		if (!controlState.equals(CONTROL_STATE.INITIALISED)) 
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
			
		this.borrowBookUI = ui;
		ui.Set_State(BorrowBookUI.UI_STATE.READY);
		controlState = CONTROL_STATE.READY;		
	}

		
	public void Swiped(int memberId) {
		if (!controlState.equals(CONTROL_STATE.READY)) 
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
			
		member = library.MEMBER(memberId);
		if (member == null) {
			borrowBookUI.Display("Invalid memberId");
			return;
		}
		if (library.MEMBER_CAN_BORROW(member)) {
			booksPending = new ArrayList<>();
			borrowBookUI.Set_State(BorrowBookUI.UI_STATE.SCANNING);
			controlState = CONTROL_STATE.SCANNING; }
		else 
		{
			borrowBookUI.Display("Member cannot borrow at this time");
			borrowBookUI.Set_State(BorrowBookUI.UI_STATE.RESTRICTED); }}
	
	
	public void Scanned(int bookId) {
		book = null;
		if (!controlState.equals(CONTROL_STATE.SCANNING)) {
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
		}	
		book = library.Book(bookId);
		if (book == null) {
			borrowBookUI.Display("Invalid bookId");
			return;
		}
		if (!book.AVAILABLE()) {
			borrowBookUI.Display("Book cannot be borrowed");
			return;
		}
		booksPending.add(book);
		for (book thisBook : booksPending) {
			borrowBookUI.Display(thisBook.toString());
		}
		if (library.Loans_Remaining_For_Member(member) - booksPending.size() == 0) {
			borrowBookUI.Display("Loan limit reached");
			Complete();
		}
	}
	
	
	public void Complete() {
		if (booksPending.size() == 0) {
			cancel();
		}
		else {
			borrowBookUI.Display("\nFinal Borrowing List");
			for (book thisBook : booksPending) {
				borrowBookUI.Display(thisBook.toString());
			}
			booksCompleted = new ArrayList<loan>();
			borrowBookUI.Set_State(BorrowBookUI.UI_STATE.FINALISING);
			controlState = CONTROL_STATE.FINALISING;
		}
	}


	public void Commit_LOans() {
		if (!controlState.equals(CONTROL_STATE.FINALISING)) {
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
		}	
		for (book thisBook : booksPending) {
			loan LOAN = library.ISSUE_LAON(thisBook, member);
			booksCompleted.add(LOAN);			
		}
		borrowBookUI.Display("Completed Loan Slip");
		for (loan LOAN : booksCompleted) {
			borrowBookUI.Display(LOAN.toString());
		}
		borrowBookUI.Set_State(BorrowBookUI.UI_STATE.COMPLETED);
		controlState = CONTROL_STATE.COMPLETED;
	}

	
	public void cancel() {
		borrowBookUI.Set_State(BorrowBookUI.UI_STATE.CANCELLED);
		controlState = CONTROL_STATE.CANCELLED;
	}
	
	
}
