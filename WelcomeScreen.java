import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JPanel {
    private ClearPassAIGUI controller;
    private JTextField roleField;
    private JSpinner questionCountSpinner;

    public WelcomeScreen(ClearPassAIGUI controller) {
        this.controller = controller;
        setLayout(new FlowLayout());

        add(new JLabel("Enter Job Role:"));
        roleField = new JTextField(20);
        add(roleField);

        add(new JLabel("Number of Questions:"));
        questionCountSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));
        add(questionCountSpinner);

        JButton startButton = new JButton("Start Interview");
        add(startButton);

        startButton.addActionListener(e -> {
            String role = roleField.getText().trim();
            int questionCount = (int) questionCountSpinner.getValue();
            if (!role.isEmpty()) {
                controller.setRole(role);
                controller.setQuestionCount(questionCount);
                controller.startSession(role, questionCount);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a job role.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
