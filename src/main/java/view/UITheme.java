package view;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.net.URL;

/**
 * UI Theme & Styling Tokens for Sunrise Dental Clinic Swing Application
 */
public class UITheme {

    // Color Palette
    public static final Color PRIMARY = new Color(15, 118, 110);        // Deep Teal
    public static final Color PRIMARY_HOVER = new Color(17, 94, 89);    // Darker Teal
    public static final Color ACCENT = new Color(13, 148, 136);         // Mint Teal
    public static final Color BG_LIGHT = new Color(248, 250, 252);      // Slate 50
    public static final Color CARD_BG = new Color(255, 255, 255);       // White
    public static final Color TEXT_DARK = new Color(15, 23, 42);        // Slate 900
    public static final Color TEXT_MUTED = new Color(100, 116, 139);    // Slate 500
    public static final Color BORDER_COLOR = new Color(226, 232, 240);  // Slate 200
    public static final Color SUCCESS = new Color(16, 185, 129);        // Emerald 500
    public static final Color DANGER = new Color(239, 68, 68);          // Red 500
    public static final Color WARNING = new Color(245, 158, 11);        // Amber 500
    public static final Color SIDEBAR_BG = new Color(15, 23, 42);       // Slate 900

    // Typography
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    /**
     * Initializes modern System Look & Feel
     */
    public static void setupLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }
    }

    /**
     * Loads the Clinic Logo image as an ImageIcon scaled to the specified dimensions
     * @param width Desired width in pixels
     * @param height Desired height in pixels
     * @return Scaled ImageIcon or null if file not found
     */
    public static ImageIcon getLogoIcon(int width, int height) {
        try {
            URL imgURL = UITheme.class.getResource("/images/logo.png");
            if (imgURL != null) {
                ImageIcon original = new ImageIcon(imgURL);
                Image scaled = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (Exception e) {
            System.err.println("Could not load logo icon: " + e.getMessage());
        }
        return null;
    }

    /**
     * Sets the application window taskbar/titlebar icon
     * @param frame JFrame to set icon on
     */
    public static void setFrameIcon(JFrame frame) {
        try {
            URL imgURL = UITheme.class.getResource("/images/logo.png");
            if (imgURL != null) {
                frame.setIconImage(new ImageIcon(imgURL).getImage());
            }
        } catch (Exception ignored) {}
    }

    /**
     * Creates a styled primary button
     */
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    /**
     * Creates a styled secondary button
     */
    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setForeground(TEXT_DARK);
        btn.setBackground(new Color(241, 245, 249));
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(9, 17, 9, 17)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    /**
     * Creates a styled danger button
     */
    public static JButton createDangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(DANGER);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    /**
     * Creates a styled text input field
     */
    public static JTextField createTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(FONT_REGULAR);
        tf.setForeground(TEXT_DARK);
        tf.setBackground(Color.WHITE);
        tf.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(6, 10, 6, 10)));
        return tf;
    }

    /**
     * Creates a styled password input field
     */
    public static JPasswordField createPasswordField(int columns) {
        JPasswordField pf = new JPasswordField(columns);
        pf.setFont(FONT_REGULAR);
        pf.setForeground(TEXT_DARK);
        pf.setBackground(Color.WHITE);
        pf.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(6, 10, 6, 10)));
        return pf;
    }

    /**
     * Creates a styled card container panel
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(15, 15, 15, 15)));
        return panel;
    }

    /**
     * Styles a JTable with professional headers and row formatting
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(32);
        table.setGridColor(BORDER_COLOR);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setSelectionBackground(new Color(204, 251, 241));
        table.setSelectionForeground(TEXT_DARK);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(new Color(241, 245, 249));
        header.setForeground(TEXT_DARK);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));
        header.setBorder(new LineBorder(BORDER_COLOR));

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBorder(new EmptyBorder(0, 8, 0, 8));

        table.setDefaultRenderer(Object.class, leftRenderer);
    }
}
