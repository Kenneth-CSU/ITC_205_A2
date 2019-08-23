
import java.awt.print.Book;
//import Library.library;

public class FixBookControl {
	
	private FixBookUi fixBookUi;
	private enum ControlState { INITIALISED, READY, FIXING };
	private ControlState controlState;
	private Library currentLibrary;
	private Book currentBook;

	public FixBookControl() {
		this.currentLibrary = Library.getInstance();
		controlState = ControlState.INITIALISED;
	}
	
	public void setUi(FixBookUi fixBookUi) {
		if (!controlState.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
		}	
		this.fixBookUi = fixBookUi;
		fixBookUi.setState(FixBookUi.UiState.READY);
		controlState = ControlState.READY;		
	}

	public void bookScanned(int bookId) {
		if (!controlState.equals(ControlState.READY)) {
			throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
		}	
		Book currentBook = currentLibrary.book(bookId);
		if (currentBook == null) {
			fixBookUi.display("Invalid bookId");
			return;
		}
		boolean bookIsDamiges = currentBook.isDamaged();
		if (!bookIsDamiges) {
			fixBookUi.display("Book has not been damaged");
			return;
		}
		fixBookUi.display(currentBook.toString());
		fixBookUi.setState(FixBookUi.UiState.FIXING);
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
		fixBookUi.setState(FixBookUi.UiState.READY);
		controlState = ControlState.READY;		
	}
	
	public void scanningComplete() {
		if (!controlState.equals(ControlState.READY)) {
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
		}	
		fixBookUi.setState(FixBookUi.UiState.COMPLETED);		
	}
}
