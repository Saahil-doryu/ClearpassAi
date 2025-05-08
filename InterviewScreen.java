import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class InterviewScreen extends JPanel {
    private ClearPassAIGUI controller;
    private JTextArea questionArea;
    private JTextArea answerInput;
    private JTextArea feedbackArea;
    private JButton generateBtn, nextBtn, evaluateBtn, submitBtn, readBtn, stopBtn;
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

        generateBtn = new JButton("Generate Question");
        evaluateBtn = new JButton("Evaluate Answer");
        nextBtn = new JButton("Next Question");
        readBtn = new JButton("Read Question");
        stopBtn = new JButton("Stop Reading");
        submitBtn = new JButton("Submit Interview");
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
        buttonPanel.add(readBtn, gbc);
        gbc.gridx = 4;
        buttonPanel.add(stopBtn, gbc);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(welcomeLabel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timerPanel.add(timerLabel);
        timerPanel.add(counterLabel);
        topPanel.add(timerPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        centerPanel.add(new JScrollPane(questionArea));
        centerPanel.add(new JScrollPane(answerInput));
        centerPanel.add(new JScrollPane(feedbackArea));
        add(centerPanel, BorderLayout.CENTER);

        // Create bottom panel for submit button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitBtn.setPreferredSize(new Dimension(200, 40)); // Make submit button larger
        bottomPanel.add(submitBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        addListeners();
    }

    private JTextArea createTextArea(boolean editable) {
        JTextArea ta = new JTextArea();
        ta.setEditable(editable);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(new Font("Arial", Font.PLAIN, 14));
        return ta;
    }

    private void addListeners() {
        generateBtn.addActionListener(e -> generateQuestion());
        evaluateBtn.addActionListener(e -> evaluateAnswer());
        nextBtn.addActionListener(e -> generateBtn.doClick());
        submitBtn.addActionListener(e -> {
            if (answeredCount > 0) {
                controller.finishSession();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Please answer at least one question before submitting.", 
                    "No Answers", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        readBtn.addActionListener(e -> {
            String question = questionArea.getText().trim();
            if (!question.isEmpty()) {
                try {
                    controller.getTTSClient().speakText(question);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error reading question: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        stopBtn.addActionListener(e -> {
            controller.getTTSClient().stopSpeaking();
        });
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