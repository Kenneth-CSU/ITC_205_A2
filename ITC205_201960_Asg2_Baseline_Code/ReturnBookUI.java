import java.util.Scanner;


public class ReturnBookUI {

	public static enum UI_STATE { INITIALISED, READY, INSPECTING, COMPLETED };

	private ReturnBookControl returnBookControl;
	private Scanner inputScanner;
	private UI_STATE returnBookUiState;

	
	public ReturnBookUI(ReturnBookControl newReturnBookControl) {
		this.returnBookControl = newReturnBookControl;
		inputScanner = new Scanner(System.in);
		returnBookUiState = UI_STATE.INITIALISED;
		newReturnBookControl.Set_UI(this);
	}


	public void RuN() {		
		output("Return Book Use Case UI\n");
		
		while (true) {
			
			switch (returnBookUiState) {
			
			case INITIALISED:
				break;
				
			case READY:
				String bookScanString = input("Scan Book (<enter> completes): ");
				if (bookScanString.length() == 0) {
					returnBookControl.Scanning_Complete();
				}
				else {
					try {
						int newBookId = Integer.valueOf(bookScanString).intValue();
						returnBookControl.Book_scanned(newBookId);
					}
					catch (NumberFormatException e) {
						output("Invalid bookId");
					}					
				}
				break;				
				
			case INSPECTING:
				String userResponse = input("Is book damaged? (Y/N): ");
				boolean isDamaged = false;
				if (userResponse.toUpperCase().equals("Y")) {					
					isDamaged = true;
				}
				returnBookControl.Discharge_loan(isDamaged);
			
			case COMPLETED:
				output("Return processing complete");
				return;
			
			default:
				output("Unhandled state");
				throw new RuntimeException("ReturnBookUI : unhandled state :" + returnBookUiState);			
			}
		}
	}

	
	private String input(String prompt) {
		System.out.print(prompt);
		return inputScanner.nextLine();
	}	
		
		
	private void output(Object object) {
		System.out.println(object);
	}
	
			
	public void display(Object object) {
		output(object);
	}
	
	public void Set_State(UI_STATE state) {
		this.returnBookUiState = state;
	}

	
}
