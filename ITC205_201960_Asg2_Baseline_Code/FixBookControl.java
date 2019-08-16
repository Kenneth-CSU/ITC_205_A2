public class FixBookControl {
	
	private FixBookUi UI;
	private enum ControlState { INITIALISED, READY, FIXING };
	private ControlState StAtE;
	
	private Library LIB;
	private Book Cur_Book;


	public FixBookControl() {
		this.LIB = LIB.instance();
		StAtE = ControlState.INITIALISED;
	}
	
	
	public void SetUi(FixBookUI ui) {
		if (!StAtE.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
		}	
		this.UI = ui;
		ui.SetState(FixBookUI.uiState.READY);
		StAtE = ControlState.READY;		
	}


	public void BookScanned(int bookId) {
		if (!StAtE.equals(ControlState.READY)) {
			throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
		}	
		Cur_Book = LIB.Book(bookId);
		
		if (Cur_Book == null) {
			UI.display("Invalid bookId");
			return;
		}
		if (!Cur_Book.isDamaged()) {
			UI.display("Book has not been damaged");
			return;
		}
		UI.display(Cur_Book.toString());
		UI.setState(FixBookUI.uiState.FIXING);
		StAtE = ControlState.FIXING;		
	}


	public void FixBook(boolean MUST_fix) {
		if (!StAtE.equals(ControlState.FIXING)) {
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
		}	
		if (MUST_fix) {
			LIB.repairBook(Cur_Book);
		}
		Cur_Book = null;
		UI.setState(FixBookUI.uiState.READY);
		StAtE = ControlState.READY;		
	}

	
	public void ScanningComplete() {
		if (!StAtE.equals(ControlState.READY)) {
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
		}	
		UI.setState(FixBookUI.uiState.COMPLETED);		
	}






}
