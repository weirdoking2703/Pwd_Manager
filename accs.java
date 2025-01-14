import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class accs extends JFrame implements ActionListener {

    ImageIcon icon = new ImageIcon("D:/AK/Programs/Projects/JDBC/Icon/log.jpg");

    private JPanel log;
    private String jdbcURL = "jdbc:mysql://localhost:3306/pwd_mng";
    private String dbUsername = "root";
    private String dbPassword = "@#Abhi2703#@";
    private Connection connection;

    public accs() {
        establishDBConnection();
        setFrameProperties();
        setUpBackgroundPanel("D:/AK/Programs/Projects/JDBC/Icon/accs.jpg");
        setIconImage(icon.getImage());
        setVisible(true);

        // Create threads for adding labels and buttons with a 1ms delay
        new Thread(() -> addLabelWithDelay("Instagram", 200, 40)).start();
        new Thread(() -> addLabelWithDelay("Facebook", 200, 100)).start();
        new Thread(() -> addLabelWithDelay("GMail", 200, 160)).start();
        new Thread(() -> addLabelWithDelay("Twitter", 200, 220)).start();
        new Thread(() -> addLabelWithDelay("Snapchat", 200, 280)).start();
        new Thread(() -> addLabelWithDelay("Linways", 200, 340)).start();

        new Thread(() -> {
            addButtonWithDelay("I", 400, 40, "D:/AK/Programs/Projects/JDBC/Icon/in_log.jpg");
            addButtonWithDelay("F", 400, 100, "D:/AK/Programs/Projects/JDBC/Icon/fb_log.jpg");
            addButtonWithDelay("G", 400, 160, "D:/AK/Programs/Projects/JDBC/Icon/gm_log.jpg");
            addButtonWithDelay("T", 400, 220, "D:/AK/Programs/Projects/JDBC/Icon/tw_log.jpg");
            addButtonWithDelay("S", 400, 280, "D:/AK/Programs/Projects/JDBC/Icon/sn_log.jpg");
            addButtonWithDelay("L", 400, 340, "D:/AK/Programs/Projects/JDBC/Icon/lin_log.jpg");
        }).start();
    }

    // Function to add a button with a simulated 1ms delay
    private void addButtonWithDelay(String account, int x, int y, String logo) {
        try {
            Thread.sleep(1);  // 1ms delay
            JButton button = new JButton();
            ImageIcon logoo = new ImageIcon(logo);
            button.setBounds(x, y, 50, 50);
            button.setText(account);
            button.setIcon(logoo);
            button.addActionListener(this);
            log.add(button);
            log.revalidate();
            log.repaint();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Function to add a label with a simulated 1ms delay
    private void addLabelWithDelay(String labelText, int x, int y) {
        try {
            Thread.sleep(1);  // 1ms delay
            JLabel label = new JLabel(labelText);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(Color.white);
            label.setBounds(x, y, 100, 50);
            log.add(label);
            log.revalidate();
            log.repaint();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setFrameProperties() {
        setTitle("Accounts List");
        setBounds(350, 200, 720, 480);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void setUpBackgroundPanel(String imagePath) {
        BackgroundPanel backgroundPanel = new BackgroundPanel(imagePath);
        backgroundPanel.setLayout(null);

        log = new JPanel();
        log.setLayout(null);
        log.setBounds(9, 7, 680, 430);
        log.setBackground(new Color(0, 0, 0, 50));
        backgroundPanel.add(log);
        setContentPane(backgroundPanel);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("I")) {
            new insta();
            dispose();
        }
        else if (action.equals("F")) {
            new fb();
            dispose();
        }
        else if (action.equals("G")) {
            new gmal();
            dispose();
        }
        else if (action.equals("T")) {
            new twitt();
            dispose();
        }
        else if (action.equals("S")) {
            new sncht();
            dispose();
        }
        else if (action.equals("L")) {
            new lins();
            dispose();
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

//    public static void main(String[] args) {
//        new accs();
//    }
}
