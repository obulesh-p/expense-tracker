# Vaultly — Full-Stack Personal Expense Tracker

A full-stack personal finance application powered by a native Java HTTP server backend and an interactive, modern web dashboard. The application provides persistent transaction storage, real-time analytics, defensive validation, and seamless client-server data synchronization.

---

## Architecture Overview

Vaultly decouples application concerns using a lightweight client-server architecture:
[ Web Browser Frontend ]
│
HTTP / JSON (REST API)
│
▼
[ Java Native HttpServer (Port 8080) ]
│
Domain Logic & Collections (ArrayList)
│
Persistent Storage (CSV / Disk I/O)
- **Frontend:** Responsive dashboard featuring real-time stat summaries, category breakdown visualization via SVG donut charts, 7-day trend analysis, dynamic tables, and modal workflows.
- **Backend:** Native Java HTTP server (`com.sun.net.httpserver.HttpServer`) handling routing, request parsing, JSON serialization, and CORS headers with zero external dependencies.
- **Persistence Layer:** Flat-file CSV engine using `BufferedReader` and `BufferedWriter` with comma-escaping and state synchronization.

---

## Features

- **Transaction Management:** Add, list, search, filter, and delete expenses with instant UI feedback.
- **Dynamic Analytics:**
  - Automated calculation of total expenditures and monthly aggregations.
  - Interactive SVG-based category spend distribution.
  - 7-day spending trends.
- **Full-Stack REST Endpoints:**
  - `GET /api/expenses` — Returns all logged transactions as a JSON array.
  - `POST /api/expenses` — Validates and logs a new transaction payload.
  - `DELETE /api/expenses?id={id}` — Removes an expense and synchronizes disk storage.
- **Persistence & Integrity:**
  - Preserves data across sessions in a local CSV format (`expences.txt`).
  - Automatic CSV boundary protection by sanitizing commas in descriptions.
  - Overloaded constructors ensure consistent ID incrementation across disk loads and runtime creation.
- **Defensive Design:** Client-side form guards combined with robust backend parsing.

---

## Tech Stack

- **Backend:** Java (JDK 8+), `HttpServer`, `BufferedReader`, `BufferedWriter`, `LocalDate`
- **Frontend:** HTML5, CSS3 (Custom properties, Flexbox, CSS Grid), Vanilla JavaScript (`async/await`, Fetch API)
- **Data Format:** RESTful JSON communication, CSV local persistence

---

## Project Structure

```text
expense-tracker/
├── Expense.java          # Core domain model & ID state tracking
├── ExpenseTracker.java   # Aggregation logic, list manipulation & file I/O
├── Main.java             # Terminal CLI interface (alternative run mode)
├── ServerMain.java       # Native Java HTTP REST API server
├── expense-tracker.html  # Modern web dashboard interface
└── README.md             # Project documentation
