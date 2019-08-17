import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Member implements Serializable {

	private String lastName;
	private String firstName;
	private String email;
	private int phoneNo;
	private int memberId;
	private double fines;
	private Map<Integer, loan> memberLoans;

	public member(String lastName, String firstName, String email, int phoneNo, int memberId) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.phoneNo = phoneNo;
		this.memberId = memberId;
		this.memberLoans = new HashMap<>();
	}

	public String toString() {
		StringBuilder memberRecord = new StringBuilder();
		memberRecord.append("Member:  ").append(memberId).append("\n")
		  .append("  Name:  ").append(lastName).append(", ").append(firstName).append("\n")
		  .append("  Email: ").append(email).append("\n")
		  .append("  Phone: ").append(phoneNo)
		  .append("\n")
		  .append(String.format("  Fines Owed :  $%.2f", fines))
		  .append("\n");
		for (Loan newLoan : memberLoans.values()) {
			memberRecord.append(newLoan).append("\n");
		}		  
		return memberRecord.toString();
	}

	public int getMemberId() {
		return memberId;
	}

	public List<Loan> getLoans() {
		return new ArrayList<Loan>(memberLoans.values());
	}
	
	public int numberOfCurrentLoans() {
		return memberLoans.size();
	}
	
	public double finesOwed() {
		return fines;
	}
	
	public void takeOutLoan(Loan newLoan) {
		if (!memberLoans.containsKey(newLoan.Id())) {
			memberLoans.put(newLoan.Id(), newLoan);
		}
		else {
			throw new RuntimeException("Duplicate loan added to member");
		}		
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void addFine(double fine) {
		fines += fine;
	}
	
	public double payFine(double finePayment) {
		if (finePayment < 0) {
			throw new RuntimeException("Member.payFine: amount must be positive");
		}
		double change = 0;
		if (finePayment > fines) {
			change = finePayment - fines;
			fines = 0;
		}
		else {
			fines -= finePayment;
		}
		return change;
	}

	public void dischargeLoan(Loan returnLoan) {
		if (memberLoans.containsKey(returnLoan.Id())) {
			memberLoans.remove(returnLoan.Id());
		}
		else {
			throw new RuntimeException("No such loan held by member");
		}		
	}
}
