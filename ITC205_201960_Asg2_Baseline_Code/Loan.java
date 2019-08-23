import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {

    public static enum LoanState { CURRENT, OVERDUE, DISCHARGED };
    private int loanId;
    private Book book;
    private Member member;
    private Date dueDate;
    private LoanState loanState;

    public Loan(int loanId, Book book, Member member, Date dueDate) {
        this.loanId = loanId;
        this.book = book;
        this.member = member;
        this.dueDate = dueDate;
        this.loanState = LoanState.CURRENT;
    }

    public void checkOverDue() {
        if (loanState == LoanState.CURRENT && Calendar.instance().date().after(dueDate)) {
            this.loanState = LoanState.OVERDUE;			
        }
    }

    public boolean isOverdue() {
        return loanState == LoanState.OVERDUE;
    }

    public int loanId() {
        return loanId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder menuEntries = new StringBuilder();
        menuEntries.append("Loan:  ").append(loanId).append("\n")
        .append("  Borrower ").append(member.getMemberId()).append(" : ")
        .append(member.getLastName()).append(", ").append(member.getFirstName()).append("\n")
        .append("  Book ").append(book.id()).append(" : " )
        .append(book.title()).append("\n")
        .append("  DueDate: ").append(simpleDateFormat.format(dueDate)).append("\n")
        .append("  State: ").append(loanState);		
        return menuEntries.toString();
    }

    public Member member() {
        return member;
    }

    public Book book() {
        return book;
    }

    public void discharge() {
        loanState = LoanState.DISCHARGED;		
    }
}
