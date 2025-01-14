import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class signup extends JFrame implements ActionListener {

    ImageIcon icon = new ImageIcon("D:/AK/Programs/Projects/JDBC/Icon/log.jpg");
    JPanel panel = new CustomPanel(); // Use a custom panel with background
    JButton signUpButton;
    JTextField textField; // For username
    JPasswordField pwd;   // For password
    JLabel msgLabel;      // For displaying messages

    private String jdbcURL = "jdbc:mysql://localhost:3306/pwd_mng";
    private String dbUsername = "root";
    private String dbPassword = "@#Abhi2703#@";
    private Connection connection;

    public signup(){
        establishDBConnection();

        setTitle("Sign-Up Page");
        setLayout(null);
        setBounds(350, 200, 720, 480);
        setResizable(false);
        setIconImage(icon.getImage());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        panel.setLayout(null);
        panel.setBounds(0, 0, 720, 480); // Cover full window with background
        add(panel);

        // Adding input fields and labels
        addFormElements();

        // Adding buttons and message area
        addButtonsAndMessage();

        setVisible(true);
    }

    private void addFormElements() {
        // Label for Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(250, 150, 100, 25);
        panel.add(userLabel);

        // Username field
        textField = new JTextField();
        textField.setBounds(350, 150, 160, 25);
        panel.add(textField);

        // Label for Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(250, 190, 100, 25);
        panel.add(passLabel);

        // Password field
        pwd = new JPasswordField();
        pwd.setBounds(350, 190, 160, 25);
        panel.add(pwd);
    }

    private void addButtonsAndMessage() {
        // Sign-up button
        signUpButton = createButton("SIGN-UP", 300, 230, new Color(100, 30, 180));
        panel.add(signUpButton);

        // Message label to display errors or success messages
        msgLabel = new JLabel();
        msgLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        msgLabel.setForeground(Color.WHITE);
        msgLabel.setBounds(250, 270, 300, 30);
        panel.add(msgLabel);
    }

    private JButton createButton(String text, int x, int y, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBounds(x, y, 120, 30);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.addActionListener(this);
        return button;
    }

    private void establishDBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
            System.exit(1);
        }
    }

    public void handleSignup(){
        String username = textField.getText();
        String password = new String(pwd.getPassword());
        encode enc = new encode();
        String pass=enc.encode(password);
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            // Check if the username is already taken
            String checkQuery = "SELECT * FROM users WHERE us_ue = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username already exists.");
                return;
            }

            // Insert new user into the database
            String query = "INSERT INTO users (us_ue, us_pwd) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, pass);

            int result = statement.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Sign-Up Successful!");
                // Optionally, navigate to the login window
                dispose();
                new login();
            } else
            {
                JOptionPane.showMessageDialog(this, "Sign-Up Failed.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error During Sign-Up.");
            dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("SIGN-UP")) {
            handleSignup();
        }
    }

//    public static void main(String[] args) {
//        new signup();
//    }

    // Custom JPanel class to paint background image
    class CustomPanel extends JPanel {
        private Image backgroundImage;

        public CustomPanel() {
            // Load the background image
            backgroundImage = new ImageIcon("D:/AK/Programs/Projects/JDBC/Icon/reg.jpg").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the background image
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
