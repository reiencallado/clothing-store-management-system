import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisplayData {
    final Font titleFont = new Font("Arial", Font.BOLD, 25);
    
    public void displayData(JFrame parent, ResultSet rs, String titleText, ActionListener backAction) {
        parent.getContentPane().removeAll();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel dataTitle = new JLabel(titleText, SwingConstants.CENTER);
        dataTitle.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(dataTitle, BorderLayout.NORTH);
        
        JTable table = createTableFromResultSet(rs);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(backAction);
        JPanel backPanel = new JPanel();
        backPanel.add(backButton);
        panel.add(backPanel, BorderLayout.SOUTH);
        
        parent.getContentPane().add(panel);
        parent.revalidate();
        parent.repaint();
    }
    
    public JTable createTableFromResultSet(ResultSet rs) {
        try {
            if (rs == null) {
                return new JTable(new Object[][]{{"", "No data available"}}, new Object[]{"", "Message"});
            }
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i-1] = metaData.getColumnLabel(i);
            }
            
            // Reset cursor position with rs.beforeFirst() before reading data
            rs.beforeFirst();
            
            List<Object[]> data = new ArrayList<>();
            boolean hasData = false;
            
            while (rs.next()) {
                hasData = true;
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i-1] = rs.getObject(i);
                }
                data.add(row);
            }
            
            // If no data was found, return a message table
            if (!hasData) {
                return new JTable(new Object[][]{{"", "No data available for the selected period"}}, 
                                 new Object[]{"", "Message"});
            }
            
            Object[][] dataArray = data.toArray(new Object[0][]);
            return new JTable(dataArray, columnNames);
        } catch (SQLException e) {
            e.printStackTrace();
            return new JTable();
        }
    }
    
    public void showStockTransfer(JFrame parent, JComboBox<String> sourceBranch, JComboBox<String> destBranch, 
                                 JComboBox<String> productBox, JTextField quantityField, 
                                 ActionListener submitAction, ActionListener backAction) {
        parent.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel transferTitle = new JLabel("Stock Transfer", SwingConstants.CENTER);
        transferTitle.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(transferTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Branch Code:"), gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Destination Branch:"), gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Product:"), gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Quantity:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; panel.add(sourceBranch, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(destBranch, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(productBox, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(quantityField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton submitBtn = new JButton("Submit");
        JButton backBtn = new JButton("Back");
        
        submitBtn.addActionListener(submitAction);
        backBtn.addActionListener(backAction);
        
        buttonPanel.add(submitBtn);
        buttonPanel.add(backBtn);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(buttonPanel, gbc);

        parent.getContentPane().add(panel);
        parent.revalidate();
        parent.repaint();
    }

    public String[] getComboBoxData(String query) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            List<String> dataList = new ArrayList<>();
            while (rs.next()) {
                dataList.add(rs.getString(1));
            }
            
            return dataList.toArray(new String[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public void showProcessReturn(JFrame parent, JComboBox<String> branchCode, JComboBox<String> saleDate, 
                                 JComboBox<String> returnItem, JTextField quantityField, JTextField reasonField, 
                                 ActionListener submitAction, ActionListener backAction) {
        parent.getContentPane().removeAll();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel returnTitle = new JLabel("Process Return", SwingConstants.CENTER);
        returnTitle.setFont(titleFont);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(returnTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Branch code:"), gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Sale date (YYYY-MM-DD): "), gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Item to return:"), gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Quantity to return :"), gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Reason :"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; panel.add(branchCode, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(saleDate, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(returnItem, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(quantityField, gbc);
        gbc.gridx = 1; gbc.gridy = 5; panel.add(reasonField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton submitBtn = new JButton("Submit Return");
        submitBtn.setBackground(Color.decode("#008000"));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setOpaque(true);
        submitBtn.setBorderPainted(false);

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(Color.decode("#880808"));
        backBtn.setForeground(Color.WHITE);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        
        submitBtn.addActionListener(submitAction);
        backBtn.addActionListener(backAction);
        
        buttonPanel.add(submitBtn);
        buttonPanel.add(backBtn);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(buttonPanel, gbc);

        parent.getContentPane().add(panel);
        parent.revalidate();
        parent.repaint();
    }

    public void showRestockProducts(JFrame parent, JComboBox<String> branchName, JComboBox<String> productName, 
                                    JComboBox<String> supplier, JTextField quantityField, JLabel costLabel, 
                                    ActionListener submitAction, ActionListener backAction) {
        parent.getContentPane().removeAll();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel returnTitle = new JLabel("Restock Product", SwingConstants.CENTER);
        returnTitle.setFont(titleFont);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(returnTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Branch Name:"), gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Product to Restock: "), gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Choose Supplier:"), gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Quantity to restock :"), gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Cost :"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; panel.add(branchName, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(productName, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(supplier, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(quantityField, gbc);
        gbc.gridx = 1; gbc.gridy = 5; panel.add(costLabel, gbc);

        JPanel buttonPanel = new JPanel();
        JButton submitBtn = new JButton("Submit Restock");
        submitBtn.setBackground(Color.decode("#008000"));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setOpaque(true);
        submitBtn.setBorderPainted(false);

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(Color.decode("#880808"));
        backBtn.setForeground(Color.WHITE);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        
        submitBtn.addActionListener(submitAction);
        backBtn.addActionListener(backAction);
        
        buttonPanel.add(submitBtn);
        buttonPanel.add(backBtn);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(buttonPanel, gbc);

        parent.getContentPane().add(panel);
        parent.revalidate();
        parent.repaint();
    }
}
