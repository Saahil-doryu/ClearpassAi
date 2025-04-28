import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ClearPassAIApp extends JFrame {
    private final GeminiClient geminiClient;

    private JTextField roleInput;
    private JTextArea questionArea;
    private JTextArea answerInput;
    private JTextArea feedbackArea;

    public ClearPassAIApp() {
        geminiClient = new GeminiClient("AIzaSyDjIVnRqT1mcY-mh7QsKBIUF2Eb-__tiac", "gemini-1.5-flash");

        setTitle("ClearPass AI - Interview Coach");
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel parentPanel = new JPanel(new BorderLayout());
        parentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 20px padding on all sides

        // Top panel for role input and buttons
        JPanel topPanel = new JPanel(new FlowLayout());
        roleInput = new JTextField(20);
        JButton generateBtn = new JButton("Generate Question");
        JButton evaluateBtn = new JButton("Evaluate Answer");

        topPanel.add(new JLabel("Job Role:"));
        topPanel.add(roleInput);
        topPanel.add(generateBtn);
        topPanel.add(evaluateBtn);

        // Text areas
        questionArea = new JTextArea(5, 50);
        answerInput = new JTextArea(5, 50);
        feedbackArea = new JTextArea(5, 50);
        questionArea.setLineWrap(true);
        answerInput.setLineWrap(true);
        feedbackArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        answerInput.setWrapStyleWord(true);
        feedbackArea.setWrapStyleWord(true);

        // Layout for center
        JPanel centerPanel = new JPanel(new GridLayout(6, 1));
        centerPanel.add(new JLabel("Generated Question:"));
        centerPanel.add(new JScrollPane(questionArea));
        centerPanel.add(new JLabel("Your Answer:"));
        centerPanel.add(new JScrollPane(answerInput));
        centerPanel.add(new JLabel("AI Feedback:"));
        centerPanel.add(new JScrollPane(feedbackArea));

        // Block user to copy paste and cut as well
        answerInput.getInputMap().put(KeyStroke.getKeyStroke("ctrl C"), "none");
        answerInput.getInputMap().put(KeyStroke.getKeyStroke("ctrl V"), "none");
        answerInput.getInputMap().put(KeyStroke.getKeyStroke("ctrl X"), "none");

        answerInput.setComponentPopupMenu(null);

        // Blocking it to access Clipboard
        answerInput.setTransferHandler(null);

        // Blocking the Question and feedback field
        questionArea.setEditable(false);
        feedbackArea.setEditable(false);

        // Add to frame
        parentPanel.add(topPanel, BorderLayout.NORTH);
        parentPanel.add(centerPanel, BorderLayout.CENTER);
        add(parentPanel);

        // Action Listeners
        generateBtn.addActionListener((ActionEvent e) -> {
            String role = roleInput.getText().trim();
            if (!role.isEmpty()) {
                String prompt = "Imagine you are an expert interviewer. The candidate is applying for the job role: \""
                        + role + "\". " +
                        "Please create a thoughtful and beginner-friendly interview question. " +
                        "If the role is related to technology (like software development, web development, data science, IT, etc.), then add a simple coding-related question too, such as: "
                        +
                        "'Complete this small code snippet', 'Find and fix the error', or 'Explain this basic concept'. "
                        +
                        "If the role is non-technical (business, management, arts, etc.), ask a soft-skill or behavioral question. "
                        +
                        "Your output should be ONLY the interview question itself, written naturally, and easy for a user to understand. "
                        +
                        "Do not include any labels like 'Interview Question:' or any explanation.";

                try {
                    questionArea.setText(geminiClient.generateResponse(prompt));
                } catch (IOException ex) {
                    questionArea.setText("Error: " + ex.getMessage());
                }
            }
        });

        evaluateBtn.addActionListener((ActionEvent e) -> {
            String question = questionArea.getText().trim();
            String answer = answerInput.getText().trim();
            if (!question.isEmpty() && !answer.isEmpty()) {
                String evalPrompt = "Evaluate this answer to the question: \"" + question + "\".\n\nAnswer: \"" + answer
                        + "\"\n\nGive short, clear feedback.";
                String feedback = "";
                try {
                    feedback = geminiClient.generateResponse(evalPrompt);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                feedbackArea.setText(feedback);
            }

        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClearPassAIApp app = new ClearPassAIApp();
            app.setVisible(true);
        });
    }
}
