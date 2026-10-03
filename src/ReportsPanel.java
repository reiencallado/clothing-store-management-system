import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class ReportsPanel extends JPanel {
    final public String opening = "Reports Management";
    final public String b1Text = "Monthly Sales Report";
    final public String b2Text = "Sales Representative Report";
    final public String b3Text = "Product Performance Report";
    final public String b4Text = "Revenue per Branch Report";
    final public String prevText = "Back";

    final Font font = new Font("Arial", Font.PLAIN, 20);
    final Font titleFont = new Font("Arial", Font.BOLD, 25);
    final Dimension buttonSize = new Dimension(350, 50);

    private DisplayData displayData = new DisplayData();
    private ClothingStoreApp mainApp;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public ReportsPanel(ClothingStoreApp app) {
        this.mainApp = app;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Create a panel with CardLayout to switch between different report views
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        
        // Create and add the reports menu panel
        JPanel reportsMenuPanel = createReportsMenuPanel();
        mainPanel.add(reportsMenuPanel, "reportsMenu");
        
        // Add the main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
        
        // Show the reports menu initially
        cardLayout.show(mainPanel, "reportsMenu");
    }

    private JPanel createReportsMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLbl = new JLabel(opening, SwingConstants.CENTER);
        titleLbl.setFont(titleFont);

        JButton btn1 = new JButton(b1Text);
        JButton btn2 = new JButton(b2Text);
        JButton btn3 = new JButton(b3Text);
        JButton btn4 = new JButton(b4Text);

        btn1.setFont(font);
        btn2.setFont(font);
        btn3.setFont(font);
        btn4.setFont(font);

        btn1.setPreferredSize(buttonSize);
        btn2.setPreferredSize(buttonSize);
        btn3.setPreferredSize(buttonSize);
        btn4.setPreferredSize(buttonSize);

        JButton prevBtn = new JButton(prevText);
        prevBtn.setFont(font);
        prevBtn.setBackground(Color.decode("#880808"));
        prevBtn.setForeground(Color.WHITE);
        prevBtn.setOpaque(true);
        prevBtn.setBorderPainted(false);

        btn1.addActionListener(e -> showMonthlySalesReport());
        btn2.addActionListener(e -> showSalesRepReport());
        btn3.addActionListener(e -> showProductPerformanceReport());
        btn4.addActionListener(e -> showRevenueBranchReport());
        prevBtn.addActionListener(e -> mainApp.showPanel("MainMenu"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        panel.add(titleLbl, gbc);

        gbc.gridy = 2;
        panel.add(btn1, gbc);

        gbc.gridy = 3;
        panel.add(btn2, gbc);

        gbc.gridy = 4;
        panel.add(btn3, gbc);

        gbc.gridy = 5;
        panel.add(btn4, gbc);

        gbc.gridy = 7;
        panel.add(prevBtn, gbc);

        return panel;
    }

    private JPanel createDataPanel(ResultSet rs, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel dataTitle = new JLabel(title, SwingConstants.CENTER);
        dataTitle.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(dataTitle, BorderLayout.NORTH);
        
        JTable table = null;
        try {
            if (rs != null) {
                // Create a DefaultTableModel to hold the data
                DefaultTableModel model = new DefaultTableModel();
                
                // Get metadata
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                // Add column headers
                for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnLabel(i));
                }
                
                // Add data rows
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i-1] = rs.getObject(i);
                    }
                    model.addRow(row);
                }
                
                if (hasData) {
                    // Create table with data
                    table = new JTable(model);
                } else {
                    // No data found
                    DefaultTableModel emptyModel = new DefaultTableModel();
                    emptyModel.addColumn("Message");
                    emptyModel.addRow(new Object[]{"No data available for the selected period"});
                    table = new JTable(emptyModel);
                }
            } else {
                // Null result set
                DefaultTableModel emptyModel = new DefaultTableModel();
                emptyModel.addColumn("Message");
                emptyModel.addRow(new Object[]{"No data available for the selected period"});
                table = new JTable(emptyModel);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in createDataPanel: " + e.getMessage());
            e.printStackTrace();
            // Create empty table with error message
            DefaultTableModel errorModel = new DefaultTableModel();
            errorModel.addColumn("Error");
            errorModel.addRow(new Object[]{"Error retrieving data: " + e.getMessage()});
            table = new JTable(errorModel);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back");
        backButton.setFont(font);
        backButton.setBackground(Color.decode("#880808"));
        backButton.setForeground(Color.WHITE);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "reportsMenu"));
        JPanel backPanel = new JPanel();
        backPanel.add(backButton);
        panel.add(backPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createDataPanelWithTable(JTable table, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel dataTitle = new JLabel(title, SwingConstants.CENTER);
        dataTitle.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(dataTitle, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back");
        backButton.setFont(font);
        backButton.setBackground(Color.decode("#880808"));
        backButton.setForeground(Color.WHITE);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "reportsMenu"));
        JPanel backPanel = new JPanel();
        backPanel.add(backButton);
        panel.add(backPanel, BorderLayout.SOUTH);
        
        return panel;
    }


    // Monthly Sales Report
    private void showMonthlySalesReport() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Monthly Sales Report", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Month selection
        JLabel monthLabel = new JLabel("Select Month:");
        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthCombo = new JComboBox<>(months);
        monthCombo.setSelectedIndex(LocalDate.now().getMonthValue() - 1);

        // Year selection
        JLabel yearLabel = new JLabel("Select Year:");
        Integer[] years = {2023, 2024, 2025};
        JComboBox<Integer> yearCombo = new JComboBox<>(years);
        yearCombo.setSelectedItem(LocalDate.now().getYear());

        JButton generateBtn = new JButton("Generate Report");
        generateBtn.setFont(font);
        generateBtn.setBackground(Color.decode("#008000"));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setOpaque(true);
        generateBtn.setBorderPainted(false);

        generateBtn.addActionListener(e -> {
            int month = monthCombo.getSelectedIndex() + 1;
            int year = (Integer) yearCombo.getSelectedItem();
            
            JTable resultTable = generateMonthlySalesReportTable(month, year);
            JPanel dataPanel = createDataPanelWithTable(resultTable, 
                "Monthly Sales Report - " + monthCombo.getSelectedItem() + " " + year);
            mainPanel.add(dataPanel, "monthlySalesData");
            cardLayout.show(mainPanel, "monthlySalesData");
        });

        centerPanel.add(monthLabel);
        centerPanel.add(monthCombo);
        centerPanel.add(yearLabel);
        centerPanel.add(yearCombo);
        centerPanel.add(new JLabel(""));
        centerPanel.add(generateBtn);
        
        panel.add(centerPanel, BorderLayout.CENTER);

        JButton backBtn = new JButton(prevText);
        backBtn.setFont(font);
        backBtn.setBackground(Color.decode("#880808"));
        backBtn.setForeground(Color.WHITE);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "reportsMenu"));
        
        JPanel southPanel = new JPanel();
        southPanel.add(backBtn);
        panel.add(southPanel, BorderLayout.SOUTH);

        mainPanel.add(panel, "monthlySalesReport");
        cardLayout.show(mainPanel, "monthlySalesReport");
    }

    private JTable generateMonthlySalesReportTable(int month, int year) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                System.err.println("Database connection is null");
                return createErrorTable("Database connection failed");
            }
            
            // Create table model with column headers
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Day");
            model.addColumn("Category");
            model.addColumn("Total Quantity");
            model.addColumn("Total Amount");
            
            String query = "SELECT DAY(s.sale_date) as day, p.category, " +
                          "SUM(si.quantity_ordered) as total_quantity, " +
                          "SUM(si.quantity_ordered * si.unit_price) as total_amount " +
                          "FROM Sales s " +
                          "JOIN SalesItems si ON si.sales_id = s.sales_id " +
                          "JOIN Product p ON si.product_id = p.product_id " +
                          "WHERE MONTH(s.sale_date) = " + month + " AND YEAR(s.sale_date) = " + year + " " +
                          "GROUP BY DAY(s.sale_date), p.category " +
                          "ORDER BY day, p.category";
            
            System.out.println("Executing query: " + query);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            
            // Add data rows from the query result
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int day = rs.getInt("day");
                String category = rs.getString("category");
                int quantity = rs.getInt("total_quantity");
                double amount = rs.getDouble("total_amount");
                model.addRow(new Object[]{day, category, quantity, amount});
            }
            
            System.out.println("Query executed successfully. Has data: " + hasData);
            
            // If no data found, add a message row
            if (!hasData) {
                // Check if there's any data in the Sales table for the selected month/year
                String checkQuery = "SELECT COUNT(*) FROM Sales WHERE MONTH(sale_date) = " + month + " AND YEAR(sale_date) = " + year;
                Statement checkStmt = conn.createStatement();
                ResultSet checkRs = checkStmt.executeQuery(checkQuery);
                checkRs.next();
                int count = checkRs.getInt(1);
                checkRs.close();
                checkStmt.close();
                
                if (count == 0) {
                    // No sales data for this period
                    model.addRow(new Object[]{0, "No sales data for " + month + "/" + year, 0, 0.0});
                } else {
                    // There are sales but no category data
                    model.addRow(new Object[]{0, "Data issue: Sales exist but no category data", 0, 0.0});
                }
            }
            
            return new JTable(model);
        } catch (SQLException e) {
            System.err.println("SQL Error in generateMonthlySalesReport: " + e.getMessage());
            e.printStackTrace();
            
            // Create error message table
            DefaultTableModel errorModel = new DefaultTableModel();
            errorModel.addColumn("Error");
            errorModel.addColumn("Message");
            errorModel.addColumn("");
            errorModel.addColumn("");
            errorModel.addRow(new Object[]{"Database Error", e.getMessage(), "", ""});
            return new JTable(errorModel);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private JTable createErrorTable(String message) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Message");
        model.addRow(new Object[]{message});
        return new JTable(model);
    }
    // }

    // Sales per Sales Representative Report
    private void showSalesRepReport() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Sales per Sales Representative Report", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Month selection
        JLabel monthLabel = new JLabel("Select Month:");
        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthCombo = new JComboBox<>(months);
        monthCombo.setSelectedIndex(LocalDate.now().getMonthValue() - 1);

        JButton generateBtn = new JButton("Generate Report");
        generateBtn.setFont(font);
        generateBtn.setBackground(Color.decode("#008000"));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setOpaque(true);
        generateBtn.setBorderPainted(false);

        generateBtn.addActionListener(e -> {
            int month = monthCombo.getSelectedIndex() + 1;
            int year = LocalDate.now().getYear();
            
            JTable resultTable = generateSalesRepReportTable(month, year);
            JPanel dataPanel = createDataPanelWithTable(resultTable, 
                "Sales per Rep Report - " + monthCombo.getSelectedItem() + " " + year);
            mainPanel.add(dataPanel, "salesRepData");
            cardLayout.show(mainPanel, "salesRepData");
        });

        centerPanel.add(monthLabel);
        centerPanel.add(monthCombo);
        centerPanel.add(new JLabel(""));
        centerPanel.add(generateBtn);
        
        panel.add(centerPanel, BorderLayout.CENTER);

        JButton backBtn = new JButton(prevText);
        backBtn.setFont(font);
        backBtn.setBackground(Color.decode("#880808"));
        backBtn.setForeground(Color.WHITE);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "reportsMenu"));
        
        JPanel southPanel = new JPanel();
        southPanel.add(backBtn);
        panel.add(southPanel, BorderLayout.SOUTH);

        mainPanel.add(panel, "salesRepReport");
        cardLayout.show(mainPanel, "salesRepReport");
    }

    private JTable generateSalesRepReportTable(int month, int year) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                System.err.println("Database connection is null");
                return createErrorTable("Database connection failed");
            }
            
            // Create table model with column headers
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Sales Rep");
            model.addColumn("Branch");
            model.addColumn("Total Sales");
            
            // Query to get sales rep data from the database
            String query = "SELECT sr.name AS sales_rep, b.branch_name AS branch, " +
                          "SUM(s.total_amount) AS total_sales " +
                          "FROM Sales s " +
                          "JOIN SalesRep sr ON s.sales_rep_id = sr.sales_rep_id " +
                          "JOIN Branch b ON s.branch_code = b.branch_code " +
                          "WHERE MONTH(s.sale_date) = " + month + " AND YEAR(s.sale_date) = " + year + " " +
                          "GROUP BY sr.name, b.branch_name " +
                          "ORDER BY b.branch_name, total_sales DESC";
            
            System.out.println("Executing query: " + query);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            
            // Add data rows from the query result
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                String salesRep = rs.getString("sales_rep");
                String branch = rs.getString("branch");
                double totalSales = rs.getDouble("total_sales");
                model.addRow(new Object[]{salesRep, branch, totalSales});
            }
            
            System.out.println("Query executed successfully. Has data: " + hasData);
            
            // If no data found, add a message row
            if (!hasData) {
                // Check if there's any data in the Sales table for the selected month/year
                String checkQuery = "SELECT COUNT(*) FROM Sales WHERE MONTH(sale_date) = " + month + " AND YEAR(sale_date) = " + year;
                Statement checkStmt = conn.createStatement();
                ResultSet checkRs = checkStmt.executeQuery(checkQuery);
                checkRs.next();
                int count = checkRs.getInt(1);
                checkRs.close();
                checkStmt.close();
                
                if (count == 0) {
                    // No sales data for this period
                    model.addRow(new Object[]{"No sales data for " + month + "/" + year, "", ""});
                } else {
                    // There are sales but no sales rep data
                    model.addRow(new Object[]{"Data issue: Sales exist but no sales rep data", "", ""});
                }
            }
            
            return new JTable(model);
        } catch (SQLException e) {
            System.err.println("SQL Error in generateSalesRepReport: " + e.getMessage());
            e.printStackTrace();
            return createErrorTable("Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Product Performance Report
    private void showProductPerformanceReport() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Product Performance Report", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Quarter selection
        JLabel quarterLabel = new JLabel("Select Quarter:");
        String[] quarters = {"Q1 (Jan-Mar)", "Q2 (Apr-Jun)", "Q3 (Jul-Sep)", "Q4 (Oct-Dec)"};
        JComboBox<String> quarterCombo = new JComboBox<>(quarters);
        
        // Calculate current quarter
        int currentMonth = LocalDate.now().getMonthValue();
        int currentQuarter = (currentMonth - 1) / 3;
        quarterCombo.setSelectedIndex(currentQuarter);

        JButton generateBtn = new JButton("Generate Report");
        generateBtn.setFont(font);
        generateBtn.setBackground(Color.decode("#008000"));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setOpaque(true);
        generateBtn.setBorderPainted(false);

        generateBtn.addActionListener(e -> {
            int quarter = quarterCombo.getSelectedIndex() + 1;
            
            ResultSet rs = generateProductPerformanceReport(quarter);
            if (rs != null) {
                JPanel dataPanel = createDataPanel(rs, 
                    "Product Performance Report - " + quarterCombo.getSelectedItem());
                mainPanel.add(dataPanel, "productPerfData");
                cardLayout.show(mainPanel, "productPerfData");
            } else {
                JOptionPane.showMessageDialog(this, "No data available for the selected period.", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        centerPanel.add(quarterLabel);
        centerPanel.add(quarterCombo);
        centerPanel.add(new JLabel(""));
        centerPanel.add(generateBtn);
        
        panel.add(centerPanel, BorderLayout.CENTER);

        JButton backBtn = new JButton(prevText);
        backBtn.setFont(font);
        backBtn.setBackground(Color.decode("#880808"));
        backBtn.setForeground(Color.WHITE);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "reportsMenu"));
        
        JPanel southPanel = new JPanel();
        southPanel.add(backBtn);
        panel.add(southPanel, BorderLayout.SOUTH);

        mainPanel.add(panel, "productPerfReport");
        cardLayout.show(mainPanel, "productPerfReport");
    }

    private ResultSet generateProductPerformanceReport(int quarter) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) {
                System.err.println("Database connection is null");
                return null;
            }
            
            // Calculate start and end dates for the quarter
            int currentYear = LocalDate.now().getYear();
            int startMonth = (quarter - 1) * 3 + 1;
            int endMonth = quarter * 3;
            
            String startDate = currentYear + "-" + String.format("%02d", startMonth) + "-01";
            String endDate = currentYear + "-" + String.format("%02d", endMonth) + "-31";
            
            String query = "SELECT " +
                "p.product_name AS product, " +
                "COALESCE(SUM(si.quantity_ordered), 0) AS quantity_sold, " +
                "COALESCE(SUM(si.quantity_ordered * si.unit_price), 0) AS revenue, " +
                "COALESCE(AVG(si.unit_price), 0) AS avg_price " +
                "FROM Product p " +
                "LEFT JOIN SalesItems si ON p.product_id = si.product_id " +
                "LEFT JOIN Sales s ON si.sales_id = s.sales_id AND s.sale_date BETWEEN ? AND ? " +
                "GROUP BY p.product_name " +
                "ORDER BY revenue DESC";
            
            System.out.println("Executing query: " + query);
            System.out.println("Date range: " + startDate + " to " + endDate);
            
            PreparedStatement stmt = conn.prepareStatement(query, 
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Query executed successfully");
            return rs;
        } catch (SQLException e) {
            System.err.println("SQL Error in generateProductPerformanceReport: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Revenue per Branch Report
    private void showRevenueBranchReport() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Revenue per Branch Report", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Month selection
        JLabel monthLabel = new JLabel("Select Month:");
        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthCombo = new JComboBox<>(months);
        monthCombo.setSelectedIndex(LocalDate.now().getMonthValue() - 1);

        // Year selection
        JLabel yearLabel = new JLabel("Select Year:");
        Integer[] years = {2023, 2024, 2025};
        JComboBox<Integer> yearCombo = new JComboBox<>(years);
        yearCombo.setSelectedItem(LocalDate.now().getYear());

        JButton generateBtn = new JButton("Generate Report");
        generateBtn.setFont(font);
        generateBtn.setBackground(Color.decode("#008000"));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setOpaque(true);
        generateBtn.setBorderPainted(false);

        generateBtn.addActionListener(e -> {
            int month = monthCombo.getSelectedIndex() + 1;
            int year = (Integer) yearCombo.getSelectedItem();
            
            ResultSet rs = generateRevenueBranchReport(month, year);
            if (rs != null) {
                JPanel dataPanel = createDataPanel(rs, 
                    "Revenue per Branch - " + monthCombo.getSelectedItem() + " " + year);
                mainPanel.add(dataPanel, "revenueBranchData");
                cardLayout.show(mainPanel, "revenueBranchData");
            } else {
                JOptionPane.showMessageDialog(this, "No data available for the selected period.", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        centerPanel.add(monthLabel);
        centerPanel.add(monthCombo);
        centerPanel.add(yearLabel);
        centerPanel.add(yearCombo);
        centerPanel.add(new JLabel(""));
        centerPanel.add(generateBtn);
        
        panel.add(centerPanel, BorderLayout.CENTER);

        JButton backBtn = new JButton(prevText);
        backBtn.setFont(font);
        backBtn.setBackground(Color.decode("#880808"));
        backBtn.setForeground(Color.WHITE);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "reportsMenu"));
        
        JPanel southPanel = new JPanel();
        southPanel.add(backBtn);
        panel.add(southPanel, BorderLayout.SOUTH);

        mainPanel.add(panel, "revenueBranchReport");
        cardLayout.show(mainPanel, "revenueBranchReport");
    }

    private ResultSet generateRevenueBranchReport(int month, int year) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) {
                System.err.println("Database connection is null");
                return null;
            }
            
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            // Query actual branch data and sales from the database
            String query = "SELECT b.branch_code, b.branch_name, b.location, " +
                          "IFNULL((SELECT SUM(s.total_amount) " +
                            "FROM Sales s " +
                            "WHERE s.branch_code = b.branch_code " +
                            "AND MONTH(s.sale_date) = " + month + " " +
                            "AND YEAR(s.sale_date) = " + year + "), 0) AS revenue " +
                          "FROM Branch b " +
                          "ORDER BY revenue DESC";
            
            System.out.println("Executing query: " + query);
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Query executed successfully");
            return rs;
        } catch (SQLException e) {
            System.err.println("SQL Error in generateRevenueBranchReport: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
