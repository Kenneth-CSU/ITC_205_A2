import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
    private Map<Integer, Loan> memberLoans;

    public Member(String lastName, String firstName, String email, int phoneNo, int memberId) {
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
        Collection<Loan> loans = memberLoans.values();
        for (Loan newLoan : loans) {
            memberRecord.append(newLoan).append("\n");
        }		  
        return memberRecord.toString();
    }

    public  int getMemberId() {
        return memberId;
    }

    public List<Loan> getLoans() {
        Collection<Loan> loans =memberLoans.values();
        return new ArrayList<Loan>(loans);
    }

    public int numberOfCurrentLoans() {
        return memberLoans.size();
    }

    public double finesOwed() {
        return fines;
    }

    public void takeOutLoan(Loan newLoan) {
        int newLoanId = newLoan.loanId();
        if (!memberLoans.containsKey(newLoanId)) {
            memberLoans.put(newLoanId, newLoan);
        } else {
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
        } else {
            fines -= finePayment;
        }
        return change;
    }

    public void dischargeLoan(Loan returnLoan) {
        int returnLoanId = returnLoan.loanId();
        if (memberLoans.containsKey(returnLoanId)) {
            memberLoans.remove(returnLoanId);
        } else {
            throw new RuntimeException("No such loan held by member");
        }		
    }
}
