import java.util.Scanner;
import java.time.LocalDate;

public class Main {
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		ExpenseTracker tracker=new ExpenseTracker();
		
		String path = "/Users/obulesh/Desktop/expenses.txt";
		tracker.loadFromFile(path);
		
		while (true) {
			System.out.println("\n--- EXPENSE TRACKER ---");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Total");
            System.out.println("4. View Total by Category");
            System.out.println("5. Delete Expense");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            int choice=sc.nextInt();
            sc.nextLine();
            
            switch(choice) {
            case 1:
            	System.out.println("enter the amount ");
            	double amount=sc.nextDouble();
            	sc.nextLine();
            	
            	System.out.println("enter the category ");
            	String category=sc.nextLine();
            	
            	System.out.println("description  ");
            	String des=sc.nextLine();
            	
            	Expense expense=new Expense(amount,category,LocalDate.now(),des);
            	tracker.addExpense(expense);
            	break;
            
            case 2:
            	tracker.showExpense();
            	break;
            
            case 3:
            	System.out.println("total expense "+tracker.calculateTotal());
            	break;
            	
            case 4:
            	System.out.println("enter the category you want ");
            	String cat=sc.nextLine();
            	
            	System.out.println("total expense by Category "+tracker.calculateTotalByCategory(cat));
            	break;
            case 5:
            	System.out.print("Enter the ID to delete: ");
                int idToDelete = sc.nextInt();
                sc.nextLine(); 
                
                boolean deleted = tracker.deleteExpenseById(idToDelete);
                if (deleted) {
                    System.out.println("Expense #" + idToDelete + " removed.");
                } else {
                    System.out.println("No expense found with ID #" + idToDelete + ".");
                }
            	break;
            case 6:
            	System.out.println("Goodbye!");
            	tracker.saveToFile(path);
            	return;
            default:
            	System.out.println("invalid choice. Try Again");
            }
		}
	}

}
