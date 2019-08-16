import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {
	
	public static enum LoanState { CURRENT, OVERDUE, DISCHARGED };
	
	private int ID;
	private Book B;
	private Member M;
	private Date D;
	private LoanState state;

	
	public Loan(int loanId, Book book, Member member, Date dueDate) {
		this.ID = loanId;
		this.B = book;
		this.M = member;
		this.D = dueDate;
		this.state = LoanState.CURRENT;
	}

	
	public void CheckOverdue() {
		if (state == LoanState.CURRENT &&
			Calendar.instance().date().after(D)) {
			this.state = LoanState.OVERDUE;			
		}
	}

	
	public boolean Overdue() {
		return state == LoanState.OVERDUE;
	}

	
	public Integer Id() {
		return ID;
	}


	public Date GetDueDate() {
		return D;
	}
	
	
	public String String() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		StringBuilder sb = new StringBuilder();
		sb.append("Loan:  ").append(ID).append("\n")
		  .append("  Borrower ").append(M.getId()).append(" : ")
		  .append(M.getLastName()).append(", ").append(M.getFirstName()).append("\n")
		  .append("  Book ").append(B.Id()).append(" : " )
		  .append(B.title()).append("\n")
		  .append("  DueDate: ").append(sdf.format(D)).append("\n")
		  .append("  State: ").append(state);		
		return sb.toString();
	}


	public member Member() {
		return M;
	}


	public book Book() {
		return B;
	}


	public void Discharge() {
		state = LoanState.DISCHARGED;		
	}

}
