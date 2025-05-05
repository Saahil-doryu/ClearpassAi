import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class InterviewScreen extends JPanel {
    private ClearPassAIGUI controller;
    private JTextArea questionArea;
    private JTextArea answerInput;
    private JTextArea feedbackArea;
    private JButton generateBtn, nextBtn;
    private JButton evaluateBtn;
    private JLabel timerLabel = new JLabel("Time Left: 60s");
    private Timer timer;
    private int timeLeft = 60;

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

        // Block user from copying, pasting, and cutting
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
        nextBtn = new JButton("Next Question");

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(generateBtn);
        topPanel.add(evaluateBtn);
        topPanel.add(nextBtn);
        topPanel.add(timerLabel);

        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel centerPanel = new JPanel(new GridLayout(6, 1));
        centerPanel.add(new JLabel("Generated Question:"));
        centerPanel.add(new JScrollPane(questionArea));
        centerPanel.add(new JLabel("Your Answer:"));
        centerPanel.add(new JScrollPane(answerInput));
        centerPanel.add(new JLabel("AI Feedback:"));
        centerPanel.add(new JScrollPane(feedbackArea));

        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Action Listeners
        generateBtn.addActionListener((ActionEvent e) -> {
            String role = controller.getRole();
            if (!role.isEmpty()) {
                String prompt = "Imagine you are an expert interviewer. The candidate is applying for the job role: \""
                        + role + "\". Please create a thoughtful and beginner-friendly interview question.";
                try {
                    questionArea.setText(controller.getGeminiClient().generateResponse(prompt));
                    answerInput.setText("");
                    feedbackArea.setText("");
                    startTimer();
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
                try {
                    String feedback = controller.getGeminiClient().generateResponse(evalPrompt);
                    feedbackArea.setText(feedback);
                } catch (IOException ex) {
                    feedbackArea.setText("Error: " + ex.getMessage());
                }
            }
        });

        nextBtn.addActionListener((ActionEvent e) -> {
            String role = controller.getRole();
            if (!role.isEmpty()) {
                String prompt = "Imagine you are an expert interviewer. The candidate is applying for the job role: \""
                        + role + "\". Please create a new interview question.";
                try {
                    String newQuestion = controller.getGeminiClient().generateResponse(prompt);
                    questionArea.setText(newQuestion);
                    answerInput.setText("");
                    feedbackArea.setText("");
                    resetTimer();
                } catch (IOException ex) {
                    questionArea.setText("Error: " + ex.getMessage());
                }
            }
        });
    }

    private void startTimer() {
        timeLeft = 60;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft <= 0) {
                    ((Timer) e.getSource()).stop();
                    nextBtn.doClick();
                } else {
                    timeLeft--;

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            timerLabel.setText("Time Left: " + timeLeft + "s");
                        }
                    });
                }
            }
        });
        timer.start();
    }

    private void resetTimer() {
        if (timer != null) {
            timer.stop();
        }
        startTimer();
    }
}
