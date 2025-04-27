
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JPanel {
    private ClearPassAIGUI controller;
    private JTextField roleField;

    public WelcomeScreen(ClearPassAIGUI controller) {
        this.controller = controller;

        setLayout(new FlowLayout());

        add(new JLabel("Enter Job Role:"));
        roleField = new JTextField(20);
        add(roleField);

        JButton startButton = new JButton("Start Interview");
        add(startButton);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String role = roleField.getText().trim();
                if (!role.isEmpty()) {
                	controller.setRole(role);
                    controller.startSession(role, 5); 
                }
            }
        });
    }
}