import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LoginPage extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton;
    private JLabel statusLabel;
    private boolean isSignUpMode = false;
    private ClearPassAIGUI controller;

    public LoginPage(ClearPassAIGUI controller) {
        this.controller = controller;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Welcome to ClearPass AI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        add(buttonPanel, gbc);

        gbc.gridy++;
        statusLabel = new JLabel("", SwingConstants.CENTER);
        add(statusLabel, gbc);

        loginButton.addActionListener(e -> {
            if (isSignUpMode)
                handleSignUp();
            else
                handleLogin();
        });

        signupButton.addActionListener(e -> toggleMode());
    }

    private void toggleMode() {
        isSignUpMode = !isSignUpMode;
        loginButton.setText(isSignUpMode ? "Create Account" : "Login");
        signupButton.setText(isSignUpMode ? "Back to Login" : "Sign Up");
        statusLabel.setText("");
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both fields", Color.RED);
            return;
        }

        if (authenticate(username, password)) {
            controller.setCurrentUser(username);
            controller.showScreen("Welcome");
        } else {
            showMessage("Invalid credentials", Color.RED);
        }
    }

    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill all fields", Color.RED);
            return;
        }

        File file = new File("users.txt");
        try {
            file.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ":")) {
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

            JOptionPane.showMessageDialog(this, "Account created successfully!");
            toggleMode();
        } catch (IOException ex) {
            showMessage("Error accessing file", Color.RED);
        }
    }

    private boolean authenticate(String username, String password) {
        File file = new File("users.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            showMessage("Error reading file", Color.RED);
        }
        return false;
    }

    private void showMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
}
