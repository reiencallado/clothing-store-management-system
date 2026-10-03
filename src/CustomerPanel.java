import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CustomerPanel extends JPanel {
    // Database connection handled by DBConnection class

    final public String opening = "Customer Records Management";
    final public String b1Text = "View All Customers";
    final public String b2Text = "View Customer Purchase History";
    final public String prevText = "Back";

    final Font font = new Font("Arial", Font.PLAIN, 20);
    final Font titleFont = new Font("Arial", Font.BOLD, 25);
    final Dimension buttonSize = new Dimension(350, 50);

    private DisplayData displayData = new DisplayData();
    private ClothingStoreApp mainApp;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public CustomerPanel(ClothingStoreApp app) {
        this.mainApp = app;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Create a panel with CardLayout to switch between different views
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        
        // Create and add the main menu panel
        JPanel menuPanel = createMainMenuPanel();
        mainPanel.add(menuPanel, "mainMenu");
        
        // Add the main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
        
        // Show the main menu initially
        cardLayout.show(mainPanel, "mainMenu");
    }

    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(opening, SwingConstants.CENTER);
        titleLabel.setFont(titleFont);

        JButton btn1 = new JButton(b1Text);
        JButton btn2 = new JButton(b2Text);

        btn1.setFont(font);
        btn2.setFont(font);

        btn1.setPreferredSize(buttonSize);
        btn2.setPreferredSize(buttonSize);

        JButton prevBtn = new JButton(prevText);
        prevBtn.setFont(font);
        prevBtn.setBackground(Color.decode("#880808"));
        prevBtn.setForeground(Color.WHITE);
        prevBtn.setOpaque(true);
        prevBtn.setBorderPainted(false);

        btn1.addActionListener(e -> {
            ResultSet rs = getAllCustomers();
            if (rs != null) {
                JPanel dataPanel = createDataPanel(rs, "All Customers");
                mainPanel.add(dataPanel, "allCustomers");
                cardLayout.show(mainPanel, "allCustomers");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load customers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn2.addActionListener(e -> showSelectCustomerForPurchases());
        prevBtn.addActionListener(e -> mainApp.showPanel("MainMenu"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        panel.add(btn1, gbc);

        gbc.gridy = 2;
        panel.add(btn2, gbc);

        gbc.gridy = 3;
        panel.add(prevBtn, gbc);

        return panel;
    }

    private JPanel createDataPanel(ResultSet rs, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel dataTitle = new JLabel(title, SwingConstants.CENTER);
        dataTitle.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(dataTitle, BorderLayout.NORTH);
        
        JTable table = displayData.createTableFromResultSet(rs);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "mainMenu"));
        JPanel backPanel = new JPanel();
        backPanel.add(backButton);
        panel.add(backPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private void showSelectCustomerForPurchases() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Title at the top
        JLabel titleLabel = new JLabel("Select Customer to View Purchases", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Main content panel with GridBagLayout for better control
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        // Customer selection dropdown
        JComboBox<String> customerComboBox = new JComboBox<>(displayData.getComboBoxData(
            "SELECT CONCAT(customer_id, ' - ', first_name, ' ', last_name) FROM customer ORDER BY last_name, first_name"
        ));
        customerComboBox.setPreferredSize(new Dimension(400, 30));
        customerComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Proceed button
        JButton proceedBtn = new JButton("Proceed");
        proceedBtn.setFont(font);
        proceedBtn.setBackground(Color.decode("#008000"));
        proceedBtn.setForeground(Color.WHITE);
        proceedBtn.setOpaque(true);
        proceedBtn.setBorderPainted(false);
        proceedBtn.setPreferredSize(new Dimension(200, 40));
        proceedBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        proceedBtn.addActionListener(e -> {
            String selectedCustomer = (String) customerComboBox.getSelectedItem();
            if (selectedCustomer != null) {
                int customerId = Integer.parseInt(selectedCustomer.split(" - ")[0]);
                ResultSet rs = getCustomerPurchases(customerId);
                try {
                    if (rs != null && rs.next()) {
                        // Reset the cursor to before the first row
                        rs.beforeFirst();
                        JPanel dataPanel = createDataPanel(rs, "Purchases for " + selectedCustomer);
                        mainPanel.add(dataPanel, "customerPurchases");
                        cardLayout.show(mainPanel, "customerPurchases");
                    } else {
                        JOptionPane.showMessageDialog(this, "This customer has no purchase history.", 
                                                    "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error retrieving purchase history: " + ex.getMessage(), 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add components to the content panel with proper spacing
        contentPanel.add(customerComboBox, gbc);
        
        gbc.insets = new Insets(30, 0, 10, 0);
        contentPanel.add(proceedBtn, gbc);
        
        // Add the content panel to the center
        panel.add(contentPanel, BorderLayout.CENTER);

        // Back button at the bottom
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.WHITE);
        southPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton backBtn = new JButton(prevText);
        backBtn.setFont(font);
        backBtn.setBackground(Color.decode("#880808"));
        backBtn.setForeground(Color.WHITE);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.setPreferredSize(new Dimension(200, 40));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "mainMenu"));

        southPanel.add(backBtn);
        panel.add(southPanel, BorderLayout.SOUTH);

        // Add the panel to the main panel and show it
        mainPanel.add(panel, "selectCustomer");
        cardLayout.show(mainPanel, "selectCustomer");
    }

    private ResultSet getAllCustomers() {
        return executeQuery(
            "SELECT c.customer_id, c.first_name, c.last_name, c.email, m.gender, m.date_registered" +
            "\nFROM Customer c\nLEFT JOIN Member m ON c.customer_id = m.customer_id" + 
            "\nORDER BY c.last_name, c.first_name;"
        );
    }

    private ResultSet getCustomerPurchases(int customerId) {
        String query = 
            "SELECT s.sale_date, p.product_name, si.quantity_ordered, si.unit_price, " +
            "(si.quantity_ordered * si.unit_price) AS SUBTOTAL " +
            "\nFROM Sales s " +
            "\nJOIN Salesitems si ON s.sales_id = si.sales_id " +
            "\nJOIN Product p ON si.product_id = p.product_id " +
            "\nWHERE s.customer_id = " + customerId +
            "\n ORDER BY s.sale_date DESC;";
        return executeQuery(query);
    }

    private ResultSet executeQuery(String query) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}