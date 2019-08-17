public class ReturnBookControl {
	private ReturnBookUi returnBookUi;
	private enum ControlState { INITIALISED, READY, INSPECTING };
	private ControlState bookReturnState;
	private Library library;
	private Loan currentLoan;

	public ReturnBookControl() {
		this.library = Library.instance();
		bookReturnState = ControlState.INITIALISED;
	}
	
	public void setUi(ReturnBookUi ui) {
		if (!bookReturnState.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
		}	
		this.returnBookUi = ui;
		ui.setState(ReturnBookUi.UiState.READY);
		bookReturnState = ControlState.READY;		
	}

	public void bookScanned(int bookId) {
		if (!bookReturnState.equals(ControlState.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		}	
		Book currentBook = library.book(bookId);
		if (currentBook == null) {
			returnBookUi.display("Invalid Book Id");
			return;
		}
		if (!currentBook.onLoan()) {
			returnBookUi.display("Book has not been borrowed");
			return;
		}		
		currentLoan = library.loanByBookId(bookId);	
		double overDueFine = 0.0;
		if (currentLoan.isOverdue()) {
			overDueFine = library.calculateOverdueFine(currentLoan);
		}
		returnBookUi.display("Inspecting");
		returnBookUi.display(currentBook.toString());
		returnBookUi.display(currentLoan.toString());
		
		if (currentLoan.isOverdue()) {
			returnBookUi.display(String.format("\nOverdue fine : $%.2f", overDueFine));
		}
		returnBookUi.setState(ReturnBookUi.UiState.INSPECTING);
		bookReturnState = ControlState.INSPECTING;		
	}

	public void scanningComplete() {
		if (!bookReturnState.equals(ControlState.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
		}	
		returnBookUi.setState(ReturnBookUi.UiState.COMPLETED);		
	}

	public void dischargeLoan(boolean isDamaged) {
		if (!bookReturnState.equals(ControlState.INSPECTING)) {
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
		}	
		library.dischargeLoan(currentLoan, isDamaged);
		currentLoan = null;
		returnBookUi.setState(ReturnBookUi.UiState.READY);
		bookReturnState = ControlState.READY;				
	}
}
