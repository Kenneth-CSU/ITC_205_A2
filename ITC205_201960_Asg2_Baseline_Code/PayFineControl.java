public class PayFineControl {
	private PayFineUi ui;
	private enum ControlState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
	private ControlState paymentControlState;
	private Library library;
	private Member member;

	public PayFineControl() {
		this.library = Library.instance();
		paymentControlState = ControlState.INITIALISED;
	}
	
	public void setUi(PayFineUi newUi) {
		if (!paymentControlState.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}	
		this.ui = newUi;
		newUi.setState(PayFineUi.UiState.READY);
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
		ui.setState(PayFineUi.UiState.PAYING);
		paymentControlState = ControlState.PAYING;
	}
	
	public void cancel() {
		ui.setState(PayFineUi.UiState.CANCELLED);
		paymentControlState = ControlState.CANCELLED;
	}

	public double payFine(double fineAmount) {
		if (!paymentControlState.equals(ControlState.PAYING)) {
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
		}	
		double change = member.payFine(fineAmount);
		if (change > 0) {
			ui.display(String.format("Change: $%.2f", change));
		}
		ui.display(member.toString());
		ui.setState(PayFineUi.UiState.COMPLETED);
		paymentControlState = ControlState.COMPLETED;
		return change;
	}
}