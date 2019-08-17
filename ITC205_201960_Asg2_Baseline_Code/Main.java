import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {
	private static Scanner inputScanner;
	private static Library library;
	private static String menuEntries;
	private static Calendar calendar;
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
			library = Library.instance();
			calendar = Calendar.instance();
			simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			for (Member member : library.instance()) {
				output(member);
			}
			output(" ");
			for (Book book : library.books()) {
				output(book);
			}
			menuEntries = getMenu();
			boolean loopState = false;
			while (!loopState) {
				output("\n" + simpleDateFormat.format(calendar.date()));
				String menuInput = input(menuEntries);
				switch (menuInput.toUpperCase()) {
				case "M": 
					AddMember();
					break;
					
				case "LM": 
					Members();
					break;
					
				case "B": 
					AddBook();
					break;
					
				case "LB": 
					Books();
					break;
					
				case "FB": 
					FixBooks();
					break;
					
				case "L": 
					BorrowBook();
					break;
					
				case "R": 
					ReturnBook();
					break;
					
				case "LL": 
					CurrentLoans();
					break;
					
				case "P": 
					Fines();
					break;
					
				case "T": 
					IncrementDate();
					break;
					
				case "Q": 
					loopState = true;
					break;
					
				default: 
					Output("\nInvalid option\n");
					break;
				}
				Library.save();
			}			
		} catch (RuntimeException errorType) {
			output(errorType);
		}		
		output("\nEnded\n");
	}	
	
	private static void Fines() {
		new PayFineUI(new PayFineControl()).Run();		
	}

	private static void currentLoans() {
		output("");
		for (Loan loan : library.currentLoans()) {
			output(loan + "\n");
		}		
	}

	private static void Books() {
		output("");
		for (Book book : library.books()) {
			output(book + "\n");
		}		
	}

	private static void instance() {
		output("");
		for (Member member : library.instance()) {
			output(member + "\n");
		}		
	}

	private static void borrowBook() {
		new BorrowBookUI(new BorrowBookControl()).run();		
	}

	private static void returnBook() {
		new ReturnBookUI(new ReturnBookControl()).run();		
	}

	private static void fixBooks() {
		new FixBookUI(new FixBookControl()).run();		
	}

	private static void incrementDate() {
		try {
			int days = Integer.valueOf(input("Enter number of days: ")).intValue();
			calendar.incrementDate(days);
			library.checkCurrentLoans();
			output(simpleDateFormat.format(calendar.date()));
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
			int phoneNumber = Integer.valueOf(input("Enter phone number: ")).intValue();
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
