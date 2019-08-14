import java.text.SimpleDateFormat;
import java.util.Scanner;


public class Main {
	
	private static Scanner inputScanner;
	private static library thisLibrary;
	private static String menu;
	private static Calendar thisCalendar;
	private static SimpleDateFormat simpleDateFormat;
	
	
	private static String Get_menu() {
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
			thisLibrary = library.INSTANCE();
			thisCalendar = Calendar.INSTANCE();
			simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
			for (member thisMember : thisLibrary.MEMBERS()) {
				output(thisMember);
			}
			output(" ");
			for (book thisBook : thisLibrary.BOOKS()) {
				output(thisBook);
			}
						
			menu = Get_menu();
			
			boolean e = false;
			
			while (!e) {
				
				output("\n" + simpleDateFormat.format(thisCalendar.Date()));
				String c = input(menu);
				
				switch (c.toUpperCase()) {
				
				case "M": 
					ADD_MEMBER();
					break;
					
				case "LM": 
					MEMBERS();
					break;
					
				case "B": 
					ADD_BOOK();
					break;
					
				case "LB": 
					BOOKS();
					break;
					
				case "FB": 
					FIX_BOOKS();
					break;
					
				case "L": 
					BORROW_BOOK();
					break;
					
				case "R": 
					RETURN_BOOK();
					break;
					
				case "LL": 
					CURRENT_LOANS();
					break;
					
				case "P": 
					FINES();
					break;
					
				case "T": 
					INCREMENT_DATE();
					break;
					
				case "Q": 
					e = true;
					break;
					
				default: 
					output("\nInvalid option\n");
					break;
				}
				
				library.SAVE();
			}			
		} catch (RuntimeException e) {
			output(e);
		}		
		output("\nEnded\n");
	}	

		private static void FINES() {
		new PayFineUI(new PayFineControl()).RuN();		
	}


	private static void CURRENT_LOANS() {
		output("");
		for (loan loan : thisLibrary.CurrentLoans()) {
			output(loan + "\n");
		}		
	}



	private static void BOOKS() {
		output("");
		for (book book : thisLibrary.BOOKS()) {
			output(book + "\n");
		}		
	}



	private static void MEMBERS() {
		output("");
		for (member member : thisLibrary.MEMBERS()) {
			output(member + "\n");
		}		
	}



	private static void BORROW_BOOK() {
		new BorrowBookUI(new BorrowBookControl()).run();		
	}


	private static void RETURN_BOOK() {
		new ReturnBookUI(new ReturnBookControl()).RuN();		
	}


	private static void FIX_BOOKS() {
		new FixBookUI(new FixBookControl()).RuN();		
	}


	private static void INCREMENT_DATE() {
		try {
			int days = Integer.valueOf(input("Enter number of days: ")).intValue();
			thisCalendar.incrementDate(days);
			thisLibrary.checkCurrentLoans();
			output(simpleDateFormat.format(thisCalendar.Date()));
			
		} catch (NumberFormatException e) {
			 output("\nInvalid number of days\n");
		}
	}


	private static void ADD_BOOK() {
		
		String author = input("Enter author: ");
		String title  = input("Enter title: ");
		String callNumber = input("Enter call number: ");
		book thisBook = thisLibrary.Add_book(author, title, callNumber);
		output("\n" + thisBook + "\n");
		
	}

	
	private static void ADD_MEMBER() {
		try {
			String nameLast = input("Enter last name: ");
			String nameFirst  = input("Enter first name: ");
			String emailAdress = input("Enter email: ");
			int phoneNumber = Integer.valueOf(input("Enter phone number: ")).intValue();
			member thisMember = thisLibrary.Add_mem(nameLast, nameFirst, emailAdress, phoneNumber);
			output("\n" + thisMember + "\n");
			
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
