import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ClearPassAIGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPage loginPage;
    private WelcomeScreen welcomeScreen;
    private InterviewScreen interviewScreen;
    private ResultScreen resultScreen;
    private String role;
    private String currentUser;
    private int questionCount = 5;
    private GeminiClient geminiClient;
    private DataModel dataModel;

    public ClearPassAIGUI() {
        setTitle("ClearPass AI - Interview Coach");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        String apiKey = loadAPIKey();
        geminiClient = new GeminiClient(apiKey, "gemini-1.5-flash");
        dataModel = new DataModel();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPage = new LoginPage(this);
        welcomeScreen = new WelcomeScreen(this);
        resultScreen = new ResultScreen(this);

        mainPanel.add(loginPage, "Login");
        mainPanel.add(welcomeScreen, "Welcome");
        mainPanel.add(resultScreen, "Results");

        add(mainPanel);
        showScreen("Login");
    }

    private String loadAPIKey() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
            return props.getProperty("GEMINI_API_KEY");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "API key not found", "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public void startSession(String role, int questionCount) {
        this.role = role;
        this.questionCount = questionCount;
        this.dataModel = new DataModel();
        interviewScreen = new InterviewScreen(this, questionCount);
        JScrollPane scrollPane = new JScrollPane(interviewScreen);
        mainPanel.add(scrollPane, "Interview");
        showScreen("Interview");
    }

    public void finishSession() {
        resultScreen = new ResultScreen(this);
        mainPanel.add(resultScreen, "Results");
        showScreen("Results");
    }

    public GeminiClient getGeminiClient() {
        return geminiClient;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
}
