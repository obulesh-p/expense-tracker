import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class ServerMain {
    private static final int PORT = 8080;
    private static ExpenseTracker tracker = new ExpenseTracker();
    private static String filePath = System.getProperty("user.home") + "/Desktop/expences.txt";

    public static void main(String[] args) throws IOException {
        // 1. Load persisted data from Desktop
        tracker.loadFromFile(filePath);

        // 2. Start HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/api/expenses", new ExpensesApiHandler());
        server.setExecutor(null); // default executor
        server.start();

        System.out.println("Expense Server running at http://localhost:" + PORT + "/api/expenses");
    }

    static class ExpensesApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Enable CORS so the browser can make requests freely
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            String method = exchange.getRequestMethod();

            if (method.equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (method.equalsIgnoreCase("GET")) {
                handleGet(exchange);
            } else if (method.equalsIgnoreCase("POST")) {
                handlePost(exchange);
            } else if (method.equalsIgnoreCase("DELETE")) {
                handleDelete(exchange);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }

        // GET: Return all expenses as JSON array
        private void handleGet(HttpExchange exchange) throws IOException {
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < tracker.expenses.size(); i++) {
                Expense e = tracker.expenses.get(i);
                json.append(String.format(
                    "{\"id\":%d,\"amount\":%.2f,\"category\":\"%s\",\"date\":\"%s\",\"description\":\"%s\"}",
                    e.ID, e.amount, escapeJson(e.category), e.date.toString(), escapeJson(e.description)
                ));
                if (i < tracker.expenses.size() - 1) json.append(",");
            }
            json.append("]");

            sendResponse(exchange, 200, json.toString());
        }

        // POST: Receive new expense and save
        private void handlePost(HttpExchange exchange) throws IOException {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) body.append(line);

            String bodyStr = body.toString();
            try {
                double amount = Double.parseDouble(extractJsonField(bodyStr, "amount"));
                String category = extractJsonField(bodyStr, "category");
                String dateStr = extractJsonField(bodyStr, "date");
                String desc = extractJsonField(bodyStr, "description");

                LocalDate date = dateStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dateStr);

                Expense newExp = new Expense(amount, category, date, desc);
                tracker.addExpense(newExp);
                tracker.saveToFile(filePath);

                sendResponse(exchange, 201, "{\"status\":\"success\",\"id\":" + newExp.ID + "}");
            } catch (Exception ex) {
                sendResponse(exchange, 400, "{\"error\":\"Invalid payload: " + ex.getMessage() + "\"}");
            }
        }

        // DELETE: Remove by ID (/api/expenses?id=3)
        private void handleDelete(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            if (query != null && query.startsWith("id=")) {
                try {
                    int id = Integer.parseInt(query.substring(3));
                    boolean removed = tracker.deleteExpenseById(id);
                    if (removed) {
                        tracker.saveToFile(filePath);
                        sendResponse(exchange, 200, "{\"status\":\"deleted\"}");
                        return;
                    }
                } catch (NumberFormatException ignored) {}
            }
            sendResponse(exchange, 404, "{\"error\":\"Expense not found\"}");
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
            byte[] bytes = responseText.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(statusCode, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }

        private String extractJsonField(String json, String field) {
            String target = "\"" + field + "\"";
            int idx = json.indexOf(target);
            if (idx == -1) return "";
            int colon = json.indexOf(":", idx);
            if (colon == -1) return "";

            int start = colon + 1;
            while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;

            if (start < json.length() && json.charAt(start) == '"') {
                int end = json.indexOf('"', start + 1);
                return end != -1 ? json.substring(start + 1, end) : "";
            } else {
                int end = start;
                while (end < json.length() && json.charAt(end) != ',' && json.charAt(end) != '}') end++;
                return json.substring(start, end).trim();
            }
        }

        private String escapeJson(String raw) {
            return raw == null ? "" : raw.replace("\\", "\\\\").replace("\"", "\\\"");
        }
    }
}
