import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.io.IOException;

public class InterviewScreen extends JPanel {
    private ClearPassAIGUI controller;
    private JTextArea questionArea;
    private JTextArea answerInput;
    private JTextArea feedbackArea;

    public InterviewScreen(ClearPassAIGUI controller) {
        this.controller = controller;

        setLayout(new BorderLayout());

        // Create fields
        questionArea = new JTextArea(5, 50);
        answerInput = new JTextArea(5, 50);
        feedbackArea = new JTextArea(5, 50);

        JPanel centerPanel = new JPanel(new GridLayout(6, 1));
        centerPanel.add(new JLabel("Generated Question:"));
        centerPanel.add(new JScrollPane(questionArea));
        centerPanel.add(new JLabel("Your Answer:"));
        centerPanel.add(new JScrollPane(answerInput));
        centerPanel.add(new JLabel("AI Feedback:"));
        centerPanel.add(new JScrollPane(feedbackArea));

        JButton generateBtn = new JButton("Generate Question");
        JButton evaluateBtn = new JButton("Evaluate Answer");

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(generateBtn);
        topPanel.add(evaluateBtn);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Action Listeners
        generateBtn.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae) {
                generateQuestion();
            }
        });
        
        evaluateBtn.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae) {
                evaluateAnswer();
            }
        });}
    //method to generate question based on role input
    private void generateQuestion() {
        String role = controller.getRole();
        String prompt = "Generate an interview question suitable for the job role of " + role;

        try {
            String question = controller.getGeminiClient().generateResponse(prompt);
            questionArea.setText(question);
        } catch (IOException ex) {
            questionArea.setText("Error: " + ex.getMessage());
        }
    }
    //method to review the user's answer
    private void evaluateAnswer() {
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
    }
}