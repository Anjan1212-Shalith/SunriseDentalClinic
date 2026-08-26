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

// Lightweight Web Service for branch and external API access
public class ClinicWebService {

    private static HttpServer server;
    private static final int DEFAULT_PORT = 8088;
    private static boolean running = false;

    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private static final DentistDAO dentistDAO = new DentistDAO();

    // Start HTTP Web Service
    public static synchronized void startServer(int port) throws IOException {
        if (running) return;

        server = HttpServer.create(new InetSocketAddress(port), 0);

        // API endpoints
        server.createContext("/api/status", new StatusHandler());
        server.createContext("/api/appointments", new AppointmentsHandler());
        server.createContext("/api/appointment", new SingleAppointmentHandler());
        server.createContext("/api/dentists", new DentistsHandler());
        server.createContext("/api/treatments", new TreatmentsHandler());

        server.setExecutor(null);
        server.start();
        running = true;
        System.out.println("Web service started at http://localhost:" + port + "/api/");
    }

    // Start on default port 8088
    public static void startServer() {
        try {
            startServer(DEFAULT_PORT);
        } catch (IOException e) {
            System.out.println("Web service error: " + e.getMessage());
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
