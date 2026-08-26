package clinicmanagementsystem;

import view.LoginForm;
import view.UITheme;

import javax.swing.*;

/**
 * Main Application Launcher for Sunrise Dental Clinic Management System
 */
public class MainApp {

    public static void main(String[] args) {
        // Set Look and Feel
        UITheme.setupLookAndFeel();

        // Launch Application on Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            } catch (Exception e) {
                System.err.println("Fatal Error initializing application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
