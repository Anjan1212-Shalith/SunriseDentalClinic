package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Database connection class for Sunrise Dental Clinic
public class DBconnection {

    // Database connection details
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/clinic_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    // Load MySQL driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e1) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e2) {
                System.out.println("Driver Error: " + e2.getMessage());
            }
        }
    }

    // Get database connection
    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database Connection Error: " + e.getMessage());
        }
        return con;
    }

    // Check if database is connected
    public static boolean isConnected() {
        try (Connection con = getConnection()) {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    // Close connection
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
