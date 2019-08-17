import java.util.Scanner;


public class FixBookUI {
	public static enum UiState { INITIALISED, READY, FIXING, COMPLETED };
	private FixBookControl fixBookControl;
	private Scanner scannerInput;
	private UiState uiState;
	
	public fixBookUI(FixBookControl control) {
		this.fixBookControl = control;
		scannerInput = new Scanner(System.in);
		uiState = UiState.INITIALISED;
		control.setUi(this);
	}

	public void setState(UiState state) {
		this.uiState = state;
	}
	
	public void Run() {
		output("Fix Book Use Case UI\n");
		while (true) {
			switch (uiState) {
			case READY:
				String bookScanString = input("Scan Book (<enter> completes): ");
				if (bookScanString.length() == 0) {
					fixBookControl.scanningComplete();
				}
				else {
					try {
						int bookId = Integer.valueOf(bookScanString).intValue();
						fixBookControl.Book_scanned(bookId);
					}
					catch (NumberFormatException e) {
						output("Invalid bookId");
					}
				}
				break;	
				
			case FIXING:
				String fixBookAnswer = input("Fix Book? (Y/N) : ");
				boolean fixBookFlag = false;
				if (fixBookAnswer.toUpperCase().equals("Y")) {
					fixBookFlag = true;
				}
				fixBookControl.fixBook(fixBookFlag);
				break;
								
			case COMPLETED:
				output("Fixing process complete");
				return;
			
			default:
				output("Unhandled state");
				throw new RuntimeException("FixBookUI : unhandled state :" + uiState);			
			}		
		}
	}
	
	private String Input(String prompt) {
		System.out.print(prompt);
		return scannerInput.nextLine();
	}	
		
	private void Output(Object object) {
		System.out.println(object);
	}

	public void Display(Object object) {
		output(object);
	}
}
