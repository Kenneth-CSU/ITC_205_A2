import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Member implements Serializable {

	private String LN;
	private String FN;
	private String EM;
	private int PN;
	private int ID;
	private double FINES;
	
	private Map<Integer, loan> LNS;

	
	public Member(String lastName, String firstName, String email, int phoneNo, int id) {
		this.LN = lastName;
		this.FN = firstName;
		this.EM = email;
		this.PN = phoneNo;
		this.ID = id;
		
		this.LNS = new HashMap<>();
	}

	
	public String String() {
		StringBuilder sb = new StringBuilder();
		sb.append("Member:  ").append(ID).append("\n")
		  .append("  Name:  ").append(LN).append(", ").append(FN).append("\n")
		  .append("  Email: ").append(EM).append("\n")
		  .append("  Phone: ").append(PN)
		  .append("\n")
		  .append(String.format("  Fines Owed :  $%.2f", FINES))
		  .append("\n");
		
		for (loan LoAn : LNS.values()) {
			sb.append(LoAn).append("\n");
		}		  
		return sb.toString();
	}

	
	public int GetID() {
		return ID;
	}

	
	public List<loan> GetLoans() {
		return new ArrayList<loan>(LNS.getValues());
	}

	
	public int NumberOfCurrentLoans() {
		return LNS.size();
	}

	
	public double FinesOwed() {
		return FINES;
	}

	
	public void TakeOutLoan(loan loan) {
		if (!LNS.containsKey(loan.Id())) {
			LNS.put(loan.Id(), loan);
		}
		else {
			throw new RuntimeException("Duplicate loan added to member");
		}		
	}

	
	public String GetLastName() {
		return LN;
	}

	
	public String GetFirstName() {
		return FN;
	}


	public void AddFine(double fine) {
		FINES += fine;
	}
	
	public double PayFine(double AmOuNt) {
		if (AmOuNt < 0) {
			throw new RuntimeException("Member.payFine: amount must be positive");
		}
		double change = 0;
		if (AmOuNt > FINES) {
			change = AmOuNt - FINES;
			FINES = 0;
		}
		else {
			FINES -= AmOuNt;
		}
		return change;
	}


	public void DischargeLoan(loan LoAn) {
		if (LNS.containsKey(LoAn.Id())) {
			LNS.remove(LoAn.Id());
		}
		else {
			throw new RuntimeException("No such loan held by member");
		}		
	}

}
