import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class welcome extends JFrame implements ActionListener{
    ImageIcon ico = new ImageIcon("D:/AK/Programs/Projects/JDBC/Icon/log.jpg");

    JButton button = new JButton();
    public welcome(){
        ImageIcon icon = new ImageIcon("D:/AK/Programs/Projects/JDBC/Icon/Welcome.jpg");
        setBounds(350, 200, 720, 480);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(ico.getImage());

        button.setBounds(0, 0, 720, 480);
        button.setLayout(null);
        button.setIcon(icon);
        button.addActionListener(this);
        add(button);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if the source of the event is the button
        if (e.getSource() == button) {
            new login(); // Open the login frame
            dispose(); // Close the welcome frame
        }
    }
}
