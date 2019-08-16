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
		this.LIBRARY = LIBRARY.instance();
		State = ControlState.INITIALISED;
	}
	

	public void SetUi(BorrowBookUI ui) {
		if (!State.equals(ControlState.INITIALISED)) 
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
			
		this.UI = ui;
		ui.setState(BorrowBookUI.UiState.READY);
		State = ControlState.READY;		
	}

		
	public void Swiped(int MEMMER_ID) {
		if (!State.equals(ControlState.READY)) 
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
			
		M = LIBRARY.member(MEMMER_ID);
		if (M == null) {
			UI.display("Invalid memberId");
			return;
		}
		if (LIBRARY.MEMBER_CAN_BORROW(M)) {
			PENDING = new ArrayList<>();
			UI.setState(BorrowBookUI.uiState.SCANNING);
			State = ControlState.SCANNING; }
		else 
		{
			UI.Display("Member cannot borrow at this time");
			UI.SetState(BorrowBookUI.uiState.RESTRICTED); }}
	
	
	public void Scanned(int bookId) {
		BOOK = null;
		if (!State.equals(ControlState.SCANNING)) {
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
		}	
		BOOK = LIBRARY.book(bookId);
		if (BOOK == null) {
			UI.display("Invalid bookId");
			return;
		}
		if (!BOOK.isAvailable()) {
			UI.display("Book cannot be borrowed");
			return;
		}
		PENDING.add(BOOK);
		for (book B : PENDING) {
			UI.display(B.toString());
		}
		if (LIBRARY.loansRemainingForMember(M) - PENDING.size() == 0) {
			UI.display("Loan limit reached");
			Complete();
		}
	}
	
	
	public void Complete() {
		if (PENDING.size() == 0) {
			Cancel();
		}
		else {
			UI.display("\nFinal Borrowing List");
			for (book B : PENDING) {
				UI.display(B.toString());
			}
			COMPLETED = new ArrayList<loan>();
			UI.SetState(BorrowBookUI.uiState.FINALISING);
			State = ControlState.FINALISING;
		}
	}


	public void CommitLoans() {
		if (!State.equals(ControlState.FINALISING)) {
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
		}	
		for (book B : PENDING) {
			loan LOAN = LIBRARY.issueLoan(B, M);
			COMPLETED.add(LOAN);			
		}
		UI.display("Completed Loan Slip");
		for (loan LOAN : COMPLETED) {
			UI.display(LOAN.toString());
		}
		UI.setState(BorrowBookUI.uiState.COMPLETED);
		State = ControlState.COMPLETED;
	}

	
	public void Cancel() {
		UI.SetState(BorrowBookUI.UiState.CANCELLED);
		State = ControlState.CANCELLED;
	}
	
	
}
