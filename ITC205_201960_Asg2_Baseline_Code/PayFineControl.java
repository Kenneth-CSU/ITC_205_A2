public class PayFineControl {
	private PayFineUI ui;
	private enum ControlState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
	private ControlState paymentControlState;
	private Library library;
	private Member member;

	public PayFineControl() {
		this.library = Library.instance();
		paymentControlState = ControlState.INITIALISED;
	}
	
	public void setUi(PayFineUI newUi) {
		if (!paymentControlState.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}	
		this.ui = newUi;
		newUi.setState(PayFineUI.uiState.READY);
		paymentControlState = ControlState.READY;
	}

	public void cardSwiped(int memberId) {
		if (!paymentControlState.equals(ControlState.READY)) {
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
		}	
		member = library.member(memberId);
		if (member == null) {
			ui.display("Invalid Member Id");
			return;
		}
		ui.display(member.toString());
		ui.setState(PayFineUI.uiState.PAYING);
		paymentControlState = ControlState.PAYING;
	}
	
	public void cancel() {
		ui.setState(PayFineUI.uiState.CANCELLED);
		paymentControlState = ControlState.CANCELLED;
	}

	public double payFine(double fineAmount) {
		if (!paymentControlState.equals(ControlState.PAYING)) {
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
		}	
		double change = Member.payFine(fineAmount);
		if (change > 0) {
			ui.display(String.format("Change: $%.2f", change));
		}
		ui.display(Member.toString());
		ui.setState(PayFineUI.uiState.COMPLETED);
		paymentControlState = ControlState.COMPLETED;
		return change;
	}
}