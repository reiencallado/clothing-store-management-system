import javax.swing.*;
import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/DBclothing";
    private static final String USERNAME = "root";
    private static String PASSWORD = ""; // Default password
    private static boolean isInitialized = false;
    
    public static void initialize() {
        if (!isInitialized) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Test connection with the default password
                try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
                    JOptionPane.showMessageDialog(null, 
                        "Database connection successful!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    isInitialized = true;
                }
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, 
                    "MySQL Driver not found!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            } catch (SQLException e) {
                // If default password fails, prompt for password
                PASSWORD = JOptionPane.showInputDialog(null, 
                    "Enter database password:", 
                    "Database Connection", 
                    JOptionPane.QUESTION_MESSAGE);
                
                if (PASSWORD == null || PASSWORD.isEmpty()) {
                    JOptionPane.showMessageDialog(null, 
                        "No password provided. Application will exit.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
                
                try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
                    JOptionPane.showMessageDialog(null, 
                        "Database connection successful!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    isInitialized = true;
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Database connection failed: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (!isInitialized) {
            initialize();
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    
    public static String getURL() {
        return URL;
    }
    
    public static String getUsername() {
        return USERNAME;
    }
    
    public static String getPassword() {
        return PASSWORD;
    }
}