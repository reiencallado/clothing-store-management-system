import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SalesRepPanel extends JPanel {
    // Using centralized database connection

    final public String opening = "Sales Representative Records Management";
    final public String b1Text = "View Sales Representatives";
    final public String b2Text = "View Completed Sales by Rep";
    final public String prevText = "Back";

    final Font font = new Font("Arial", Font.PLAIN, 20);
    final Font titleFont = new Font("Arial", Font.BOLD, 25);
    final Dimension buttonSize = new Dimension(350, 50);

    private DisplayData displayData = new DisplayData();
    private ClothingStoreApp mainApp;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public SalesRepPanel(ClothingStoreApp app) {
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
            ResultSet rs = getSalesReps();
            if (rs != null) {
                JPanel dataPanel = createDataPanel(rs, "Sales Representatives");
                mainPanel.add(dataPanel, "salesReps");
                cardLayout.show(mainPanel, "salesReps");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load sales representatives.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn2.addActionListener(e -> showSelectSalesRepForSales());
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

    private void showSelectSalesRepForSales() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Select Sales Representative", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(110, 300, 110, 300));

        JComboBox<String> salesRepComboBox = new JComboBox<>(displayData.getComboBoxData("SELECT name FROM salesrep ORDER BY name"));

        JButton proceedBtn = new JButton("Proceed");
        proceedBtn.setFont(font);
        proceedBtn.setBackground(Color.decode("#008000"));
        proceedBtn.setForeground(Color.WHITE);
        proceedBtn.setOpaque(true);
        proceedBtn.setBorderPainted(false);

        proceedBtn.addActionListener(e -> {
            String selectedRep = (String) salesRepComboBox.getSelectedItem();
            if (selectedRep != null) {
                ResultSet rs = getCompletedSalesByRep(selectedRep);
                if (rs != null) {
                    JPanel dataPanel = createDataPanel(rs, "Completed Sales for " + selectedRep);
                    mainPanel.add(dataPanel, "salesByRep");
                    cardLayout.show(mainPanel, "salesByRep");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to load sales.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        centerPanel.add(salesRepComboBox);
        centerPanel.add(proceedBtn);
        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.setBackground(Color.WHITE);

        JButton backBtn = new JButton(prevText);
        backBtn.setFont(font);
        backBtn.setBackground(Color.decode("#880808"));
        backBtn.setForeground(Color.WHITE);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);

        southPanel.add(backBtn);
        panel.add(southPanel, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "mainMenu"));

        mainPanel.add(panel, "selectSalesRep");
        cardLayout.show(mainPanel, "selectSalesRep");
    }

    private ResultSet getSalesReps() {
        return executeQuery("SELECT * FROM salesrep");
    }

    private ResultSet getCompletedSalesByRep(String salesRepName) {
        String query = "SELECT s.sales_id, s.sale_date, s.total_amount, c.first_name, c.last_name " +
                      "FROM sales s " +
                      "JOIN customer c ON s.customer_id = c.customer_id " +
                      "JOIN salesrep sr ON s.sales_rep_id = sr.sales_rep_id " +
                      "WHERE sr.name = '" + salesRepName + "'";
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