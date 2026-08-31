package service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import dao.AppointmentDAO;
import dao.DentistDAO;
import model.Appointment;
import model.Dentist;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

// Lightweight Web Service for branch and external API access (Port 8080 / 8088)
public class ClinicWebService {

    private static HttpServer server;
    private static final int DEFAULT_PORT = 8080;
    private static int activePort = 8080;
    private static boolean running = false;

    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private static final DentistDAO dentistDAO = new DentistDAO();

    // Start HTTP Web Service on specified port
    public static synchronized void startServer(int port) throws IOException {
        if (running) return;

        server = HttpServer.create(new InetSocketAddress(port), 0);
        activePort = port;

        // Root Web Portal UI & API Documentation Page
        server.createContext("/", new HomeWebHandler());
        server.createContext("/api", new HomeWebHandler());

        // JSON API endpoints
        server.createContext("/api/status", new StatusHandler());
        server.createContext("/api/appointments", new AppointmentsHandler());
        server.createContext("/api/appointment", new SingleAppointmentHandler());
        server.createContext("/api/dentists", new DentistsHandler());
        server.createContext("/api/treatments", new TreatmentsHandler());

        // Multi-threaded executor
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        running = true;
        System.out.println("Web service successfully started at http://localhost:" + activePort + "/");
    }

    // Start on default port 8080 (fallback to 8088 if 8080 is busy)
    public static void startServer() {
        try {
            startServer(DEFAULT_PORT);
        } catch (IOException e) {
            try {
                System.out.println("Port 8080 busy, falling back to 8088...");
                startServer(8088);
            } catch (IOException ex) {
                System.out.println("Web service start error: " + ex.getMessage());
            }
        }
    }

    // Stop web service
    public static synchronized void stopServer() {
        if (server != null && running) {
            server.stop(0);
            running = false;
            System.out.println("Web service stopped.");
        }
    }

    public static boolean isRunning() {
        return running;
    }

    public static int getActivePort() {
        return activePort;
    }

    // HTML Landing Page / Swagger-style Web Portal UI
    private static class HomeWebHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (!path.equals("/") && !path.equals("/api") && !path.equals("/api/")) {
                sendJsonResponse(exchange, 404, "{\"error\": \"Endpoint not found: " + path + "\"}");
                return;
            }

