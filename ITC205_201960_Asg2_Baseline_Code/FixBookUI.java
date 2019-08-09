import java.util.Scanner;


public class FixBookUI {

	public static enum UIState { INITIALISED, READY, FIXING, COMPLETED };

	private FixBookControl CoNtRoL;
	private Scanner input;
	private UIState StAtE;

	
	public FixBookUI(FixBookControl control) {
		this.CoNtRoL = control;
		input = new Scanner(System.in);
		StAtE = UIState.INITIALISED;
		control.SetUI(this);
	}


	public void SetState(UIState state) {
		this.StAtE = state;
	}

	
	public void Run() {
		output("Fix Book Use Case UI\n");
		
		while (true) {
			
			switch (StAtE) {
			
			case READY:
				String Book_STR = input("Scan Book (<enter> completes): ");
				if (Book_STR.length() == 0) {
					CoNtRoL.scanningComplete();
				}
				else {
					try {
						int Book_ID = Integer.valueOf(Book_STR).intValue();
						CoNtRoL.bookScanned(Book_ID);
					}
					catch (NumberFormatException e) {
						output("Invalid bookId");
					}
				}
				break;	
				
			case FIXING:
				String AnS = input("Fix Book? (Y/N) : ");
				boolean FiX = false;
				if (AnS.toUpperCase().equals("Y")) {
					FiX = true;
				}
				CoNtRoL.FIX_Book(FiX);
				break;
								
			case COMPLETED:
				output("Fixing process complete");
				return;
			
			default:
				output("Unhandled state");
				throw new RuntimeException("FixBookUI : unhandled state :" + StAtE);			
			
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
	
	
}
