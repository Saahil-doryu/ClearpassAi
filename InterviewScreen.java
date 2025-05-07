import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class InterviewScreen extends JPanel {
    private ClearPassAIGUI controller;
    private JTextArea questionArea;
    private JTextArea answerInput;
    private JTextArea feedbackArea;
    private JButton generateBtn, nextBtn, evaluateBtn;
    private JLabel timerLabel, counterLabel, welcomeLabel;
    private Timer timer;
    private int timeLeft = 60;
    private int answeredCount = 0;
    private int totalQuestions;

    public InterviewScreen(ClearPassAIGUI controller, int questionCount) {
        this.controller = controller;
        this.totalQuestions = questionCount;
        setLayout(new BorderLayout(10, 10));

        String username = controller.getCurrentUser();
        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        questionArea = createTextArea(false);
        answerInput = createTextArea(true);
        feedbackArea = createTextArea(false);
        disableCopyPaste(answerInput);

        generateBtn = new JButton("Generate Question");
        evaluateBtn = new JButton("Evaluate Answer");
        nextBtn = new JButton("Next Question");
        timerLabel = new JLabel("Time Left: 60s");
        counterLabel = new JLabel(getCounterText());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridx = 0;
        buttonPanel.add(generateBtn, gbc);
        gbc.gridx = 1;
        buttonPanel.add(evaluateBtn, gbc);
        gbc.gridx = 2;
        buttonPanel.add(nextBtn, gbc);
        gbc.gridx = 3;
        buttonPanel.add(timerLabel, gbc);
        gbc.gridx = 4;
        buttonPanel.add(counterLabel, gbc);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(welcomeLabel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.add(new JLabel("Generated Question:"));
        centerPanel.add(new JScrollPane(questionArea));
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(new JLabel("Your Answer:"));
        centerPanel.add(new JScrollPane(answerInput));
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(new JLabel("AI Feedback:"));
        centerPanel.add(new JScrollPane(feedbackArea));

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        addListeners();
    }

    private JTextArea createTextArea(boolean editable) {
        JTextArea ta = new JTextArea(5, 50);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(editable);
        return ta;
    }

    private void disableCopyPaste(JTextArea ta) {
        ta.getInputMap().put(KeyStroke.getKeyStroke("ctrl C"), "none");
        ta.getInputMap().put(KeyStroke.getKeyStroke("ctrl V"), "none");
        ta.getInputMap().put(KeyStroke.getKeyStroke("ctrl X"), "none");
        ta.setComponentPopupMenu(null);
        ta.setTransferHandler(null);
    }

    private void addListeners() {
        generateBtn.addActionListener(e -> generateQuestion());
        evaluateBtn.addActionListener(e -> evaluateAnswer());
        nextBtn.addActionListener(e -> generateBtn.doClick());
    }

    private void generateQuestion() {
        String role = controller.getRole();
        String username = controller.getCurrentUser();
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
    }

    private void evaluateAnswer() {
        String question = questionArea.getText().trim();
        String answer = answerInput.getText().trim();
        if (!question.isEmpty() && !answer.isEmpty()) {
            String evalPrompt = "Evaluate this answer to the question: \"" + question + "\".\n\nAnswer: \"" + answer
                    + "\"\n\nGive short, clear feedback.";
            try {
                String feedback = controller.getGeminiClient().generateResponse(evalPrompt);
                feedbackArea.setText(feedback);
                recordAnswer(question, answer, feedback);
            } catch (IOException ex) {
                feedbackArea.setText("Error: " + ex.getMessage());
            }
        }
    }

    private void startTimer() {
        timeLeft = 60;
        if (timer != null)
            timer.stop();
        timer = new Timer(1000, e -> {
            if (timeLeft <= 0) {
                timer.stop();
                nextBtn.doClick();
            } else {
                timeLeft--;
                timerLabel.setText("Time Left: " + timeLeft + "s");
            }
        });
        timer.start();
    }

    private void recordAnswer(String question, String answer, String feedback) {
        QuestionData qd = new QuestionData();
        qd.setQuestion(question);
        qd.setAnswer(answer);
        qd.setFeedback(feedback);
        controller.getDataModel().setHistory(qd);

        String filename = controller.getCurrentUser() + "_answers.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write("Q: " + question + "\nA: " + answer + "\nF: " + feedback + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        answeredCount++;
        counterLabel.setText(getCounterText());

        if (answeredCount >= totalQuestions) {
            controller.finishSession();
        }
    }

    private String getCounterText() {
        return "Answered: " + answeredCount + "/" + totalQuestions;
    }
}