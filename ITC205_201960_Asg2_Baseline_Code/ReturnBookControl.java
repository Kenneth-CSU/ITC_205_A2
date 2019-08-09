public class ReturnBookControl {

	private ReturnBookUI Ui;
	private enum ControlState { INITIALISED, READY, INSPECTING };
	private ControlState sTaTe;
	
	private class Library;
	private class CurrentLoan;
	

	public ReturnBookControl() {
		this.Library = Library.INSTANCE();
		sTaTe = ControlState.INITIALISED;
	}
	
	
	public void SetUI(ReturnBookUI ui) {
		if (!sTaTe.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
		}	
		this.Ui = ui;
		ui.Set_State(ReturnBookUI.UI_STATE.READY);
		sTaTe = ControlState.READY;		
	}


	public void BookScanned(int Book_ID) {
		if (!sTaTe.equals(ControlState.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		}	
		book CUR_book = Library.Book(Book_ID);
		
		if (CUR_book == null) {
			Ui.display("Invalid Book Id");
			return;
		}
		if (!CUR_book.On_loan()) {
			Ui.display("Book has not been borrowed");
			return;
		}		
		CurrentLoan = Library.LOAN_BY_BOOK_ID(Book_ID);	
		double Over_Due_Fine = 0.0;
		if (CurrentLoan.isOverdue()) {
			Over_Due_Fine = Library.CalculateOverdueFine(CurrentLoan);
		}
		Ui.display("Inspecting");
		Ui.display(CUR_book.toString());
		Ui.display(CurrentLoan.toString());
		
		if (CurrentLoan.isOverdue()) {
			Ui.display(String.format("\nOverdue fine : $%.2f", Overdue_Fine));
		}
		Ui.Set_State(ReturnBookUI.UI_STATE.INSPECTING);
		sTaTe = ControlState.INSPECTING;		
	}


	public void ScanningComplete() {
		if (!sTaTe.equals(ControlState.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
		}	
		Ui.SetState(ReturnBookUI.UISTATE.COMPLETED);		
	}


	public void DischargeLoan(boolean isDamaged) {
		if (!sTaTe.equals(ControlState.INSPECTING)) {
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
		}	
		Library.dischargeLoan(CurrentLoan, isDamaged);
		CurrentLoan = null;
		Ui.SetState(ReturnBookUI.UI_STATE.READY);
		sTaTe = ControlState.READY;				
	}


}
