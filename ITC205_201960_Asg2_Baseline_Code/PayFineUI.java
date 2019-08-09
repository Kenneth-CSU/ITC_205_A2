import java.util.Scanner;

public class PayFineUI {

	public static enum UIState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };

	private PayFineControl CoNtRoL;
	private Scanner input;
	private UISTATE StAtE;

	
	public PayFineUI(PayFineControl control) {
		this.CoNtRoL = control;
		input = new Scanner(System.in);
		StAtE = UISTATE.INITIALISED;
		control.Set_UI(this);
	}
	
	
	public void SetState(UI_STATE state) {
		this.StAtE = state;
	}


	public void Run() {
		output("Pay Fine Use Case UI\n");
		
		while (true) {
			
			switch (StAtE) {
			
			case READY:
				String Mem_Str = input("Swipe member card (press <enter> to cancel): ");
				if (Mem_Str.length() == 0) {
					CoNtRoL.CaNcEl();
					break;
				}
				try {
					int Member_ID = Integer.valueOf(Mem_Str).intValue();
					CoNtRoL.Card_Swiped(Member_ID);
				}
				catch (NumberFormatException e) {
					output("Invalid memberId");
				}
				break;
				
			case PAYING:
				double AmouNT = 0;
				String Amt_Str = input("Enter amount (<Enter> cancels) : ");
				if (Amt_Str.length() == 0) {
					CoNtRoL.CaNcEl();
					break;
				}
				try {
					AmouNT = Double.valueOf(Amt_Str).doubleValue();
				}
				catch (NumberFormatException e) {}
				if (AmouNT <= 0) {
					output("Amount must be positive");
					break;
				}
				CoNtRoL.PaY_FiNe(AmouNT);
				break;
								
			case CANCELLED:
				output("Pay Fine process cancelled");
				return;
			
			case COMPLETED:
				output("Pay Fine process complete");
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
