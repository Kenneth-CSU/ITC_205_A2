
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Library implements Serializable {
	
	private static final String libraryFile = "library.obj";
	private static final int loanLimit = 2;
	private static final int loanPeriod = 2;
	private static final double finePerDay = 1.0;
	private static final double maxFinesOwed = 1.0;
	private static final double damageFee = 2.0;
	
	private static Library SeLf;
	private int BOOK_ID;
	private int MEMBER_ID;
	private int LOAN_ID;
	private Date LOAN_DATE;
	
	private Map<Integer, book> CATALOG;
	private Map<Integer, member> MEMBERS;
	private Map<Integer, loan> LOANS;
	private Map<Integer, loan> CURRENT_LOANS;
	private Map<Integer, book> DAMAGED_BOOKS;
	

	private Library() {
		CATALOG = new HashMap<>();
		MEMBERS = new HashMap<>();
		LOANS = new HashMap<>();
		CURRENT_LOANS = new HashMap<>();
		DAMAGED_BOOKS = new HashMap<>();
		BOOK_ID = 1;
		MEMBER_ID = 1;		
		LOAN_ID = 1;		
	}

	
	public static synchronized Library Instance() {		
		if (SeLf == null) {
			Path PATH = Paths.get(libraryFile);			
			if (Files.exists(PATH)) {	
				try (ObjectInputStream LiF = new ObjectInputStream(new FileInputStream(libraryFile));) {
			    
					SeLf = (library) LiF.readObject();
					Calendar.instance().setDate(SeLf.LOAN_DATE);
					LiF.close();
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			else SeLf = new library();
		}
		return SeLf;
	}

	
	public static synchronized void Save() {
		if (SeLf != null) {
			SeLf.LOAN_DATE = Calendar.instance().date();
			try (ObjectOutputStream LoF = new ObjectOutputStream(new FileOutputStream(libraryFile));) {
				LoF.writeObject(SeLf);
				LoF.flush();
				LoF.close();	
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	
	public int BookId() {
		return BOOK_ID;
	}
	
	
	public int MemberId() {
		return MEMBER_ID;
	}
	
	
	private int NextBId() {
		return BOOK_ID++;
	}

	
	private int NextMId() {
		return MEMBER_ID++;
	}

	
	private int NextLId() {
		return LOAN_ID++;
	}

	
	public List<member> Members() {		
		return new ArrayList<member>(MEMBERS.values()); 
	}


	public List<book> Books() {		
		return new ArrayList<book>(CATALOG.values()); 
	}


	public List<loan> CurrentLoans() {
		return new ArrayList<loan>(CURRENT_LOANS.values());
	}


	public member AddMem(String lastName, String firstName, String email, int phoneNo) {		
		member member = new member(lastName, firstName, email, phoneNo, NextMId());
		MEMBERS.put(member.getId(), member);		
		return member;
	}

	
	public book AddBook(String a, String t, String c) {		
		book b = new book(a, t, c, NextBId());
		CATALOG.put(b.id(), b);		
		return b;
	}

	
	public member Member(int memberId) {
		if (MEMBERS.containsKey(memberId)) 
			return MEMBERS.get(memberId);
		return null;
	}

	
	public book Book(int bookId) {
		if (CATALOG.containsKey(bookId)) 
			return CATALOG.get(bookId);		
		return null;
	}

	
	public int LoanLimit() {
		return loanLimit;
	}

	
	public boolean MemberCanBorrow(member member) {		
		if (member.numberOfCurrentLoans() == loanLimit ) 
			return false;
				
		if (member.finesOwed() >= maxFinesOwed) 
			return false;
				
		for (loan loan : member.getLoans()) 
			if (loan.isOverdue()) 
				return false;
			
		return true;
	}

	
	public int LoansRemainingForMember(member member) {		
		return loanLimit - member.numberOfCurrentLoans();
	}

	
	public loan IssueLoan(book book, member member) {
		Date dueDate = Calendar.instance().dueDate(loanPeriod);
		loan loan = new loan(NextLId(), book, member, dueDate);
		member.takeOutLoan(loan);
		book.borrow();
		LOANS.put(loan.Id(), loan);
		CURRENT_LOANS.put(book.Id(), loan);
		return loan;
	}
	
	
	public loan LoanByBookId(int bookId) {
		if (CURRENT_LOANS.containsKey(bookId)) {
			return CURRENT_LOANS.get(bookId);
		}
		return null;
	}

	
	public double CalculateOverdueFine(loan loan) {
		if (loan.OVer_Due()) {
			long daysOverDue = Calendar.INSTANCE().Get_Days_Difference(loan.Get_Due_Date());
			double fine = daysOverDue * finePerDay;
			return fine;
		}
		return 0.0;		
	}


	public void DischargeLoan(loan currentLoan, boolean isDamaged) {
		member member = currentLoan.member();
		book book  = currentLoan.book();
		
		double overDueFine = CalculateOverDueFine(currentLoan);
		member.addFine(overDueFine);	
		
		member.dischargeLoan(currentLoan);
		book.return(isDamaged);
		if (isDamaged) {
			member.addFine(damageFee);
			DAMAGED_BOOKS.put(book.Id(), book);
		}
		currentLoan.discharge();
		CURRENT_LOANS.remove(book.Id());
	}


	public void CheckCurrentLoans() {
		for (loan loan : CURRENT_LOANS.values()) {
			loan.checkOverdue();
		}		
	}


	public void RepairBook(book currentBook) {
		if (DAMAGED_BOOKS.containsKey(currentBook.Id())) {
			currentBook.repair();
			DAMAGED_BOOKS.remove(currentBook.Id());
		}
		else {
			throw new RuntimeException("Library: repairBook: book is not damaged");
		}
		
	}
	
	
}
