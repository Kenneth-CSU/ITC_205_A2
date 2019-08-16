public class FixBookControl {
	
	private FixBookUI fixBookUserInterface;
	private enum CONTROL_STATE { INITIALISED, READY, FIXING };
	private CONTROL_STATE controlState;
	
	private library currentLibrary;
	private book currentBook;


	public FixBookControl() {
		this.currentLibrary = currentLibrary.INSTANCE();
		controlState = CONTROL_STATE.INITIALISED;
	}
	
	
	public void Set_Ui(FixBookUI userInterface) {
		if (!controlState.equals(CONTROL_STATE.INITIALISED)) {
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
		}	
		this.fixBookUserInterface = userInterface;
		userInterface.Set_State(FixBookUI.UI_STATE.READY);
		controlState = CONTROL_STATE.READY;		
	}


	public void Book_scanned(int bookId) {
		if (!controlState.equals(CONTROL_STATE.READY)) {
			throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
		}	
		currentBook = currentLibrary.Book(bookId);
		
		if (currentBook == null) {
			fixBookUserInterface.display("Invalid bookId");
			return;
		}
		if (!currentBook.IS_Damaged()) {
			fixBookUserInterface.display("Book has not been damaged");
			return;
		}
		fixBookUserInterface.display(currentBook.toString());
		fixBookUserInterface.Set_State(FixBookUI.UI_STATE.FIXING);
		controlState = CONTROL_STATE.FIXING;		
	}


	public void FIX_Book(boolean MUST_fix) {
		if (!controlState.equals(CONTROL_STATE.FIXING)) {
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
		}	
		if (MUST_fix) {
			currentLibrary.Repair_BOOK(currentBook);
		}
		currentBook = null;
		fixBookUserInterface.Set_State(FixBookUI.UI_STATE.READY);
		controlState = CONTROL_STATE.READY;		
	}

	
	public void SCannING_COMplete() {
		if (!controlState.equals(CONTROL_STATE.READY)) {
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
		}	
		fixBookUserInterface.Set_State(FixBookUI.UI_STATE.COMPLETED);		
	}






}
