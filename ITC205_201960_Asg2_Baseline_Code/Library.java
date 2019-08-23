
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
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

    public static synchronized Library instance() {
        if (self == null) {
            Path path = Paths.get(LIBRARY_FILE);
            if (Files.exists(path)) {
                try (FileInputStream newFileInputStream = new FileInputStream(LIBRARY_FILE);
                    ObjectInputStream libraryInputFile = new ObjectInputStream(newFileInputStream);) {
                    self = (Library) libraryInputFile.readObject();
                    Calendar.instance().setDate(self.loanDate);
                    libraryInputFile.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                self = new Library();
            }
        }
        return self;
    }

    public static synchronized void save() {
        if (self != null) {
            self.loanDate = Calendar.instance().date();
            try (FileOutputStream newFileOutputStream = new FileOutputStream(LIBRARY_FILE);
                ObjectOutputStream libraryOutputFile = new ObjectOutputStream(newFileOutputStream);) {
                libraryOutputFile.writeObject(self);
                libraryOutputFile.flush();
                libraryOutputFile.close();
            } catch (Exception e) {
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
        Collection<Member> membersList = members.values();
        return new ArrayList<Member>(membersList);
    }

    public List<Book> books() {
        Collection<Book> catalogList = catalog.values();
        return new ArrayList<Book>(catalogList);
    }

    public List<Loan> currentLoans() {
        Collection<Loan> currentLoansList = currentLoans.values();
        return new ArrayList<Loan>(currentLoansList);
    }

    public Member addMember(String lastName, String firstName, String email, int phoneNo) {
        int nextMemberId = nextMemberId();
        Member member = new Member(lastName, firstName, email, phoneNo, nextMemberId);
        int newMemberId = member.getMemberId();
        members.put(newMemberId, member);
        return member;
    }

    public Book addBook(String author, String title, String callNumber) {
        int newBookId = nextBookId();
        Book newBook = new Book(author, title, callNumber, newBookId);
        catalog.put(newBookId, newBook);
        return newBook;
    }

    public Member member(int memberId) {
        if (members.containsKey(memberId)) {
            return members.get(memberId);
        }
        return null;
    }

    public Book book(int bookId) {
        if (catalog.containsKey(bookId)) {
            return catalog.get(bookId);
        }
        return null;
    }

    public int loanLimit() {
        return LOAN_LIMIT;
    }

    public boolean memberCanBorrow(Member member) {
        if (member.numberOfCurrentLoans() == LOAN_LIMIT) {
            return false;
        }
        if (member.finesOwed() >= MAX_FINES_OWED) {
            return false;
        }
        for (Loan loan : member.getLoans()) {
            Boolean isOverdue = loan.isOverdue();
            if (isOverdue) {
                return false;
            }
        }
        return true;
    }

    public int loansRemainingForMember(Member member) {
        return LOAN_LIMIT - member.numberOfCurrentLoans();
    }

    public Loan issueLoan(Book book, Member member) {
        Date dueDate = Calendar.instance().dueDate(LOAN_PERIOD);
        int newLoanId = nextLoanId();
        Loan loan = new Loan(newLoanId, book, member, dueDate);
        member.takeOutLoan(loan);
        book.borrow();
        int issuedLoanId = loan.loanId();
        loans.put(issuedLoanId, loan);
        int borrowedBookId = book.id();
        currentLoans.put(borrowedBookId, loan);
        return loan;
    }

    public Loan loanByBookId(int bookId) {
        if (currentLoans.containsKey(bookId)) {
            return currentLoans.get(bookId);
        }
        return null;
    }

    public double calculateOverdueFine(Loan loan) {
        Boolean isOverdue = loan.isOverdue();
        if (isOverdue) {
            Date dateNow = loan.getDueDate();
            long daysOverDue = Calendar.instance().getDaysDifference(dateNow);
            double fine = daysOverDue * FINE_PER_DAY;
            return fine;
        }
        return 0.0;
    }

    public void dischargeLoan(Loan currentLoan, boolean isDamaged) {
        Member member = currentLoan.member();
        Book book = currentLoan.book();
        double overDueFine = calculateOverdueFine(currentLoan);
        member.addFine(overDueFine);
        member.dischargeLoan(currentLoan);
        book.returnBook(isDamaged);
        if (isDamaged) {
            member.addFine(DAMAGE_FEE);
            int damigedBookId = book.id();
            damagedBooks.put(damigedBookId, book);
        }
        currentLoan.discharge();
        int bookId = book.id();
        currentLoans.remove(bookId);
    }

    public void checkCurrentLoans() {
        Collection<Loan> loansList = currentLoans.values();
        for (Loan loan : loansList) {
            loan.checkOverDue();
        }
    }

    public void repairBook(Book currentBook) {
        int repairdBookId = currentBook.id();
        if (damagedBooks.containsKey(repairdBookId)) {
            currentBook.repair();
            damagedBooks.remove(repairdBookId);
        } else {
            throw new RuntimeException("Library: repairBook: book is not damaged");
        }
    }
}
