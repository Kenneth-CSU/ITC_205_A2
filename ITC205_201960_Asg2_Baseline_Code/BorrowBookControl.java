import java.util.ArrayList;
import java.util.List;

public class BorrowBookControl {
	
	private BorrowBookUI UI;
	
	private Library LIBRARY;
	private Member M;
	private enum ControlState { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
	private ControlState State;
	
	private List<book> PENDING;
	private List<loan> COMPLETED;
	private book BOOK;
	
	
	public BorrowBookControl() {
		this.LIBRARY = LIBRARY.INSTANCE();
		State = ControlState.INITIALISED;
	}
	

	public void setUI(BorrowBookUI ui) {
		if (!State.equals(ControlState.INITIALISED)) 
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
			
		this.UI = ui;
		ui.SetState(BorrowBookUI.UIState.READY);
		State = ControlState.READY;		
	}

		
	public void Swiped(int MEMMER_ID) {
		if (!State.equals(ControlState.READY)) 
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
			
		M = LIBRARY.MEMBER(MEMMER_ID);
		if (M == null) {
			UI.Display("Invalid memberId");
			return;
		}
		if (LIBRARY.MEMBER_CAN_BORROW(M)) {
			PENDING = new ArrayList<>();
			UI.SetState(BorrowBookUI.UIState.SCANNING);
			State = ControlState.SCANNING; }
		else 
		{
			UI.Display("Member cannot borrow at this time");
			UI.SetState(BorrowBookUI.UIState.RESTRICTED); }}
	
	
	public void Scanned(int bookId) {
		BOOK = null;
		if (!State.equals(ControlState.SCANNING)) {
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
		}	
		BOOK = LIBRARY.Book(bookId);
		if (BOOK == null) {
			UI.display("Invalid bookId");
			return;
		}
		if (!BOOK.AVAILABLE()) {
			UI.display("Book cannot be borrowed");
			return;
		}
		PENDING.add(BOOK);
		for (book B : PENDING) {
			UI.display(B.toString());
		}
		if (LIBRARY.loansRemainingForMember(M) - PENDING.size() == 0) {
			UI.Display("Loan limit reached");
			Complete();
		}
	}
	
	
	public void Complete() {
		if (PENDING.size() == 0) {
			Cancel();
		}
		else {
			UI.Display("\nFinal Borrowing List");
			for (book B : PENDING) {
				UI.display(B.toString());
			}
			COMPLETED = new ArrayList<loan>();
			UI.SetState(BorrowBookUI.UIState.FINALISING);
			State = ControlState.FINALISING;
		}
	}


	public void CommitLoans() {
		if (!State.equals(ControlState.FINALISING)) {
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
		}	
		for (book B : PENDING) {
			loan LOAN = LIBRARY.ISSUE_LAON(B, M);
			COMPLETED.add(LOAN);			
		}
		UI.Display("Completed Loan Slip");
		for (loan LOAN : COMPLETED) {
			UI.display(LOAN.toString());
		}
		UI.Set_State(BorrowBookUI.UIState.COMPLETED);
		State = ControlState.COMPLETED;
	}

	
	public void Cancel() {
		UI.SetState(BorrowBookUI.UIState.CANCELLED);
		State = ControlState.CANCELLED;
	}
	
	
}
