
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
	
	private static final String LIBRARY_FILE = "library.obj";
	private static final int LOAN_LIMIT = 2;
	private static final int LOAN_PERIOD = 2;
	private static final double FINE_PER_DAY = 1.0;
	private static final double MAX_FINES_OWED = 1.0;
	private static final double DAMAGE_FEE = 2.0;
	
	private static Library self;
	private int bookId;
	private int memberId;
	private int loanId;
	private Date loanDate;
	
	private Map<Integer, Book> catalog;
	private Map<Integer, Member> members;
	private Map<Integer, Loan> loans;
	private Map<Integer, Loan> currentLoans;
	private Map<Integer, Book> damagedBooks;

	private Library() {
		catalog = new HashMap<>();
		members = new HashMap<>();
		loans = new HashMap<>();
		currentLoans = new HashMap<>();
		damagedBooks = new HashMap<>();
		bookId = 1;
		memberId = 1;		
		loanId = 1;		
	}
	
	public static synchronized Library openLibrary() {		
		if (self == null) {
			Path path = Paths.get(LIBRARY_FILE);			
			if (Files.exists(path)) {	
				try (ObjectInputStream libraryInputFile = new ObjectInputStream(new FileInputStream(LIBRARY_FILE));) {
					self = (library) libraryInputFile.readObject();
					Calendar.openLibrary().Set_dATE(self.loanDate);
					libraryInputFile.close();
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			else self = new library();
		}
		return self;
	}

	public static synchronized void save() {
		if (self != null) {
			self.loanDate = Calendar.instance().date();
			try (ObjectOutputStream libraryOutputFile = new ObjectOutputStream(new FileOutputStream(LIBRARY_FILE));) {
				libraryOutputFile.writeObject(self);
				libraryOutputFile.flush();
				libraryOutputFile.close();	
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public int bookId() {
		return bookId;
	}
	
	public int memberId() {
		return memberId;
	}
	
	private int nextBookId() {
		return bookId++;
	}
	
	private int nextMemberId() {
		return memberId++;
	}
	
	private int nextLoanId() {
		return loanId++;
	}
	
	public List<Member> members() {		
		return new ArrayList<Member>(members.values()); 
	}

	public List<Book> books() {		
		return new ArrayList<Book>(catalog.values()); 
	}

	public List<loan> CurrentLoans() {
		return new ArrayList<loan>(currentLoans.values());
	}

	public Member addMember(String lastName, String firstName, String email, int phoneNo) {		
		Member member = new Member(lastName, firstName, email, phoneNo, nextMemberId());
		members.put(Member.getId(), member);		
		return member;
	}

	public Book addBook(String author , String title , String callNumber) {		
		Book newBook = new book(author , title , callNumber, nextBookId());
		catalog.put(newBook.Id(), newBook);		
		return newBook;
	}

	public Member member(int memberId) {
		if (members.containsKey(memberId)) 
			return members.get(memberId);
		return null;
	}
	
	public book Book(int bookId) {
		if (catalog.containsKey(bookId)) 
			return catalog.get(bookId);		
		return null;
	}

	public int loanLimit() {
		return loanLimit;
	}

	public boolean memberCanBorrow(Member member) {		
		if (member.numberOfCurrentLoans() == LOAN_LIMIT ) 
			return false;
		if (member.finesOwed() >= MAX_FINES_OWED) 
			return false;
		for (loan loan : member.getLoans()) 
			if (loan.isOverdue()) 
				return false;
		return true;
	}

	public int loans_Remaining_For_Member(Member member) {		
		return LOAN_LIMIT - member.numberOfCurrentLoans();
	}

	public loan issueLoan(Book book, Member member) {
		Date dueDate = Calendar.INSTANCE().dueDate(LOAN_PERIOD);
		Loan loan = new Loan(nextLibraryId(), book, member, dueDate);
		Member.takeOutLoan(loan);
		Book.Borrow();
		loans.put(Loan.Id(), loan);
		currentLoans.put(Book.Id(), loan);
		return loan;
	}
	
	public Loan loanByBookId(int bookId) {
		if (currentLoans.containsKey(bookId)) {
			return currentLoans.get(bookId);
		}
		return null;
	}
	
	public double CalculateOverdueFine(loan loan) {
		if (loan.OVer_Due()) {
			long daysOverDue = Calendar.INSTANCE().Get_Days_Difference(loan.Get_Due_Date());
			double fine = daysOverDue * FINE_PER_DAY;
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
			member.addFine(DAMAGE_FEE);
			damagedBooks.put(book.Id(), book);//book.Id or Book.Id
		}
		currentLoan.discharge();
		currentLoans.remove(book.Id());
	}

	public void checkCurrentLoans() {
		for (Loan loan : currentLoans.values()) {
			loan.checkOverDue();
		}		
	}

	public void repairBook(book currentBook) {
		if (damagedBooks.containsKey(currentBook.Id())) {
			currentBook.repair();
			damagedBooks.remove(currentBook.Id());
		}
		else {
			throw new RuntimeException("Library: repairBook: book is not damaged");
		}
	}
}
