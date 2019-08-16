import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class loan implements Serializable {
	
	public static enum LOAN_STATE { CURRENT, OVER_DUE, DISCHARGED };
	
	private int loanId;
	private book book;
	private member member;
	private Date dueDate;
	private LOAN_STATE loanState;

	
	public loan(int loanId, book book, member member, Date dueDate) {
		this.loanId = loanId;
		this.book = book;
		this.member = member;
		this.dueDate = dueDate;
		this.loanState = LOAN_STATE.CURRENT;
	}

	
	public void checkOverDue() {
		if (loanState == LOAN_STATE.CURRENT &&
			Calendar.INSTANCE().Date().after(dueDate)) {
			this.loanState = LOAN_STATE.OVER_DUE;			
		}
	}

	
	public boolean OVer_Due() {
		return loanState == LOAN_STATE.OVER_DUE;
	}

	
	public Integer ID() {
		return loanId;
	}


	public Date Get_Due_Date() {
		return dueDate;
	}
	
	
	public String toString() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		StringBuilder menuEntries = new StringBuilder();
		menuEntries.append("Loan:  ").append(loanId).append("\n")
		  .append("  Borrower ").append(member.GeT_ID()).append(" : ")
		  .append(member.Get_LastName()).append(", ").append(member.Get_FirstName()).append("\n")
		  .append("  Book ").append(book.ID()).append(" : " )
		  .append(book.TITLE()).append("\n")
		  .append("  DueDate: ").append(simpleDateFormat.format(dueDate)).append("\n")
		  .append("  State: ").append(loanState);		
		return menuEntries.toString();
	}


	public member Member() {
		return member;
	}


	public book Book() {
		return book;
	}


	public void DiScHaRgE() {
		loanState = LOAN_STATE.DISCHARGED;		
	}

}