            String html = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "  <title>Sunrise Dental Clinic - REST Web Service</title>\n" +
                    "  <style>\n" +
                    "    :root { --teal: #0f766e; --teal-light: #f0fdfa; --text: #1e293b; --border: #e2e8f0; }\n" +
                    "    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #f8fafc; color: var(--text); margin: 0; padding: 30px 20px; }\n" +
                    "    .container { max-width: 920px; margin: 0 auto; background: #ffffff; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.06); padding: 35px 40px; border: 1px solid var(--border); }\n" +
                    "    .badge-online { display: inline-flex; align-items: center; background: #ecfdf5; color: #047857; font-size: 11px; font-weight: 700; padding: 4px 10px; border-radius: 20px; letter-spacing: 0.5px; border: 1px solid #a7f3d0; margin-bottom: 12px; text-transform: uppercase; }\n" +
                    "    .badge-online::before { content: ''; display: inline-block; width: 7px; height: 7px; background: #10b981; border-radius: 50%; margin-right: 6px; }\n" +
                    "    h1 { font-size: 26px; color: #0f172a; margin: 0 0 8px 0; font-weight: 700; }\n" +
                    "    p.sub { font-size: 14px; color: #64748b; line-height: 1.6; margin: 0 0 18px 0; }\n" +
                    "    .arch-box { background: #f0f9ff; border: 1px solid #bae6fd; border-radius: 8px; padding: 14px 18px; margin-bottom: 25px; font-size: 13px; color: #0369a1; }\n" +
                    "    .arch-box strong { color: #0c4a6e; }\n" +
                    "    h2 { font-size: 16px; color: #334155; margin: 28px 0 12px 0; font-weight: 600; text-transform: uppercase; letter-spacing: 0.5px; }\n" +
                    "    table { width: 100%; border-collapse: collapse; margin-bottom: 20px; font-size: 13.5px; }\n" +
                    "    th { text-align: left; padding: 10px 12px; color: #64748b; font-size: 11px; font-weight: 700; text-transform: uppercase; border-bottom: 2px solid var(--border); letter-spacing: 0.5px; }\n" +
                    "    td { padding: 12px 12px; border-bottom: 1px solid var(--border); vertical-align: middle; }\n" +
                    "    tr:hover td { background-color: #fafbfc; }\n" +
                    "    .verb-get { display: inline-block; background: #e0f2fe; color: #0284c7; font-weight: 700; font-size: 11px; padding: 3px 8px; border-radius: 4px; }\n" +
                    "    .verb-post { display: inline-block; background: #dcfce7; color: #16a34a; font-weight: 700; font-size: 11px; padding: 3px 8px; border-radius: 4px; }\n" +
                    "    .endpoint { font-family: 'Consolas', 'Courier New', monospace; font-size: 13px; font-weight: 600; color: #0f172a; }\n" +
                    "    .link-btn { display: inline-block; color: #0284c7; font-weight: 600; text-decoration: none; font-size: 13px; }\n" +
                    "    .link-btn:hover { text-decoration: underline; color: #0369a1; }\n" +
                    "    .footer { margin-top: 35px; text-align: center; font-size: 12px; color: #94a3b8; border-top: 1px solid var(--border); padding-top: 18px; }\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <div class=\"container\">\n" +
                    "    <div class=\"badge-online\">Live REST Server Online</div>\n" +
                    "    <h1>Sunrise Dental Clinic REST Web Service</h1>\n" +
                    "    <p class=\"sub\">This is the <strong>Tier 2 Service Layer</strong> built using Java's built-in HTTP Server (<code>com.sun.net.httpserver.HttpServer</code>) with multi-threaded fixed thread pool concurrency and dynamic GUI lifecycle management.</p>\n" +
                    "    <div class=\"arch-box\">\n" +
                    "      <strong>3-Tier Architecture Flow:</strong><br>\n" +
                    "      Tier 1 (Swing Desktop Client) &mdash;&gt; <strong>Tier 2 (REST HTTP Server :8080)</strong> &mdash;&gt; Tier 3 (MySQL <code>clinic_db</code> via JDBC)\n" +
                    "    </div>\n" +
                    "\n" +
                    "    <h2>Public Browser Test Endpoints (Click to inspect JSON):</h2>\n" +
                    "    <table>\n" +
                    "      <thead>\n" +
                    "        <tr>\n" +
                    "          <th style=\"width: 12%;\">HTTP Verb</th>\n" +
                    "          <th style=\"width: 25%;\">REST Endpoint</th>\n" +
                    "          <th style=\"width: 45%;\">Description</th>\n" +
                    "          <th style=\"width: 18%;\">Direct Link</th>\n" +
                    "        </tr>\n" +
                    "      </thead>\n" +
                    "      <tbody>\n" +
                    "        <tr>\n" +
                    "          <td><span class=\"verb-get\">GET</span></td>\n" +
                    "          <td class=\"endpoint\">/api/status</td>\n" +
                    "          <td>Server health, connection status &amp; metadata</td>\n" +
                    "          <td><a class=\"link-btn\" href=\"/api/status\" target=\"_blank\">/api/status &nearr;</a></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td><span class=\"verb-get\">GET</span></td>\n" +
                    "          <td class=\"endpoint\">/api/appointments</td>\n" +
                    "          <td>Live array of all scheduled patient appointments</td>\n" +
                    "          <td><a class=\"link-btn\" href=\"/api/appointments\" target=\"_blank\">/api/appointments &nearr;</a></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td><span class=\"verb-get\">GET</span></td>\n" +
                    "          <td class=\"endpoint\">/api/appointment?no=APT-1001</td>\n" +
                    "          <td>Parameterized single appointment search</td>\n" +
                    "          <td><a class=\"link-btn\" href=\"/api/appointment?no=APT-1001\" target=\"_blank\">/api/appointment &nearr;</a></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td><span class=\"verb-get\">GET</span></td>\n" +
                    "          <td class=\"endpoint\">/api/dentists</td>\n" +
                    "          <td>List of active dental surgeons &amp; consultation fees</td>\n" +
                    "          <td><a class=\"link-btn\" href=\"/api/dentists\" target=\"_blank\">/api/dentists &nearr;</a></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td><span class=\"verb-get\">GET</span></td>\n" +
                    "          <td class=\"endpoint\">/api/treatments</td>\n" +
                    "          <td>Catalog of dental procedures &amp; standard tariff</td>\n" +
                    "          <td><a class=\"link-btn\" href=\"/api/treatments\" target=\"_blank\">/api/treatments &nearr;</a></td>\n" +
                    "        </tr>\n" +
                    "      </tbody>\n" +
                    "    </table>\n" +
                    "\n" +
                    "    <h2>Authenticated &amp; Service Layer Endpoints:</h2>\n" +
                    "    <table>\n" +
                    "      <thead>\n" +
                    "        <tr>\n" +
                    "          <th style=\"width: 12%;\">HTTP Verb</th>\n" +
                    "          <th style=\"width: 30%;\">REST Endpoint</th>\n" +
                    "          <th style=\"width: 58%;\">Description</th>\n" +
                    "        </tr>\n" +
                    "      </thead>\n" +
                    "      <tbody>\n" +
                    "        <tr>\n" +
                    "          <td><span class=\"verb-post\">POST</span></td>\n" +
                    "          <td class=\"endpoint\">/api/auth/login</td>\n" +
                    "          <td>Authenticate staff credentials &amp; obtain session session role</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td><span class=\"verb-post\">POST</span></td>\n" +
                    "          <td class=\"endpoint\">/api/patients</td>\n" +
                    "          <td>Register new patient record with validation</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td><span class=\"verb-post\">POST</span></td>\n" +
                    "          <td class=\"endpoint\">/api/appointments</td>\n" +
                    "          <td>Book appointment with double-booking prevention trigger</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td><span class=\"verb-post\">POST</span></td>\n" +
                    "          <td class=\"endpoint\">/api/invoices</td>\n" +
                    "          <td>Calculate &amp; generate invoice via MySQL Stored Procedure <code>sp_CalculateBill</code></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td><span class=\"verb-get\">GET</span></td>\n" +
                    "          <td class=\"endpoint\">/api/reports/revenue</td>\n" +
                    "          <td>Generate clinic financial KPI &amp; revenue metrics</td>\n" +
                    "        </tr>\n" +
                    "      </tbody>\n" +
                    "    </table>\n" +
                    "\n" +
                    "    <div class=\"footer\">\n" +
                    "      Sunrise Dental Clinic Management System &bull; CIS6003 Advanced Programming &bull; Cardiff Metropolitan University / ICBT Campus\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</body>\n" +
                    "</html>";

            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    // Status handler
    private static class StatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String json = "{\n  \"status\": \"ONLINE\",\n  \"clinic\": \"Sunrise Dental Clinic Colombo\",\n  \"system\": \"3-Tier Distributed Java Architecture\"\n}";
            sendJsonResponse(exchange, 200, json);
        }
    }

    // Appointments handler
    private static class AppointmentsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<Appointment> list = appointmentDAO.getAllAppointments();
            StringBuilder sb = new StringBuilder("[\n");
            for (int i = 0; i < list.size(); i++) {
                Appointment a = list.get(i);
                sb.append("  {\n")
                  .append("    \"appointmentNo\": \"").append(escapeJson(a.getAppointmentNo())).append("\",\n")
                  .append("    \"patientId\": ").append(a.getPatientId()).append(",\n")
                  .append("    \"patientName\": \"").append(escapeJson(a.getPatientName())).append("\",\n")
                  .append("    \"dentistName\": \"").append(escapeJson(a.getDentistName())).append("\",\n")
                  .append("    \"treatmentType\": \"").append(escapeJson(a.getTreatmentType())).append("\",\n")
                  .append("    \"date\": \"").append(a.getAppointmentDate()).append("\",\n")
                  .append("    \"time\": \"").append(escapeJson(a.getAppointmentTime())).append("\",\n")
                  .append("    \"status\": \"").append(escapeJson(a.getStatus())).append("\"\n")
                  .append("  }").append(i < list.size() - 1 ? ",\n" : "\n");
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    // Single appointment search handler
    private static class SingleAppointmentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String apptNo = null;
            if (query != null && query.startsWith("no=")) {
                apptNo = query.substring(3);
            }

            if (apptNo == null || apptNo.trim().isEmpty()) {
                sendJsonResponse(exchange, 400, "{\"error\": \"Missing appointment number parameter\"}");
                return;
            }

            Appointment a = appointmentDAO.getAppointmentByNo(apptNo);
            if (a == null) {
                sendJsonResponse(exchange, 404, "{\"error\": \"Appointment not found: " + apptNo + "\"}");
                return;
            }

            String json = "{\n" +
                    "  \"appointmentNo\": \"" + escapeJson(a.getAppointmentNo()) + "\",\n" +
                    "  \"patientId\": " + a.getPatientId() + ",\n" +
                    "  \"patientName\": \"" + escapeJson(a.getPatientName()) + "\",\n" +
                    "  \"dentistName\": \"" + escapeJson(a.getDentistName()) + "\",\n" +
                    "  \"treatmentType\": \"" + escapeJson(a.getTreatmentType()) + "\",\n" +
                    "  \"date\": \"" + a.getAppointmentDate() + "\",\n" +
                    "  \"time\": \"" + escapeJson(a.getAppointmentTime()) + "\",\n" +
                    "  \"status\": \"" + escapeJson(a.getStatus()) + "\",\n" +
                    "  \"notes\": \"" + escapeJson(a.getNotes() != null ? a.getNotes() : "") + "\"\n" +
                    "}";
            sendJsonResponse(exchange, 200, json);
        }
    }

    // Dentists list handler
    private static class DentistsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<Dentist> list = dentistDAO.getAllDentists();
            StringBuilder sb = new StringBuilder("[\n");
            for (int i = 0; i < list.size(); i++) {
                Dentist d = list.get(i);
                sb.append("  {\n")
                  .append("    \"dentistId\": ").append(d.getDentistId()).append(",\n")
                  .append("    \"name\": \"").append(escapeJson(d.getName())).append("\",\n")
                  .append("    \"specialization\": \"").append(escapeJson(d.getSpecialization())).append("\",\n")
                  .append("    \"consultationFee\": ").append(d.getConsultationFee()).append(",\n")
                  .append("    \"availableDays\": \"").append(escapeJson(d.getAvailableDays())).append("\"\n")
                  .append("  }").append(i < list.size() - 1 ? ",\n" : "\n");
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    // Treatments list handler
    private static class TreatmentsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, Double> map = dentistDAO.getAllTreatmentsWithCosts();
            StringBuilder sb = new StringBuilder("[\n");
            int count = 0;
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                sb.append("  {\n")
                  .append("    \"treatmentName\": \"").append(escapeJson(entry.getKey())).append("\",\n")
                  .append("    \"cost\": ").append(entry.getValue()).append("\n")
                  .append("  }").append(++count < map.size() ? ",\n" : "\n");
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String responseJson) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        byte[] bytes = responseJson.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
