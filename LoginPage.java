import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LoginPage extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton;
    private JLabel statusLabel;
    private boolean isSignUpMode = false; // Toggle between login/signup modes
    private ClearPassAIGUI controller;
    
    public LoginPage(ClearPassAIGUI controller) {
    	this.controller = controller;
    	//setTitle("ClearPass AI - Login");
    	setName("ClearPass AI - Login");
    	//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        //setLocationRelativeTo(null);
        //setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Welcome to ClearPass AI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label + Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        // Password Label + Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(120, 30));
        loginButton.addActionListener(new LoginButtonListener());
        mainPanel.add(loginButton);

        signupButton = new JButton("Sign Up");
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.setPreferredSize(new Dimension(120, 30));
        signupButton.addActionListener(new SignupButtonListener());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(signupButton);

        statusLabel = new JLabel("");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(statusLabel);

        add(mainPanel);
    }

    
    // Separate listener classes
    private class LoginButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (isSignUpMode) {
                handleSignUp();
            } else {
                handleLogin();
            }
        }
    }

    private class SignupButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            toggleSignUpMode();
        }
    }

    private void toggleSignUpMode() {
        isSignUpMode = !isSignUpMode;
        if (isSignUpMode) {
            loginButton.setText("Create Account");
            signupButton.setText("Back to Login");
        } else {
            loginButton.setText("Login");
            signupButton.setText("Sign Up");
        }
        statusLabel.setText("");
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter username and password", Color.RED);
            return;
        }

        if (authenticate(username, password)) {
            handleSuccessfulLogin();
        } else {
            showMessage("Invalid username or password", Color.RED);
        }
    }

    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill all fields", Color.RED);
            return;
        }

        try {
            File file = new File("users.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username)) {
                    reader.close();
                    showMessage("Username already exists", Color.RED);
                    return;
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(username + ":" + password);
            writer.newLine();
            writer.close();

            JOptionPane.showMessageDialog(this, "Account created successfully! Please login.");
            toggleSignUpMode();
        } catch (IOException ex) {
            ex.printStackTrace();
            showMessage("Error saving account", Color.RED);
        }
    }

    private boolean authenticate(String username, String password) {
        try {
            File file = new File("users.txt");
            if (!file.exists()) {
                return false;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String savedUsername = parts[0];
                    String savedPassword = parts[1];
                    if (savedUsername.equals(username) && savedPassword.equals(password)) {
                        reader.close();
                        return true;
                    }
                }
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void handleSuccessfulLogin() {
        controller.showScreen("Welcome");
        //dispose();
    }

    private void showMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
    
//		no longer needed now that all screens are managed by ClearPassGUI
//    
//    	public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new LoginPage().setVisible(true);
//            }
//        });
//    }
}
