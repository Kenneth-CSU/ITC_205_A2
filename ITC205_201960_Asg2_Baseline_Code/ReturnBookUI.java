import java.util.Scanner;


public class ReturnBookUI {

	public static enum UIState { INITIALISED, READY, INSPECTING, COMPLETED };

	private ReturnBookControl CoNtRoL;
	private Scanner input;
	private UIState StATe;

	
	public ReturnBookUI(ReturnBookControl control) {
		this.CoNtRoL = control;
		input = new Scanner(System.in);
		StATe = UIState.INITIALISED;
		control.setUI(this);
	}


	public void Run() {		
		output("Return Book Use Case UI\n");
		
		while (true) {
			
			switch (StATe) {
			
			case INITIALISED:
				break;
				
			case READY:
				String Book_STR = input("Scan Book (<enter> completes): ");
				if (Book_STR.length() == 0) {
					CoNtRoL.scanningComplete();
				}
				else {
					try {
						int Book_Id = Integer.valueOf(Book_STR).intValue();
						CoNtRoL.bookScanned(Book_Id);
					}
					catch (NumberFormatException e) {
						output("Invalid bookId");
					}					
				}
				break;				
				
			case INSPECTING:
				String ans = input("Is book damaged? (Y/N): ");
				boolean isDamaged = false;
				if (ans.toUpperCase().equals("Y")) {					
					isDamaged = true;
				}
				CoNtRoL.dischargeLoan(isDamaged);
			
			case COMPLETED:
				output("Return processing complete");
				return;
			
			default:
				output("Unhandled state");
				throw new RuntimeException("ReturnBookUI : unhandled state :" + StATe);			
			}
		}
	}

	
	private String input(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}	
		
		
	private void output(Object object) {
		System.out.println(object);
	}
	
			
	public void display(Object object) {
		output(object);
	}
	
	public void Set_State(UI_STATE state) {
		this.StATe = state;
	}

	
}
