import java.util.Scanner;

public class PayFineUi {
    public static enum UiState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
    private PayFineControl paymentControl;	
    private Scanner inputScanner;
    private UiState uiState;

    public PayFineUi(PayFineControl newPayment) {
        this.paymentControl = newPayment;
        inputScanner = new Scanner(System.in);
        uiState = UiState.INITIALISED;
        newPayment.setUi(this);
    }

    public void setState(UiState state) {
        this.uiState = state;
    }

    public void run() {
        output("Pay Fine Use Case UI\n");
        while (true) {
            switch (uiState) {
            case READY:
                String memberCardRead = input("Swipe member card (press <enter> to cancel): ");
                int cardNumberLength = memberCardRead.length();
                if (cardNumberLength == 0) {
                    paymentControl.cancel();
                    break;
                }
                try {
                    int memberId = Integer.valueOf(memberCardRead).intValue();
                    paymentControl.cardSwiped(memberId);
                }
                catch (NumberFormatException e) {
                    output("Invalid memberId");
                }
                break;

            case PAYING:
                double paymentAmount = 0;
                String newPaymentAmount = input("Enter amount (<Enter> cancels) : ");
                int paymentAmountLength = newPaymentAmount.length();
                if (paymentAmountLength == 0) {				    
                    paymentControl.cancel();
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
                paymentControl.payFine(paymentAmount);
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

    public void display(Object object) {
        output(object);
    }
}
