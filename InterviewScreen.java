import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class InterviewScreen extends JPanel {
    private ClearPassAIGUI controller;
    private JTextArea questionArea;
    private JTextArea answerInput;
    private JTextArea feedbackArea;
    private JButton generateBtn;
    private JButton evaluateBtn;

    public InterviewScreen(ClearPassAIGUI controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // Text Areas
        questionArea = new JTextArea(5, 50);
        answerInput = new JTextArea(5, 50);
        feedbackArea = new JTextArea(5, 50);

        questionArea.setLineWrap(true);
        answerInput.setLineWrap(true);
        feedbackArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        answerInput.setWrapStyleWord(true);
        feedbackArea.setWrapStyleWord(true);

        // Block user from copying, pasting, and cutting - INLINE exactly as your coworker wrote
        answerInput.getInputMap().put(KeyStroke.getKeyStroke("ctrl C"), "none");
        answerInput.getInputMap().put(KeyStroke.getKeyStroke("ctrl V"), "none");
        answerInput.getInputMap().put(KeyStroke.getKeyStroke("ctrl X"), "none");
        answerInput.setComponentPopupMenu(null);
        answerInput.setTransferHandler(null);

        // Make questionArea and feedbackArea non-editable
        questionArea.setEditable(false);
        feedbackArea.setEditable(false);

        // Buttons
        generateBtn = new JButton("Generate Question");
        evaluateBtn = new JButton("Evaluate Answer");

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(generateBtn);
        topPanel.add(evaluateBtn);

        JPanel centerPanel = new JPanel(new GridLayout(6, 1));
        centerPanel.add(new JLabel("Generated Question:"));
        centerPanel.add(new JScrollPane(questionArea));
        centerPanel.add(new JLabel("Your Answer:"));
        centerPanel.add(new JScrollPane(answerInput));
        centerPanel.add(new JLabel("AI Feedback:"));
        centerPanel.add(new JScrollPane(feedbackArea));

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Action Listeners
        generateBtn.addActionListener((ActionEvent e) -> {
            String role = controller.getRole();
            if (!role.isEmpty()) {
                String prompt = "Imagine you are an expert interviewer. The candidate is applying for the job role: \"" 
                        + role + "\". " +
                        "Please create a thoughtful and beginner-friendly interview question. " +
                        "If the role is related to technology (like software development, web development, data science, IT, etc.), then add a simple coding-related question too, such as: " +
                        "'Complete this small code snippet', 'Find and fix the error', or 'Explain this basic concept'. " +
                        "If the role is non-technical (business, management, arts, etc.), ask a soft-skill or behavioral question. " +
                        "Your output should be ONLY the interview question itself, written naturally, and easy for a user to understand. " +
                        "Do not include any labels like 'Interview Question:' or any explanation.";
                try {
                    questionArea.setText(controller.getGeminiClient().generateResponse(prompt));
                } catch (IOException ex) {
                    questionArea.setText("Error: " + ex.getMessage());
                }
            }
        });

        evaluateBtn.addActionListener((ActionEvent e) -> {
            String question = questionArea.getText().trim();
            String answer = answerInput.getText().trim();
            if (!question.isEmpty() && !answer.isEmpty()) {
                String evalPrompt = "Evaluate this answer to the question: \"" + question + "\".\n\nAnswer: \"" + answer + "\"\n\nGive short, clear feedback.";
                try {
                    String feedback = controller.getGeminiClient().generateResponse(evalPrompt);
                    feedbackArea.setText(feedback);
                } catch (IOException ex) {
                    feedbackArea.setText("Error: " + ex.getMessage());
                }
            }
        });
    }
}
