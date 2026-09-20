import java.time.LocalDate;

public class Expense {
	private static int nextId=1;
	int ID=0;
	double amount;
	String category;
	LocalDate date=LocalDate.now();
	String description;
	
	public Expense(double amount, String category, LocalDate date, String description) {
	    this(nextId++, amount, category, date, description);
	}

	public Expense(int id, double amount, String category, LocalDate date, String description) {
	    this.ID = id;
	    this.amount = amount;
	    this.category = category;
	    this.date = date;
	    this.description = description;
	}
	public static int getNextId() {
	    return nextId;
	}

	public static void setNextId(int id) {
	    nextId = id;
	}
	
	public void printDetails() {
	    System.out.println("ID: #" + ID + " | Amount: " + amount + " | Category: " + category + " | Date: " + date + " | Description: " + description);
	}
	
}
