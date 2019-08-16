import java.util.Scanner;


public class FixBookUI {

	public static enum UI_STATE { INITIALISED, READY, FIXING, COMPLETED };

	private FixBookControl fixBookControl;
	private Scanner scannerInput;
	private UI_STATE uiState;

	
	public FixBookUI(FixBookControl control) {
		this.fixBookControl = control;
		scannerInput = new Scanner(System.in);
		uiState = UI_STATE.INITIALISED;
		control.Set_Ui(this);
	}


	public void Set_State(UI_STATE state) {
		this.uiState = state;
	}

	
	public void RuN() {
		output("Fix Book Use Case UI\n");
		
		while (true) {
			
			switch (uiState) {
			
			case READY:
				String bookScanString = input("Scan Book (<enter> completes): ");
				if (bookScanString.length() == 0) {
					fixBookControl.SCannING_COMplete();
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
				fixBookControl.FIX_Book(fixBookFlag);
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

	
	private String input(String prompt) {
		System.out.print(prompt);
		return scannerInput.nextLine();
	}	
		
		
	private void output(Object object) {
		System.out.println(object);
	}
	

	public void display(Object object) {
		output(object);
	}
	
	
}
