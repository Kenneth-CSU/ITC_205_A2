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
        Boolean controlState = paymentControlState.equals(ControlState.INITIALISED);
        if (!controlState) {
            throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
        }	
        this.ui = newUi;
        newUi.setState(PayFineUi.UiState.READY);
        paymentControlState = ControlState.READY;
    }

    public void cardSwiped(int memberId) {
        Boolean controlState = paymentControlState.equals(ControlState.READY);
        if (!controlState) {
            throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
        }	
        member = library.member(memberId);
        if (member == null) {
            ui.display("Invalid Member Id");
            return;
        }
        String memberString = member.toString();
        ui.display(memberString);
        ui.setState(PayFineUi.UiState.PAYING);
        paymentControlState = ControlState.PAYING;
    }

    public void cancel() {
        ui.setState(PayFineUi.UiState.CANCELLED);
        paymentControlState = ControlState.CANCELLED;
    }

    public double payFine(double fineAmount) {
        Boolean controlState = paymentControlState.equals(ControlState.PAYING);
        if (!controlState) {
            throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
        }	
        double change = member.payFine(fineAmount);
        if (change > 0) {
            String changeString = String.format("Change: $%.2f", change);
            ui.display(changeString);
        }
        String memberString = member.toString();
        ui.display(memberString);
        ui.setState(PayFineUi.UiState.COMPLETED);
        paymentControlState = ControlState.COMPLETED;
        return change;
    }
}