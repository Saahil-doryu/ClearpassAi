import javax.swing.*;

public class ClearPassAIApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClearPassAIGUI gui = new ClearPassAIGUI();
            gui.setVisible(true);
        });
    }
}