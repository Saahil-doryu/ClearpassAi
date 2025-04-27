import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ClearPassAIApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClearPassAIGUI gui = new ClearPassAIGUI();
            gui.setVisible(true);
        });
    }

	
}
