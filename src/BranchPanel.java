import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BranchPanel extends JPanel {
    // Using centralized database connection

    final public String opening = "What do you want to do?";
    final public String b1Text = "Check Branch Records";
    final public String b2Text = "Branch & Active Sales Reps";
    final public String b3Text = "Transfer Inventory";
    final public String prevText = "Back";

    final Font font = new Font("Arial", Font.PLAIN, 20);
    final Dimension buttonSize = new Dimension(350, 50); // width, height

    private JComboBox<String> sourceBranch, destBranch, productBox;
    private JTextField quantityField;
    private JTextField reasonField;
    private DisplayData displayData = new DisplayData();
    private ClothingStoreApp mainApp;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public BranchPanel(ClothingStoreApp app) {
        this.mainApp = app;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Create a panel with CardLayout to switch between different branch views
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        
        // Create and add the branch menu panel
        JPanel branchMenuPanel = createBranchMenuPanel();
        mainPanel.add(branchMenuPanel, "branchMenu");
        
        // Add the main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
        
        // Show the branch menu initially
        cardLayout.show(mainPanel, "branchMenu");
    }

    private JPanel createBranchMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(opening, SwingConstants.CENTER);
        titleLabel.setFont(font);

        JButton btn1 = new JButton(b1Text);
        JButton btn2 = new JButton(b2Text);
        JButton btn3 = new JButton(b3Text);

        btn1.setFont(font);
        btn2.setFont(font);
        btn3.setFont(font);

        btn1.setPreferredSize(buttonSize);
        btn2.setPreferredSize(buttonSize);
        btn3.setPreferredSize(buttonSize);

        JButton prevBtn = new JButton(prevText);
        prevBtn.setFont(font);
        prevBtn.setBackground(Color.decode("#880808"));
        prevBtn.setForeground(Color.WHITE);
        prevBtn.setOpaque(true);
        prevBtn.setBorderPainted(false);

        btn1.addActionListener(e -> {
            ResultSet rs = getBranchResultSet();
            if (rs != null) {
                JPanel dataPanel = createDataPanel(rs, "Branch Records");
                mainPanel.add(dataPanel, "branchRecords");
                cardLayout.show(mainPanel, "branchRecords");
            } else {
                JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn2.addActionListener(e -> {
            ResultSet rs = BranchAndActiveSalesRep();
            if (rs != null) {
                JPanel dataPanel = createDataPanel(rs, "Branch & Active Sales Reps");
                mainPanel.add(dataPanel, "branchSalesReps");
                cardLayout.show(mainPanel, "branchSalesReps");
            } else {
                JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn3.addActionListener(e -> showStockTransfer());
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
        panel.add(btn3, gbc);

        gbc.gridy = 4;
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
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "branchMenu"));
        JPanel backPanel = new JPanel();
        backPanel.add(backButton);
        panel.add(backPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private void showStockTransfer() {
        JPanel transferPanel = new JPanel(new GridBagLayout());
        transferPanel.setBackground(Color.WHITE);
        
        sourceBranch = new JComboBox<>(displayData.getComboBoxData("SELECT branch_name FROM Branch ORDER BY branch_name"));
        destBranch = new JComboBox<>(displayData.getComboBoxData("SELECT branch_name FROM Branch ORDER BY branch_name"));
        productBox = new JComboBox<>(displayData.getComboBoxData("SELECT product_name FROM Product ORDER BY product_name"));
        quantityField = new JTextField();
        reasonField = new JTextField();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel transferTitle = new JLabel("Stock Transfer", SwingConstants.CENTER);
        transferTitle.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        transferPanel.add(transferTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; transferPanel.add(new JLabel("Source Branch:"), gbc);
        gbc.gridx = 0; gbc.gridy = 2; transferPanel.add(new JLabel("Destination Branch:"), gbc);
        gbc.gridx = 0; gbc.gridy = 3; transferPanel.add(new JLabel("Product:"), gbc);
        gbc.gridx = 0; gbc.gridy = 4; transferPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 0; gbc.gridy = 5; transferPanel.add(new JLabel("Reason:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; transferPanel.add(sourceBranch, gbc);
        gbc.gridx = 1; gbc.gridy = 2; transferPanel.add(destBranch, gbc);
        gbc.gridx = 1; gbc.gridy = 3; transferPanel.add(productBox, gbc);
        gbc.gridx = 1; gbc.gridy = 4; transferPanel.add(quantityField, gbc);
        gbc.gridx = 1; gbc.gridy = 5; transferPanel.add(reasonField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton submitBtn = new JButton("Submit");
        JButton backBtn = new JButton("Back");
        
        submitBtn.addActionListener(e -> {
            try {
                String src = (String) sourceBranch.getSelectedItem();
                String dst = (String) destBranch.getSelectedItem();
                String product = (String) productBox.getSelectedItem();
                int qty = Integer.parseInt(quantityField.getText());
                String reason = (String) reasonField.getText();
                
                if (src.equals(dst)) {
                    JOptionPane.showMessageDialog(this, "Source and Destination branches cannot be the same.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (qty <= 0) throw new NumberFormatException();

                if (transferStock(src, dst, product, qty, reason)) {
                    JOptionPane.showMessageDialog(this, "Stock transferred successfully!");
                    cardLayout.show(mainPanel, "branchMenu");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "branchMenu"));
        
        buttonPanel.add(submitBtn);
        buttonPanel.add(backBtn);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        transferPanel.add(buttonPanel, gbc);

        mainPanel.add(transferPanel, "stockTransfer");
        cardLayout.show(mainPanel, "stockTransfer");
    }

    private ResultSet getBranchResultSet() {
        return executeQuery("SELECT * FROM Branch");
    }

    private ResultSet BranchAndActiveSalesRep() {
        return executeQuery("SELECT b.branch_name AS BranchName, b.location AS Location, " +
                "s.name AS SalesRep, s.active_status AS ActiveStatus, " +
                "b.contact_number AS ContactBranch " +
                "FROM Branch b " +
                "LEFT JOIN SalesRep s ON b.branch_code = s.branch_code AND s.active_status = 'TRUE' " +
                "ORDER BY s.name");
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
    
    private boolean transferStock(String sourceBranchName, String destBranchName, String productName, int quantity, String reason) {
        String getBranchIdQuery = "SELECT branch_code FROM Branch WHERE branch_name = ?";
        String getProductIdQuery = "SELECT product_id FROM Product WHERE product_name = ?";
        String checkStockQuery = "SELECT quantity FROM Inventory WHERE branch_code = ? AND product_id = ?";
        String updateSourceQuery = "UPDATE Inventory SET quantity = quantity - ? WHERE branch_code = ? AND product_id = ?";
        String updateDestQuery = "INSERT INTO Inventory (branch_code, product_id, quantity) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE quantity = quantity + ?";
        String updateStockTransferQuery = "INSERT INTO StockTransfer (transfer_id, product_id, source_branch_code, dest_branch_code, quantity_transferred, transfer_date, reason) " +
                                          "VALUES (?, ?, ?, ?, ?, CURDATE(), ?)";

        try (Connection conn =  DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement getSourceIdStmt = conn.prepareStatement(getBranchIdQuery);
                 PreparedStatement getDestIdStmt = conn.prepareStatement(getBranchIdQuery);
                 PreparedStatement getProdIdStmt = conn.prepareStatement(getProductIdQuery)) {

                getSourceIdStmt.setString(1, sourceBranchName);
                String sourceBranchId = null;
                try (ResultSet rs = getSourceIdStmt.executeQuery()) { if (rs.next()) sourceBranchId = rs.getString(1); }

                getDestIdStmt.setString(1, destBranchName);
                String destBranchId = null;
                try (ResultSet rs = getDestIdStmt.executeQuery()) { if (rs.next()) destBranchId = rs.getString(1); }

                getProdIdStmt.setString(1, productName);
                int productId = -1;
                try (ResultSet rs = getProdIdStmt.executeQuery()) { if (rs.next()) productId = rs.getInt(1); }

                if (sourceBranchId == null || destBranchId == null || productId == -1) {
                    JOptionPane.showMessageDialog(this, "Invalid branch or product selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                // Check stock
                try (PreparedStatement checkStockStmt = conn.prepareStatement(checkStockQuery)) {
                    checkStockStmt.setString(1, sourceBranchId);
                    checkStockStmt.setInt(2, productId);
                    try (ResultSet rs = checkStockStmt.executeQuery()) {
                        if (!rs.next() || rs.getInt("quantity") < quantity) {
                            JOptionPane.showMessageDialog(this, "Insufficient stock in the source branch.", "Error", JOptionPane.ERROR_MESSAGE);
                            conn.rollback();
                            return false;
                        }
                    }
                }

                // Perform update
                try (PreparedStatement updateSourceStmt = conn.prepareStatement(updateSourceQuery);
                     PreparedStatement updateDestStmt = conn.prepareStatement(updateDestQuery);
                     PreparedStatement updateStockTransferStmt = conn.prepareStatement(updateStockTransferQuery)) {

                    updateSourceStmt.setInt(1, quantity);
                    updateSourceStmt.setString(2, sourceBranchId);
                    updateSourceStmt.setInt(3, productId);
                    updateSourceStmt.executeUpdate();

                    updateDestStmt.setString(1, destBranchId);
                    updateDestStmt.setInt(2, productId);
                    updateDestStmt.setInt(3, quantity);
                    updateDestStmt.setInt(4, quantity);
                    updateDestStmt.executeUpdate();

                    int transferId;
                    String getMaxTransferIdQuery = "SELECT COALESCE(MAX(transfer_id), 0) + 1 AS next_id FROM StockTransfer";
                    try (Statement idStmt = conn.createStatement();
                        ResultSet idSt = idStmt.executeQuery(getMaxTransferIdQuery)) {
                        idSt.next();
                        transferId = idSt.getInt("next_id");
                    }

                    updateStockTransferStmt.setInt(1, transferId);
                    updateStockTransferStmt.setInt(2, productId);
                    updateStockTransferStmt.setString(3, sourceBranchId);
                    updateStockTransferStmt.setString(4, destBranchId);
                    updateStockTransferStmt.setInt(5, quantity);
                    updateStockTransferStmt.setString(6, reason);
                    updateStockTransferStmt.executeUpdate();

                    conn.commit(); // Commit transaction
                    return true;
                }

            } catch (SQLException e) {
                conn.rollback(); // Rollback on error
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred during the transfer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
