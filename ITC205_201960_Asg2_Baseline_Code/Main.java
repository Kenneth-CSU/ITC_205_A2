import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner inputScanner;
    private static Library library;
    private static String menuEntries;
    private static Calendar calendar;
  //  private static Book activeBook;
    private static SimpleDateFormat simpleDateFormat;

    private static String getMenu() {
        StringBuilder menuString = new StringBuilder();
        menuString.append("\nLibrary Main Menu\n\n")
        .append("  M  : add member\n")
        .append("  LM : list members\n")
        .append("\n")
        .append("  B  : add book\n")
        .append("  LB : list books\n")
        .append("  FB : fix books\n")
        .append("\n")
        .append("  L  : take out a loan\n")
        .append("  R  : return a loan\n")
        .append("  LL : list loans\n")
        .append("\n")
        .append("  P  : pay fine\n")
        .append("\n")
        .append("  T  : increment date\n")
        .append("  Q  : quit\n")
        .append("\n")
        .append("Choice : ");
        return menuString.toString();
    }

    public static void main(String[] args) {		
        try {			
            inputScanner = new Scanner(System.in);
            library = Library.getInstance();
            calendar = Calendar.getInstance();
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            List<Member> members = library.members();
            for (Member member : members) {
                output(member);
            }
            output(" ");
            for (Book book : library.books()) {
                output(book);
            }
            menuEntries = getMenu();
            boolean loopState = false;
            while (!loopState) {
                Date date = calendar.date();
                String dateString = simpleDateFormat.format(date);
                output("\n" + dateString);
                String menuInput = input(menuEntries);
                switch (menuInput.toUpperCase()) {
                case "M": 
                    addMember();
                    break;

                case "LM": 
                    members();
                    break;

                case "B": 
                    addBook();
                    break;

                case "LB": 
                    books();
                    break;

                case "FB": 
                    fixBooks();
                    break;

                case "L": 
                    borrowBook();
                    break;

                case "R": 
                    returnBook();
                    break;

                case "LL": 
                    currentLoans();
                    break;

                case "P": 
                    fines();
                    break;

                case "T": 
                    incrementDate();
                    break;

                case "Q": 
                    loopState = true;
                    break;

                default: 
                    output("\nInvalid option\n");
                    break;
                }
                Library.save();
            }			
        } catch (RuntimeException errorType) {
            output(errorType);
        }		
        output("\nEnded\n");
    }	

    private static void fines() {
        PayFineControl payFine = new PayFineControl();
        new PayFineUi(payFine).run();		
    }

    private static void currentLoans() {
        output("");
        List<Loan> currentLoans = library.currentLoans();
        for (Loan loan : currentLoans) {
            output(loan + "\n");
        }		
    }

    private static void books() {
        output("");
        List<Book> books = library.books();
        for (Book book : books) {
            output(book + "\n");
        }		
    }

    private static void members() {
        output("");
        List<Member> members = library.members();
        for (Member member : members) {
            output(member + "\n");
        }		
    }

    private static void borrowBook() {
        BorrowBookControl borrowBookControl = new BorrowBookControl();
        new BorrowBookUi(borrowBookControl).run();		
    }

    private static void returnBook() {
        ReturnBookControl returnBookControl = new ReturnBookControl();
        new ReturnBookUi(returnBookControl).run();		
    }

    private static void fixBooks() {
        FixBookControl fixBookControl = new FixBookControl();
        new FixBookUi(fixBookControl).run();		
    }

    private static void incrementDate() {
        try {
            String inputDays = input("Enter number of days: ");
            int days = Integer.valueOf(inputDays).intValue();
            calendar.incrementDate(days);
            library.checkCurrentLoans();
            Date date = calendar.date();
            String dateString = simpleDateFormat.format(date);
            output(dateString);
        } catch (NumberFormatException e) {
            output("\nInvalid number of days\n");
        }
    }

    private static void addBook() {
        String author = input("Enter author: ");
        String title  = input("Enter title: ");
        String callNumber = input("Enter call number: ");
        Book book = library.addBook(author, title, callNumber);
        output("\n" + book + "\n");

    }

    private static void addMember() {
        try {
            String nameLast = input("Enter last name: ");
            String nameFirst  = input("Enter first name: ");
            String emailAdress = input("Enter email: ");
            String number = input("Enter phone number: ");
            int phoneNumber = Integer.valueOf(number).intValue();
            Member member = library.addMember(nameLast, nameFirst, emailAdress, phoneNumber);
            output("\n" + member + "\n");
        } catch (NumberFormatException e) {
            output("\nInvalid phone number\n");
        }
    }

    private static String input(String prompt) {
        System.out.print(prompt);
        return inputScanner.nextLine();
    }

    private static void output(Object object) {
        System.out.println(object);
    }
}
