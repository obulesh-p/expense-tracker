import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ExpenseTracker {
	ArrayList <Expense> expenses=new ArrayList <>();
	
	public void addExpense(Expense expense) {
		expenses.add(expense);
	}
	
	public void showExpense() {
		for(Expense e:expenses) {
			e.printDetails();
		}
	}
	public double calculateTotal() {
		double sum=0.0;
		for(Expense e:expenses) {
			sum+=e.amount;
		}
		return sum;
	}
	public double calculateTotalByCategory(String category) {
	    double sum=0.0;
	    for(Expense e:expenses) {
	    	if(e.category.equalsIgnoreCase (category)) {
	    		sum+=e.amount;
	    	}
	    }
	    return sum;
	}
	public boolean deleteExpenseById(int id) {
	    for(int i =0;i<expenses.size() ;i++) {
	    	if(expenses.get(i).ID == id) {
	    		expenses.remove(i);
	    		return true;
	    	}
	    }
	    return false;
	}
	
	public void saveToFile(String filename) {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
	        for (Expense e : expenses) {
	        	
	            String cleanDescription = e.description.replace(",", " -");
	            
	            String line = e.ID + "," + e.amount + "," + e.category + "," + e.date + "," + cleanDescription;
	            
	            writer.write(line);
	            writer.newLine(); 
	        }
	        System.out.println("Saved " + expenses.size() + " expenses to " + filename);
	    } catch (IOException ex) {
	        System.out.println("Error writing to file: " + ex.getMessage());
	    }
	}
	

	public void loadFromFile(String filename) {
	    File file = new File(filename);
	    if (!file.exists()) {
	        return; 
	    }

	    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.trim().isEmpty()) continue;

	            String[] parts = line.split(",");
	            int id = Integer.parseInt(parts[0].trim());
	            if (id >= Expense.getNextId()) {
	                Expense.setNextId(id + 1);
	            }
	            
	            double amount = Double.parseDouble(parts[1].trim());
	            String category = parts[2].trim();
	            LocalDate date = LocalDate.parse(parts[3].trim());
	            String description = parts[4].trim();

	            Expense loaded = new Expense(id, amount, category, date, description);
	            expenses.add(loaded);
	        }
	        System.out.println("Loaded " + expenses.size() + " expenses from disk.");
	    } catch (IOException ex) {
	        System.out.println("Error reading file: " + ex.getMessage());
	    }
	}
	

}
