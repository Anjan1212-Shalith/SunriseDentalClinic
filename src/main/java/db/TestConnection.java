package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Diagnostic utility to test MySQL database connectivity and display schema info.
 */
public class TestConnection {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  SUNRISE DENTAL CLINIC - DB DIAGNOSTIC TEST     ");
        System.out.println("=================================================");
        System.out.println("Testing connection to jdbc:mysql://localhost:3306/clinic_db...");

        try (Connection con = DBconnection.getConnection()) {
            if (con != null && !con.isClosed()) {
                System.out.println(" SUCCESS: Database connection established successfully!");
                DatabaseMetaData meta = con.getMetaData();
                System.out.println(" Database Product : " + meta.getDatabaseProductName() + " v" + meta.getDatabaseProductVersion());
                System.out.println(" Driver Name      : " + meta.getDriverName() + " v" + meta.getDriverVersion());
                
                System.out.println("\nChecking Database Tables:");
                try (Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery("SHOW TABLES;")) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        System.out.println("   " + count + ". " + rs.getString(1));
                    }
                    if (count == 0) {
                        System.out.println("  [!] Warning: Database 'clinic_db' exists but has no tables yet.");
                        System.out.println("      Please run the 'clinic_db.sql' script in phpMyAdmin.");
                    } else {
                        System.out.println(" All tables detected properly (" + count + " tables).");
                    }
                }
            } else {
                System.err.println(" FAILED: Connection is null or closed.");
                printTroubleshootingGuide();
            }
        } catch (SQLException e) {
            System.err.println(" FAILED: SQL Exception during connection:");
            System.err.println(" Error Code: " + e.getErrorCode());
            System.err.println(" Message   : " + e.getMessage());
            printTroubleshootingGuide();
        }
        System.out.println("=================================================");
    }

    private static void printTroubleshootingGuide() {
        System.out.println("\n--- TROUBLESHOOTING GUIDE ---");
        System.out.println("1. Ensure WAMP Server is running and the tray icon is GREEN.");
        System.out.println("2. Open your browser and navigate to: http://localhost/phpmyadmin");
        System.out.println("3. Ensure the database 'clinic_db' is created.");
        System.out.println("4. Import or execute the 'clinic_db.sql' script located in your project directory.");
        System.out.println("5. Confirm MySQL port is 3306 and username is 'root' with no password.");
    }
}
