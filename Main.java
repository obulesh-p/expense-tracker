import java.util.Scanner;
import java.time.LocalDate;

public class Main {
	public static int readInt(Scanner sc, String prompt) {
	    while (true) {
	        System.out.print(prompt);
	        try {
	            int val = Integer.parseInt(sc.nextLine().trim());
	            return val;
	        } catch (NumberFormatException e) {
	            System.out.println(" Invalid input! Please enter a whole number.");
	        }
	    }
	}
	
	public static double readPositiveDouble(Scanner sc, String prompt) {
	    while (true) {
	        System.out.print(prompt);
	        try {
	            double val = Double.parseDouble(sc.nextLine().trim());
	            if (val <= 0) {
	                System.out.println(" Amount must be greater than 0.");
	                continue;
	            }
	            return val;
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid input! Please enter a valid decimal number (e.g. 120.50).");
	        }
	    }
	}
	public static String readNonEmptyString(Scanner sc, String prompt) {
	    while (true) {
	        System.out.print(prompt);
	        String input = sc.nextLine().trim();
	        if (input.isEmpty()) {
	            System.out.println(" This field cannot be empty. Please enter some text.");
	            continue;
	        }
	        return input;
	    }
	}
	
	public static void main(String[] args) {
	    Scanner sc = new Scanner(System.in);
	    ExpenseTracker tracker = new ExpenseTracker();
	    
	    String path = System.getProperty("user.home") + "/Desktop/expences.txt";
	    tracker.loadFromFile(path);
	    
	    while (true) {
	        System.out.println("\n--- EXPENSE TRACKER ---");
	        System.out.println("1. Add Expense");
	        System.out.println("2. View All Expenses");
	        System.out.println("3. View Total");
	        System.out.println("4. View Total by Category");
	        System.out.println("5. Delete Expense");
	        System.out.println("6. Exit");
	        
	        // Bulletproof choice input:
	        int choice = readInt(sc, "Enter choice: ");
	        
	        switch(choice) {
	        case 1:
	            double amount = readPositiveDouble(sc, "Enter amount: ");
	            String category = readNonEmptyString(sc, "Enter category: ");
	            String des = readNonEmptyString(sc, "Description: ");
	            
	            Expense expense = new Expense(amount, category, LocalDate.now(), des);
	            tracker.addExpense(expense);
	            System.out.println(" Expense added!");
	            break;
	        
	        case 2:
	            tracker.showExpense();
	            break;
	        
	        case 3:
	            System.out.println("Total expense: " + tracker.calculateTotal());
	            break;
	            
	        case 4:
	            String cat = readNonEmptyString(sc, "Enter category: ");
	            System.out.println("Total for '" + cat + "': " + tracker.calculateTotalByCategory(cat));
	            break;
	            
	        case 5:
	            int idToDelete = readInt(sc, "Enter ID to delete: ");
	            boolean deleted = tracker.deleteExpenseById(idToDelete);
	            if (deleted) {
	                System.out.println(" Expense #" + idToDelete + " removed.");
	            } else {
	                System.out.println(" No expense found with ID #" + idToDelete + ".");
	            }
	            break;
	            
	        case 6:
	            System.out.println("Goodbye!");
	            tracker.saveToFile(path);
	            sc.close();
	            return;
	            
	        default:
	            System.out.println(" Invalid choice. Please pick between 1 and 6.");
	        }
	    }
	}

}
