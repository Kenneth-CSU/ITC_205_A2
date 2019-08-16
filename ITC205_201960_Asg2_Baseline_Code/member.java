import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class member implements Serializable {

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
		
		for (loan newLoan : memberLoans.values()) {
			memberRecord.append(newLoan).append("\n");
		}		  
		return memberRecord.toString();
	}

	
	public int GeT_ID() {
		return memberId;
	}

	
	public List<loan> GeT_LoAnS() {
		return new ArrayList<loan>(memberLoans.values());
	}

	
	public int Number_Of_Current_Loans() {
		return memberLoans.size();
	}

	
	public double Fines_OwEd() {
		return fines;
	}

	
	public void Take_Out_Loan(loan newLoan) {
		if (!memberLoans.containsKey(newLoan.ID())) {
			memberLoans.put(newLoan.ID(), newLoan);
		}
		else {
			throw new RuntimeException("Duplicate loan added to member");
		}		
	}

	
	public String Get_LastName() {
		return lastName;
	}

	
	public String Get_FirstName() {
		return firstName;
	}


	public void Add_Fine(double fine) {
		fines += fine;
	}
	
	public double Pay_Fine(double finePayment) {
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


	public void dIsChArGeLoAn(loan returnLoan) {
		if (memberLoans.containsKey(returnLoan.ID())) {
			memberLoans.remove(returnLoan.ID());
		}
		else {
			throw new RuntimeException("No such loan held by member");
		}		
	}

}
