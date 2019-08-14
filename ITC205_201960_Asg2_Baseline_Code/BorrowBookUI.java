import java.util.Scanner;


public class BorrowBookUI {
	
	public static enum UI_STATE { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };

	private BorrowBookControl borrowBookControl;
	private Scanner inputScanner;
	private UI_STATE uiState;

	
	public BorrowBookUI(BorrowBookControl control) {
		this.borrowBookControl = control;
		inputScanner = new Scanner(System.in);
		uiState = UI_STATE.INITIALISED;
		control.setUI(this);
	}

	
	private String input(String prompt) {
		System.out.print(prompt);
		return inputScanner.nextLine();
	}	
		
		
	private void output(Object object) {
		System.out.println(object);
	}
	
			
	public void Set_State(UI_STATE uiState) {
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
					borrowBookControl.Swiped(memberId);
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
					borrowBookControl.Complete();
					break;
				}
				try {
					int bookId = Integer.valueOf(bookString).intValue();
					borrowBookControl.Scanned(bookId);
					
				} catch (NumberFormatException e) {
					output("Invalid Book Id");
				} 
				break;
					
				
			case FINALISING:
				String inputString = input("Commit loans? (Y/N): ");
				if (inputString.toUpperCase().equals("N")) {
					borrowBookControl.cancel();
					
				} else {
					borrowBookControl.Commit_LOans();
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


	public void Display(Object object) {
		output(object);		
	}


}
