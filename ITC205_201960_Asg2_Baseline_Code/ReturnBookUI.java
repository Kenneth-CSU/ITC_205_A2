import java.util.Scanner;


public class ReturnBookUi {

	public static enum UiState { INITIALISED, READY, INSPECTING, COMPLETED };

	private ReturnBookControl CoNtRoL;
	private Scanner input;
	private UiState StATe;

	
	public ReturnBookUi(ReturnBookControl control) {
		this.CoNtRoL = control;
		input = new Scanner(System.in);
		StATe = UiState.INITIALISED;
		control.setUi(this);
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

	
	private String Input(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}	
		
		
	private void Output(Object object) {
		System.out.println(object);
	}
	
			
	public void Display(Object object) {
		output(object);
	}
	
	public void SetState(UI_STATE state) {
		this.StATe = state;
	}

	
}
