import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;

public class fb extends JFrame implements ActionListener {

    ImageIcon icon = new ImageIcon("D:/AK/Programs/Projects/JDBC/Icon/log.jpg");

    private JPanel log;
    private JTextField textField;
    private JPasswordField pwd;
    private JLabel msgLabel;
    private JButton signUpButton;
    private JButton backButton;

    private String jdbcURL = "jdbc:mysql://localhost:3306/pwd_mng";
    private String dbUsername = "root";
    private String dbPassword = "@#Abhi2703#@";
    private Connection connection;

    public fb() {
        establishDBConnection();
        setFrameProperties();
        setUpBackgroundPanel("D:/AK/Programs/Projects/JDBC/Icon/fb.jpg");
        setIconImage(icon.getImage());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Start the thread to display components
        new Thread(new ComponentDisplayThread()).start();

        // Display username and password from the database
        dispDetails();

        setVisible(true);
    }

    private void setFrameProperties() {
        setTitle("Facebook");
        setBounds(350, 200, 720, 480);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void setUpBackgroundPanel(String imagePath) {
        BackgroundPanel backgroundPanel = new BackgroundPanel(imagePath);
        backgroundPanel.setLayout(null);

        log = new JPanel();
        log.setLayout(null);
        log.setBounds(350, 115, 300, 200);
        log.setBackground(new Color(40, 169,218));

        backgroundPanel.add(log);
        setContentPane(backgroundPanel);
    }

    private void addFormElements() {
        // Label for title
        JLabel titleLabel = new JLabel("REGISTRATION");
        titleLabel.setBounds(70, 0, 200, 70);
        titleLabel.setForeground(new Color(222, 219, 219));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        log.add(titleLabel);

        // Label for Username
        JLabel userLabel = new JLabel("USERNAME:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(25, 60, 100, 25);
        log.add(userLabel);

        // Username field
        textField = new JTextField();
        textField.setBounds(100, 60, 160, 25);
        log.add(textField);

        // Label for Password
        JLabel passLabel = new JLabel("PASSWORD:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(25, 100, 100, 25);
        log.add(passLabel);

        // Password field
        pwd = new JPasswordField();
        pwd.setBounds(100, 100, 160, 25);
        log.add(pwd);
    }

    private void addButtonsAndMessage() {
        // Sign-up button
        signUpButton = createButton("REGISTER", 20, 150, new Color(99, 155, 236));
        log.add(signUpButton);
        // Back buttton
        backButton = createButton("BACK", 160, 150, new Color(99, 155, 236));
        log.add(backButton);

        // Message label to display errors or success messages
        msgLabel = new JLabel();
        msgLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        msgLabel.setForeground(Color.WHITE);
        msgLabel.setBounds(250, 270, 300, 30);
        log.add(msgLabel);
    }

    private JButton createButton(String text, int x, int y, Color backgroundColor) {
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
            System.exit(1);
        }
    }

    public void handleSignup() {
        String username = new String(textField.getText());
        String password = new String(pwd.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Fill In All Fields.");
            return;
        }

        try {
            // Check if the username is already taken
            String checkQuery = "SELECT * FROM fb WHERE fb_ue = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username Already Exists.");
                return;
            }

            try {
                BufferedReader rea = new BufferedReader(new FileReader("temp.txt"));
                String fb1 = rea.readLine();
                rea.close();

                // Insert new user into the database
                String query = "INSERT INTO fb VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, fb1);
                int result = statement.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Sign-Up Successful!");
                }
                else {
                    JOptionPane.showMessageDialog(this, "Sign-Up Failed.");
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error During Sign-Up.");
            //dispose();
        }
    }

    private void dispDetails() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBounds(20, 50, 300, 300);
        side.setBackground(new Color(0, 0, 0, 100));

        // Add title to the side panel
        JLabel sideTitle = new JLabel("User Data");
        sideTitle.setForeground(new Color(222, 219, 219));
        sideTitle.setFont(new Font("Arial", Font.BOLD, 20));
        sideTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(sideTitle);

        try {
            try {
                BufferedReader rea = new BufferedReader(new FileReader("temp.txt"));
                String fb1 = rea.readLine();
                rea.close();
                //System.out.println("Username from users: "+fb1);
                String checkQuery = "SELECT us_ue FROM fb where us_ue = ?";
                PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                checkStmt.setString(1, fb1);
                ResultSet rs = checkStmt.executeQuery();
                if(rs.next()) {
                    String query = "SELECT fb_ue, fb_pwd FROM fb where us_ue = '"+fb1+"'";
                    //checkStmt.setString(1, fb1);
                    Statement stmt = connection.createStatement();
                    ResultSet re = stmt.executeQuery(query);
                    while (re.next()) {
                        String user = re.getString("fb_ue");
                        String pass = re.getString("fb_pwd");

                        // Create a label for username
                        JLabel usernameLabel = new JLabel("Username: " + user);
                        usernameLabel.setForeground(Color.WHITE);
                        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
                        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                        side.add(usernameLabel);

                        // Create a label for password
                        JLabel passwordLabel = new JLabel("Password: " + pass);
                        passwordLabel.setForeground(Color.WHITE);
                        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
                        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                        side.add(passwordLabel);

                        //Space between
                        JLabel space = new JLabel("");
                        side.add(space);
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data from database.");
        }

        // Add the side panel to the frame
        getContentPane().add(side);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("REGISTER")) {
            handleSignup();
        }
        else if(action.equals("BACK")) {
            new accs();
            dispose();
        }
    }

    // Thread class to display the components with a time delay
    class ComponentDisplayThread implements Runnable {
        @Override
        public void run() {
            try {
                // Display labels, fields, and buttons with 1ms delay
                addFormElements();
                Thread.sleep(1); // Delay between components

                addButtonsAndMessage();
                Thread.sleep(1);

                log.repaint(); // Ensure panel refreshes after components are added
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
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

    /*public static void main(String[] args) {
        new fb();
    }*/
}
