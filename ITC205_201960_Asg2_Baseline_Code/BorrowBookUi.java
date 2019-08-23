import java.util.Scanner;


public class BorrowBookUi {
    
	public static enum UiState { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
	private BorrowBookControl borrowBookControl;
	private Scanner inputScanner;
	private UiState uiState;
	
	public BorrowBookUi(BorrowBookControl control) {
		this.borrowBookControl = control;
		inputScanner = new Scanner(System.in);
		uiState = uiState.INITIALISED;
		control.setUi(this);
	}
	
	private String input(String prompt) {
		System.out.print(prompt);
		return inputScanner.nextLine();
	}	
			
	private void output(Object object) {
		System.out.println(object);
	}
			
	public void setState(UiState uiState) {
		this.uiState = uiState;
	}

	public void run() {
		output("Borrow Book Use Case UI\n");
		while (true) {
			switch (uiState) {			

			case CANCELLED:
				output("Borrowing Cancelled");
				return;

			case READY:
				String memberString = input("Swipe member card (press <enter> to cancel): ");
				if (memberString.length() == 0) {
					borrowBookControl.cancel();
					break;
				}
				try {
					int memberId = Integer.valueOf(memberString).intValue();
					borrowBookControl.isSwiped(memberId);
				}
				catch (NumberFormatException e) {
					output("Invalid Member Id");
				}
				break;
				
			case RESTRICTED:
				input("Press <any key> to cancel");
				borrowBookControl.cancel();
				break;

			case SCANNING:
				String bookString = input("Scan Book (<enter> completes): ");
				if (bookString.length() == 0) {
					borrowBookControl.isComplete();
					break;
				}
				try {
					int bookId = Integer.valueOf(bookString).intValue();
					borrowBookControl.isScanned(bookId);
				} catch (NumberFormatException e) {
					output("Invalid Book Id");
				} 
				break;
					
			case FINALISING:
				String inputString = input("Commit loans? (Y/N): ");
				if (inputString.toUpperCase().equals("N")) {
					borrowBookControl.cancel();
				} else {
					borrowBookControl.commitLoans();
					input("Press <any key> to complete ");
				}
				break;
				
			case COMPLETED:
				output("Borrowing Completed");
				return;
			
			default:
				output("Unhandled state");
				throw new RuntimeException("BorrowBookUI : unhandled state :" + uiState);			
			}
		}		
	}

	public void display(Object object) {
		output(object);		
	}
}
