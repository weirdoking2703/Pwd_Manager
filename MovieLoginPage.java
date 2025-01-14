import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MovieLoginPage extends JFrame implements ActionListener {

    // Components of the login form
    private JPanel loginPanel;
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    // Database connection details
    private String jdbcURL = "jdbc:mysql://localhost:3306/movie_hub";
    private String dbUsername = "root";
    private String dbPassword = "@#Abhi2703#@";
    private Connection connection;

    // Constructor
    public MovieLoginPage() {
        establishDBConnection(); // Establish database connection
        setFrameProperties(); // Set the frame properties
        setUpBackgroundPanel("D:/AK/Pics/Astrology.png");
        setVisible(true); // Show the frame
    }

    // Main method to run the program
    public static void main(String[] args) {
        new MovieLoginPage();
    }

    // Set the frame properties
    private void setFrameProperties() {
        setTitle("Movie Recommendation Site - Login");
        setBounds(300, 100, 1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    // Set up the background panel and login box
    private void setUpBackgroundPanel(String imagePath) {
        BackgroundPanel backgroundPanel = new BackgroundPanel(imagePath);
        backgroundPanel.setLayout(null);

        // Create the login box (JPanel)
        loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBounds(325, 200, 300, 260);
        loginPanel.setBackground(new Color(0, 0, 0, 180)); // Semi-transparent black background

        // Initialize UI components
        initializeUIComponents();

        // Add the login panel to the background panel
        backgroundPanel.add(loginPanel);
        setContentPane(backgroundPanel); // Add the background panel to the frame
    }

    // Initialize UI components
    private void initializeUIComponents() {
        addTitleLabel();
        addLabelAndField("Username:", 20, 50, userTextField = new JTextField());
        addLabelAndField("Password:", 20, 90, passwordField = new JPasswordField());
        addButtonsAndMessage();
    }

    // Add title label
    private void addTitleLabel() {
        JLabel titleLabel = new JLabel("Login to Movies");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(70, 10, 250, 30);
        loginPanel.add(titleLabel);
    }

    // Helper function to add label and input fields
    private void addLabelAndField(String labelText, int x, int y, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 100, 30);
        loginPanel.add(label);

        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBounds(120, y, 150, 30);
        loginPanel.add(textField);
    }

    // Helper function to add buttons and message label
    private void addButtonsAndMessage() {
        loginPanel.add(createButton("Login", 40, 140, new Color(70, 130, 180)));
        loginPanel.add(createButton("Reset", 150, 140, new Color(220, 20, 60)));
        loginPanel.add(createButton("Sign Up", 95, 180, new Color(50, 205, 50)));

        messageLabel = new JLabel();
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBounds(70, 220, 200, 30);
        loginPanel.add(messageLabel);
    }

    // Helper function to create buttons
    private JButton createButton(String text, int x, int y, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBounds(x, y, 90, 30);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.addActionListener(this);
        return button;
    }

    // Handling button events
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Login")) {
            handleLogin();
        } else if (e.getActionCommand().equals("Reset")) {
            handleReset();
        } else if (e.getActionCommand().equals("Sign Up")) {
            handleSignUp();
        }
    }

    // Handle login button click
    private void handleLogin() {
        String userText = userTextField.getText();
        String pwdText = new String(passwordField.getPassword());

        if (authenticateUser(userText, pwdText)) {
            messageLabel.setText("Login successful");
            JOptionPane.showMessageDialog(this, "Welcome to Movie Recommendations, " + userText + "!");
            openMovieRecommendations("C:\\Users\\alapp\\IdeaProjects\\project1.java\\pic\\picbg\\ai-generated-8886074_1280.jpg", userText);
        } else {
            messageLabel.setText("Invalid username or password");
        }
    }

    // Handle reset button click
    private void handleReset() {
        userTextField.setText("");
        passwordField.setText("");
        messageLabel.setText("");
    }

    // Handle sign-up button click
    private void handleSignUp() {
        String username = userTextField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.");
        } else if (createNewUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Sign up successful! You can now log in.");
        } else {
            JOptionPane.showMessageDialog(this, "Sign up failed. Username might already exist.");
        }
    }

    // Authenticate user from the database
    private boolean authenticateUser(String username, String password) {
        String sql = "SELECT * FROM login WHERE userid = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            return false;
        }
    }

    // Create a new user in the database
    private boolean createNewUser(String username, String password) {
        String query = "INSERT INTO login (userid, password) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            return false;
        }
    }

    // Open the movie recommendations page
    private void openMovieRecommendations(String imagePath, String username) {
        JFrame recommendationsFrame = new JFrame("Movie Recommendations");
        recommendationsFrame.setBounds(300, 100, 1000, 700);
        recommendationsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BackgroundPanel backgroundPanel = new BackgroundPanel(imagePath);
        backgroundPanel.setLayout(new BorderLayout());
        recommendationsFrame.setContentPane(backgroundPanel);

        JLabel welcomeLabel = new JLabel("Welcome to Movie Recommendations, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        backgroundPanel.add(welcomeLabel, BorderLayout.CENTER);

        recommendationsFrame.setVisible(true);
    }

    // Establish database connection
    private void establishDBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
            System.exit(1); // Exit application if connection fails
        }
    }

    // Custom JPanel class to handle background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error loading background image: " + e.getMessage());
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
}