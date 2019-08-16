public class ReturnBookControl {

	private ReturnBookUI returnBookUi;
	private enum CONTROL_STATE { INITIALISED, READY, INSPECTING };
	private CONTROL_STATE bookReturnState;
	
	private library library;
	private loan currentLoan;
	

	public ReturnBookControl() {
		this.library = library.INSTANCE();
		bookReturnState = CONTROL_STATE.INITIALISED;
	}
	
	
	public void Set_UI(ReturnBookUI ui) {
		if (!bookReturnState.equals(CONTROL_STATE.INITIALISED)) {
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
		}	
		this.returnBookUi = ui;
		ui.Set_State(ReturnBookUI.UI_STATE.READY);
		bookReturnState = CONTROL_STATE.READY;		
	}


	public void Book_scanned(int bookId) {
		if (!bookReturnState.equals(CONTROL_STATE.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		}	
		book currentBook = library.Book(bookId);
		
		if (currentBook == null) {
			returnBookUi.display("Invalid Book Id");
			return;
		}
		if (!currentBook.On_loan()) {
			returnBookUi.display("Book has not been borrowed");
			return;
		}		
		currentLoan = library.LOAN_BY_BOOK_ID(bookId);	
		double overDueFine = 0.0;
		if (currentLoan.OVer_Due()) {
			overDueFine = library.CalculateOverDueFine(currentLoan);
		}
		returnBookUi.display("Inspecting");
		returnBookUi.display(currentBook.toString());
		returnBookUi.display(currentLoan.toString());
		
		if (currentLoan.OVer_Due()) {
			returnBookUi.display(String.format("\nOverdue fine : $%.2f", overDueFine));
		}
		returnBookUi.Set_State(ReturnBookUI.UI_STATE.INSPECTING);
		bookReturnState = CONTROL_STATE.INSPECTING;		
	}


	public void Scanning_Complete() {
		if (!bookReturnState.equals(CONTROL_STATE.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
		}	
		returnBookUi.Set_State(ReturnBookUI.UI_STATE.COMPLETED);		
	}


	public void Discharge_loan(boolean isDamaged) {
		if (!bookReturnState.equals(CONTROL_STATE.INSPECTING)) {
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
		}	
		library.Discharge_loan(currentLoan, isDamaged);
		currentLoan = null;
		returnBookUi.Set_State(ReturnBookUI.UI_STATE.READY);
		bookReturnState = CONTROL_STATE.READY;				
	}


}
