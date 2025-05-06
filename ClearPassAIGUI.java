import javax.swing.*;
import java.awt.*;

public class ClearPassAIGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;


    private LoginPage loginPage;
    private WelcomeScreen welcomeScreen;
    private InterviewScreen interviewScreen;
    private ResultScreen resultScreen;
    private String role;
    private GeminiClient geminiClient;
    private DataModel dataModel;

    public ClearPassAIGUI() {
        // Setup the JFrame
        setTitle("ClearPass AI - Interview Coach");
        setSize(600, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize GeminiClient
        geminiClient = new GeminiClient("AIzaSyDjIVnRqT1mcY-mh7QsKBIUF2Eb-__tiac", "gemini-1.5-flash");
        // Initialize DataModel
        dataModel = new DataModel();
        // Set up CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        loginPage = new LoginPage(this);

        // Initialize all the Screens and pass this controller into them
        
        welcomeScreen = new WelcomeScreen(this);
        interviewScreen = new InterviewScreen(this);
        resultScreen = new ResultScreen(this);

        // screens are added to CardLayout

        mainPanel.add(loginPage, "Login");
        mainPanel.add(welcomeScreen, "Welcome");
        mainPanel.add(interviewScreen, "Interview");
        mainPanel.add(resultScreen, "Results");

        // the main panel is added to the frame
        add(mainPanel);
        showScreen("Login");
    }
    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public void startSession(String role, int questionCount) {
        showScreen("Interview");
    }

    public void finishSession() {
        showScreen("Results");
    }
  
	  public GeminiClient getGeminiClient() {
		  return this.geminiClient;
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

}
