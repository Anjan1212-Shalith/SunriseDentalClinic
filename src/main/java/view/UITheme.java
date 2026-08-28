package view;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.net.URL;

// UI Theme and styling tokens for Sunrise Dental Clinic
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

    // Modern look and feel setup
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

    // Get scaled clinic logo icon
    public static ImageIcon getLogoIcon(int width, int height) {
        try {
            URL imgURL = UITheme.class.getResource("/images/logo.png");
            if (imgURL != null) {
                ImageIcon original = new ImageIcon(imgURL);
                Image scaled = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (Exception e) {
            System.out.println("Could not load logo icon: " + e.getMessage());
        }
        return null;
    }

    // Get scaled PNG icon from icons directory
    public static ImageIcon getIcon(String iconName, int width, int height) {
        try {
            URL imgURL = UITheme.class.getResource("/images/icons/" + iconName);
            if (imgURL != null) {
                ImageIcon original = new ImageIcon(imgURL);
                Image scaled = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (Exception e) {
            System.out.println("Could not load icon " + iconName + ": " + e.getMessage());
        }
        return null;
    }

    // Convenience method with default 18x18 size
    public static ImageIcon getIcon(String iconName) {
        return getIcon(iconName, 18, 18);
    }

    // Set frame window icon
    public static void setFrameIcon(JFrame frame) {
        try {
            URL imgURL = UITheme.class.getResource("/images/logo.png");
            if (imgURL != null) {
                frame.setIconImage(new ImageIcon(imgURL).getImage());
            }
        } catch (Exception ignored) {}
    }

    // Set standard uniform window size across all forms (1050 x 680)
    public static void setStandardWindowSize(JFrame frame) {
        Dimension dim = new Dimension(1050, 680);
        frame.setPreferredSize(dim);
        frame.setMinimumSize(dim);
        frame.setSize(dim);
        frame.setLocationRelativeTo(null);
    }

    // Create styled primary button
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

    // Create styled secondary button
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

    // Create styled danger button
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

    // Create styled text field
    public static JTextField createTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(FONT_REGULAR);
        tf.setForeground(TEXT_DARK);
        tf.setBackground(Color.WHITE);
        tf.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(6, 10, 6, 10)));
        return tf;
    }

    // Create styled password field
    public static JPasswordField createPasswordField(int columns) {
        JPasswordField pf = new JPasswordField(columns);
        pf.setFont(FONT_REGULAR);
        pf.setForeground(TEXT_DARK);
        pf.setBackground(Color.WHITE);
        pf.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(6, 10, 6, 10)));
        return pf;
    }

    // Create styled card panel
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(15, 15, 15, 15)));
        return panel;
    }

    // Style JTable with clean headers
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
