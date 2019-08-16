import java.util.Scanner;


public class PayFineUI {


	public static enum UI_STATE { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };

	private PayFineControl paymentControl;	
	private Scanner inputScanner;
	private UI_STATE uiState;

	
	public PayFineUI(PayFineControl newPayment) {
		this.paymentControl = newPayment;
		inputScanner = new Scanner(System.in);
		uiState = UI_STATE.INITIALISED;
		newPayment.Set_UI(this);
	}
	
	
	public void Set_State(UI_STATE state) {
		this.uiState = state;
	}


	public void RuN() {
		output("Pay Fine Use Case UI\n");
		
		while (true) {
			
			switch (uiState) {
			
			case READY:
				String memberCardRead = input("Swipe member card (press <enter> to cancel): ");
				if (memberCardRead.length() == 0) {
					paymentControl.CaNcEl();
					break;
				}
				try {
					int Member_ID = Integer.valueOf(memberCardRead).intValue();
					paymentControl.Card_Swiped(Member_ID);
				}
				catch (NumberFormatException e) {
					output("Invalid memberId");
				}
				break;
				
			case PAYING:
				double paymentAmount = 0;
				String newPaymentAmount = input("Enter amount (<Enter> cancels) : ");
				if (newPaymentAmount.length() == 0) {
					paymentControl.CaNcEl();
					break;
				}
				try {
					paymentAmount = Double.valueOf(newPaymentAmount).doubleValue();
				}
				catch (NumberFormatException e) {}
				if (paymentAmount <= 0) {
					output("Amount must be positive");
					break;
				}
				paymentControl.PaY_FiNe(paymentAmount);
				break;
								
			case CANCELLED:
				output("Pay Fine process cancelled");
				return;
			
			case COMPLETED:
				output("Pay Fine process complete");
				return;
			
			default:
				output("Unhandled state");
				throw new RuntimeException("FixBookUI : unhandled state :" + uiState);			
			
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
			

	public void DiSplAY(Object object) {
		output(object);
	}


}
