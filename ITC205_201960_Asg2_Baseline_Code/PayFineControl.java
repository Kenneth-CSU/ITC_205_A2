public class PayFineControl {
	
	private PayFineUI Ui;
	private enum ControlState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
	private ControlState StAtE;
	
	private Library LiBrArY;
	private Member MeMbEr;


	public PayFineControl() {
		this.LiBrArY = LiBrArY.INSTANCE();
		StAtE = ControlState.INITIALISED;
	}
	
	
	public void SetUI(PayFineUI ui) {
		if (!StAtE.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}	
		this.Ui = ui;
		ui.Set_State(PayFineUI.UI_STATE.READY);
		StAtE = ControlState.READY;		
	}


	public void CardSwiped(int memberId) {
		if (!StAtE.equals(ControlState.READY)) {
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
		}	
		MeMbEr = LiBrArY.MEMBER(memberId);
		
		if (MeMbEr == null) {
			Ui.DiSplAY("Invalid Member Id");
			return;
		}
		Ui.DiSplAY(MeMbEr.toString());
		Ui.Set_State(PayFineUI.UI_STATE.PAYING);
		StAtE = ControlState.PAYING;
	}
	
	
	public void Cancel() {
		Ui.Set_State(PayFineUI.UI_STATE.CANCELLED);
		StAtE = ControlState.CANCELLED;
	}


	public double PayFine(double AmOuNt) {
		if (!StAtE.equals(ControlState.PAYING)) {
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
		}	
		double ChAnGe = MeMbEr.payFine(AmOuNt);
		if (ChAnGe > 0) {
			Ui.DiSplAY(String.format("Change: $%.2f", ChAnGe));
		}
		Ui.DiSplAY(MeMbEr.toString());
		Ui.Set_State(PayFineUI.UISTATE.COMPLETED);
		StAtE = ControlState.COMPLETED;
		return ChAnGe;
	}
	


}
