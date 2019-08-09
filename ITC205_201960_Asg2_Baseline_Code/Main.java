import java.text.SimpleDateFormat;
import java.util.Scanner;


public class Main {
	
	private static Scanner IN;
	private static Library LIB;
	private static String MENU;
	private static Calendar CAL;
	private static SimpleDateFormat SDF;
	
	
	private static String GetMenu() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nLibrary Main Menu\n\n")
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
		  
		return sb.toString();
	}


	public static void main(String[] args) {		
		try {			
			IN = new Scanner(System.in);
			LIB = library.INSTANCE();
			CAL = Calendar.INSTANCE();
			SDF = new SimpleDateFormat("dd/MM/yyyy");
	
			for (member m : LIB.Members()) {
				output(m);
			}
			output(" ");
			for (book b : LIB.Books()) {
				output(b);
			}
						
			MENU = GetMenu();
			
			boolean e = false;
			
			while (!e) {
				
				output("\n" + SDF.format(CAL.Date()));
				String c = input(MENU);
				
				switch (c.toUpperCase()) {
				
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
					e = true;
					break;
					
				default: 
					Output("\nInvalid option\n");
					break;
				}
				
				Library.SAVE();
			}			
		} catch (RuntimeException e) {
			output(e);
		}		
		output("\nEnded\n");
	}	

	
	private static void Fines() {
		new PayFineUI(new PayFineControl()).Run();		
	}


	private static void CurrentLoans() {
		output("");
		for (loan loan : LIB.CurrentLoans()) {
			output(loan + "\n");
		}		
	}



	private static void Books() {
		output("");
		for (book book : LIB.BOOKS()) {
			output(book + "\n");
		}		
	}



	private static void Members() {
		output("");
		for (member member : LIB.MEMBERS()) {
			output(member + "\n");
		}		
	}



	private static void BorrowBook() {
		new BorrowBookUI(new BorrowBookControl()).Run();		
	}


	private static void ReturnBook() {
		new ReturnBookUI(new ReturnBookControl()).Run();		
	}


	private static void FixBooks() {
		new FixBookUI(new FixBookControl()).Run();		
	}


	private static void IncrementDate() {
		try {
			int days = Integer.valueOf(input("Enter number of days: ")).intValue();
			CAL.incrementDate(days);
			LIB.checkCurrentLoans();
			output(SDF.format(CAL.Date()));
			
		} catch (NumberFormatException e) {
			 output("\nInvalid number of days\n");
		}
	}


	private static void AddBook() {
		
		String A = input("Enter author: ");
		String T  = input("Enter title: ");
		String C = input("Enter call number: ");
		book B = LIB.Add_book(A, T, C);
		output("\n" + B + "\n");
		
	}

	
	private static void AddMember() {
		try {
			String LN = input("Enter last name: ");
			String FN  = input("Enter first name: ");
			String EM = input("Enter email: ");
			int PN = Integer.valueOf(input("Enter phone number: ")).intValue();
			member M = LIB.Add_mem(LN, FN, EM, PN);
			output("\n" + M + "\n");
			
		} catch (NumberFormatException e) {
			 output("\nInvalid phone number\n");
		}
		
	}


	private static String Input(String prompt) {
		System.out.print(prompt);
		return IN.nextLine();
	}
	
	
	
	private static void Output(Object object) {
		System.out.println(object);
	}

	
}
