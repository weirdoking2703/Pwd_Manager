import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class login extends JFrame implements ActionListener {

    ImageIcon icon = new ImageIcon("D:/AK/Programs/Projects/JDBC/Icon/log.jpg");

    private JPanel log;
    public JTextField textField;
    private JPasswordField pwd;
    private JLabel msgLabel;
    public String username;

    private String jdbcURL = "jdbc:mysql://localhost:3306/pwd_mng";
    private String dbUsername = "root";
    private String dbPassword = "@#Abhi2703#@";
    private Connection connection;


    public login() {
        establishDBConnection();
        setFrameProperties();
        setUpBackgroundPanel("D:/AK/Programs/Projects/JDBC/Icon/pwd.jpg");
        setIconImage(icon.getImage());
        setVisible(true);
        username = "";
    }

    private void setFrameProperties() {
        setTitle("Login Page");
        setBounds(350, 200, 720, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void setUpBackgroundPanel(String imagePath) {
        BackgroundPanel backgroundPanel = new BackgroundPanel(imagePath);
        backgroundPanel.setLayout(null);

        log = new JPanel();
        log.setLayout(null);
        log.setBounds(200, 115, 300, 200);
        log.setBackground(new Color(110, 99, 110, 180));

        initializeUIComponents();

        backgroundPanel.add(log);
        setContentPane(backgroundPanel);
    }

    private void initializeUIComponents() {
        addTitleLabel();
        addLabelAndField("USERNAME", 20, 50, textField = new JTextField());
        addLabelAndField("PASSWORD", 20, 90, pwd = new JPasswordField());
        addButtonsAndMessage();
    }

    private void addTitleLabel() {
        JLabel titleLabel = new JLabel("Login / Sign-Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(80, 10, 250, 30);
        log.add(titleLabel);
    }

    private void addLabelAndField(String labelText, int x, int y, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 100, 30);
        log.add(label);

        textField.setBorder(null);
        textField.setBackground(new Color(255, 255, 255, 180));
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBounds(120, y, 150, 30);
        log.add(textField);
    }

    private void addButtonsAndMessage() {
        log.add(logiButton("LOGIN", 40, 140, new Color(100, 30, 180)));
        log.add(regiButton("REGISTER", 150, 140, new Color(244, 154, 6)));

        msgLabel = new JLabel();
        msgLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        msgLabel.setForeground(Color.WHITE);
        msgLabel.setBounds(70, 220, 200, 30);
        log.add(msgLabel);
    }

    private JButton logiButton(String text, int x, int y, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBounds(x, y, 95, 30);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.addActionListener(this);
        return button;
    }

    private JButton regiButton(String text, int x, int y, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBounds(x, y, 110, 30);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.addActionListener(this);
        return button;
    }

    private void establishDBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
            System.exit(1);
        }
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("LOGIN")){
            handleLogin();
        }
        else if (action.equals("REGISTER")) {
            new signup();
            dispose();
        }
    }

    public void handleLogin() {
        username = textField.getText();
        String password = new String(pwd.getPassword());
        encode enc = new encode();
        String pass = enc.encode(password);

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Fill In All Fields.");
        }
        else if (authenticateUser(username, pass)) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            try {
                BufferedWriter wri = new BufferedWriter(new FileWriter("temp.txt"));
                wri.write(username);
                wri.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            new accs();
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password");
        }
    }

    private boolean authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE us_ue = ? AND us_pwd = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            return false;
        }
    }

//    public static void main(String[] args) {
//        new login();
//    }
}
