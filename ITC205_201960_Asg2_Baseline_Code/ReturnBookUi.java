import java.util.Scanner;

public class ReturnBookUi {
	public static enum UiState { INITIALISED, READY, INSPECTING, COMPLETED };
	private ReturnBookControl returnBookControl;
	private Scanner inputScanner;
	private UiState returnBookUiState;

	public ReturnBookUi(ReturnBookControl newReturnBookControl) {
		this.returnBookControl = newReturnBookControl;
		inputScanner = new Scanner(System.in);
		returnBookUiState = UiState.INITIALISED;
		newReturnBookControl.setUi(this);
	}

	public void run() {		
		output("Return Book Use Case UI\n");
		while (true) {
			switch (returnBookUiState) {
			case INITIALISED:
				break;
				
			case READY:
				String bookScanString = input("Scan Book (<enter> completes): ");
				if (bookScanString.length() == 0) {
					returnBookControl.scanningComplete();
				}
				else {
					try {
						int newBookId = Integer.valueOf(bookScanString).intValue();
						returnBookControl.bookScanned(newBookId);
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
				returnBookControl.dischargeLoan(isDamaged);
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
	
	public void setState(UiState state) {
		this.returnBookUiState = state;

	}
}
