
import javax.swing.*;
import java.awt.*;

public class ResultScreen extends JPanel {
    private ClearPassAIGUI controller;

    public ResultScreen(ClearPassAIGUI controller) {
        this.controller = controller;

        setLayout(new BorderLayout());

        JLabel label = new JLabel("Result Screen - Display results here");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
