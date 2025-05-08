import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.List;

public class ResultScreen extends JPanel {
    private JTextArea resultArea;

    public ResultScreen(ClearPassAIGUI controller) {
        setLayout(new BorderLayout(10, 10));

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
                sb.append("Question ").append(i + 1).append(":\n");
                sb.append(q.getQuestion()).append("\n\n");
                sb.append("Your Answer:\n");
                sb.append(q.getAnswer()).append("\n\n");
                sb.append("AI Feedback:\n");
                sb.append(q.getFeedback()).append("\n\n");
                sb.append("----------------------------------------\n\n");
            }

            resultArea.setText(sb.toString());
        }

        JButton backBtn = new JButton("Back to Welcome");
        backBtn.setPreferredSize(new Dimension(150, 40));
        backBtn.addActionListener(e -> controller.showScreen("Welcome"));

        JButton saveBtn = new JButton("Save Result");
        saveBtn.setPreferredSize(new Dimension(150, 40));
        saveBtn.addActionListener(
                (ActionEvent e) -> saveResultToFile(controller.getCurrentUser(), resultArea.getText()));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
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