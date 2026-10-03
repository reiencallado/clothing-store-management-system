import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    final public String opening = "Clothing Store Record Management System";
    final public String b1Text = "Product Record Management";
    final public String b2Text = "Customer Record Management";
    final public String b3Text = "Sales Rep Record Management";
    final public String b4Text = "Branch Record Management";
    final public String b5Text = "New Sales Transaction";
    final public String b6Text = "Reports";

    final Font font = new Font("Arial", Font.PLAIN, 20);
    final Font titleFont = new Font("Arial", Font.BOLD, 25);
    final Dimension buttonSize = new Dimension(350, 50);
    
    private ClothingStoreApp mainApp;

    public MainMenuPanel(ClothingStoreApp app) {
        this.mainApp = app;
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        
        initComponents();
    }

    private void initComponents() {
        JLabel titleLbl = new JLabel(opening, SwingConstants.CENTER);
        titleLbl.setFont(titleFont);

        JButton btn1 = new JButton(b1Text);
        JButton btn2 = new JButton(b2Text);
        JButton btn3 = new JButton(b3Text);
        JButton btn4 = new JButton(b4Text);
        JButton btn5 = new JButton(b5Text);
        JButton btn6 = new JButton(b6Text);

        btn1.setFont(font);
        btn2.setFont(font);
        btn3.setFont(font);
        btn4.setFont(font);
        btn5.setFont(font);
        btn6.setFont(font);

        btn1.setPreferredSize(buttonSize);
        btn2.setPreferredSize(buttonSize);
        btn3.setPreferredSize(buttonSize);
        btn4.setPreferredSize(buttonSize);
        btn5.setPreferredSize(buttonSize);
        btn6.setPreferredSize(buttonSize);

        btn1.addActionListener(e -> mainApp.showPanel("Product"));
        btn2.addActionListener(e -> mainApp.showPanel("Customer"));
        btn3.addActionListener(e -> mainApp.showPanel("SalesRep"));
        btn4.addActionListener(e -> mainApp.showPanel("Branch"));
        btn5.addActionListener(e -> mainApp.showPanel("SalesTransaction"));
        btn6.addActionListener(e -> mainApp.showPanel("Reports"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        add(titleLbl, gbc);

        gbc.gridy = 2;
        add(btn1, gbc);

        gbc.gridy = 3;
        add(btn2, gbc);

        gbc.gridy = 4;
        add(btn3, gbc);

        gbc.gridy = 5;
        add(btn4, gbc);
        
        gbc.gridy = 6;
        add(btn5, gbc);
        
        gbc.gridy = 7;
        add(btn6, gbc);
    }
}
