public class PayFineControl {
	
	private PayFineUI Ui;
	private enum ControlState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
	private ControlState StAtE;
	
	private Library LiBrArY;
	private Member MeMbEr;


	public PayFineControl() {
		this.LiBrArY = LiBrArY.instance();
		StAtE = ControlState.INITIALISED;
	}
	
	
	public void SetUi(PayFineUI ui) {
		if (!StAtE.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}	
		this.Ui = ui;
		ui.setState(PayFineUI.uiState.READY);
		StAtE = ControlState.READY;		
	}


	public void CardSwiped(int memberId) {
		if (!StAtE.equals(ControlState.READY)) {
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
		}	
		MeMbEr = LiBrArY.member(memberId);
		
		if (MeMbEr == null) {
			Ui.display("Invalid Member Id");
			return;
		}
		Ui.display(MeMbEr.toString());
		Ui.setState(PayFineUI.uiState.PAYING);
		StAtE = ControlState.PAYING;
	}
	
	
	public void Cancel() {
		Ui.setState(PayFineUI.uiState.CANCELLED);
		StAtE = ControlState.CANCELLED;
	}


	public double PayFine(double AmOuNt) {
		if (!StAtE.equals(ControlState.PAYING)) {
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
		}	
		double ChAnGe = MeMbEr.payFine(AmOuNt);
		if (ChAnGe > 0) {
			Ui.display(String.format("Change: $%.2f", ChAnGe));
		}
		Ui.display(MeMbEr.toString());
		Ui.setState(PayFineUI.uiState.COMPLETED);
		StAtE = ControlState.COMPLETED;
		return ChAnGe;
	}
	


}
