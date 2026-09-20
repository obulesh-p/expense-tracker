# Personal Expense Tracker (CLI)

A lightweight, console-based personal finance management application written in Java. The tracker enables users to record, categorize, analyze, and manage daily expenses with persistent file storage.

---

## Features

- **Record Transactions:** Log expenses with automated unique IDs, category tags, amounts, and dates (`java.time.LocalDate`).
- **Data Aggregation & Filtering:** 
  - Real-time overall spending calculations.
  - Case-insensitive category breakdown (e.g., Food, Travel, Utilities).
- **Safe Record Deletion:** Remove transactions cleanly by ID with immediate feedback.
- **Persistent Storage (File I/O):**
  - Exports transactions to a structured CSV format using `BufferedWriter`.
  - Rebuilds in-memory collections on startup via `BufferedReader`.
  - Edge-case sanitization: cleans description commas to preserve CSV column boundaries.
- **Robust State Management:** Utilizes constructor overloading and synchronized static counters to guarantee ID consistency across read/write cycles.

---

## Tech Stack & OOP Concepts

- **Language:** Java (JDK 8+)
- **Collections:** `ArrayList<Expense>` for in-memory dynamic state management.
- **Object-Oriented Programming:**
  - **Encapsulation & Domain Modeling:** Clean separation between `Expense`, `ExpenseTracker`, and the `Main` entry driver.
  - **Constructor Overloading:** Dynamic ID generation for runtime entries vs. exact ID restoration for persisted data.
  - **Static vs. Instance Scope:** Global transaction counter (`nextId`) synchronized with stored disk records.
- **Persistence:** Java File I/O (`BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter`).

---

## Project Structure

```text
expense-tracker/
├── src/
│   ├── Expense.java          # Domain model & ID state tracking
│   ├── ExpenseTracker.java   # Business logic, aggregations, and file I/O
│   └── Main.java             # Interactive CLI menu loop & input handling
├── expences.txt              # Auto-generated CSV data store
└── README.md
