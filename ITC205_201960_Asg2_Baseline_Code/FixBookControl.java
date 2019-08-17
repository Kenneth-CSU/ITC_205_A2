public class FixBookControl {
	
	private FixBookUI fixBookUi;
	private enum ControlState { INITIALISED, READY, FIXING };
	private ControlState controlState;
	private Library currentLibrary;
	private Book currentBook;

	public FixBookControl() {
		this.currentLibrary = currentLibrary.instance();
		controlState = ControlState.INITIALISED;
	}
		
	public void setUi(FixBookUI userInterface) {
		if (!controlState.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
		}	
		this.fixBookUi = userInterface;
		userInterface.setState(FixBookUI.uiState.READY);
		controlState = ControlState.READY;		
	}

	public void bookScanned(int bookId) {
		if (!controlState.equals(ControlState.READY)) {

			throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
		}	
		currentBook = currentLibrary.Book(bookId);
		
		if (currentBook == null) {
			fixBookUi.display("Invalid bookId");
			return;
		}
		if (!currentBook.isDamaged()) {
			fixBookUi.display("Book has not been damaged");
			return;
		}
		fixBookUi.display(currentBook.toString());
		fixBookUi.setState(FixBookUI.uiState.FIXING);
		controlState = ControlState.FIXING;		
	}

	public void fixBook(boolean mustFixBook) {
		if (!controlState.equals(ControlState.FIXING)) {
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
		}	
		if (mustFixBook) {
			currentLibrary.repairBook(currentBook);
		}
		currentBook = null;
		fixBookUi.setState(FixBookUI.uiState.READY);
		controlState = ControlState.READY;		
	}
	
	public void scanningComplete() {
		if (!controlState.equals(ControlState.READY)) {
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
		}	
		fixBookUi.setState(FixBookUI.uiState.COMPLETED);		
	}
}
