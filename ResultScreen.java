import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.List;

public class ResultScreen extends JPanel {
    private JTextArea resultArea;

    public ResultScreen(ClearPassAIGUI controller) {
        setLayout(new BorderLayout());

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);

        List<QuestionData> history = controller.getDataModel().getHistory();
        int total = controller.getQuestionCount();
        String username = controller.getCurrentUser();

        if (history.isEmpty()) {
            resultArea.setText("No responses recorded.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Summary: You answered ").append(history.size()).append(" out of ").append(total)
                    .append(" questions.\n\n");

            for (int i = 0; i < history.size(); i++) {
                QuestionData q = history.get(i);
                sb.append("Q").append(i + 1).append(": ").append(q.getQuestion()).append("\n");
                sb.append("Your Answer: ").append(q.getAnswer()).append("\n");
                sb.append("AI Feedback: ").append(q.getFeedback()).append("\n\n");
            }

            resultArea.setText(sb.toString());
        }

        JButton backBtn = new JButton("Back to Welcome");
        backBtn.addActionListener(e -> controller.showScreen("Welcome"));

        JButton saveBtn = new JButton("Save Result");
        saveBtn.addActionListener(
                (ActionEvent e) -> saveResultToFile(controller.getCurrentUser(), resultArea.getText()));

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(backBtn);
        bottomPanel.add(saveBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void saveResultToFile(String username, String content) {
        String filename = username + "_result.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
            JOptionPane.showMessageDialog(this, "Result saved to " + filename);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save result.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}