public class PayFineControl {
	
	private PayFineUI ui;
	private enum CONTROL_STATE { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
	private CONTROL_STATE paymentControlState;
	
	private library library;
	private member member;


	public PayFineControl() {
		this.library = library.INSTANCE();
		paymentControlState = CONTROL_STATE.INITIALISED;
	}
	
	
	public void Set_UI(PayFineUI newUi) {
		if (!paymentControlState.equals(CONTROL_STATE.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}	
		this.ui = newUi;
		newUi.Set_State(PayFineUI.UI_STATE.READY);
		paymentControlState = CONTROL_STATE.READY;		
	}


	public void Card_Swiped(int memberId) {
		if (!paymentControlState.equals(CONTROL_STATE.READY)) {
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
		}	
		member = library.MEMBER(memberId);
		
		if (member == null) {
			ui.DiSplAY("Invalid Member Id");
			return;
		}
		ui.DiSplAY(member.toString());
		ui.Set_State(PayFineUI.UI_STATE.PAYING);
		paymentControlState = CONTROL_STATE.PAYING;
	}
	
	
	public void CaNcEl() {
		ui.Set_State(PayFineUI.UI_STATE.CANCELLED);
		paymentControlState = CONTROL_STATE.CANCELLED;
	}


	public double PaY_FiNe(double fineAmount) {
		if (!paymentControlState.equals(CONTROL_STATE.PAYING)) {
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
		}	
		double change = member.Pay_Fine(fineAmount);
		if (change > 0) {
			ui.DiSplAY(String.format("Change: $%.2f", change));
		}
		ui.DiSplAY(member.toString());
		ui.Set_State(PayFineUI.UI_STATE.COMPLETED);
		paymentControlState = CONTROL_STATE.COMPLETED;
		return change;
	}
	


}
