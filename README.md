# ClearPass AI - Java Swing Interview Simulator

ClearPass AI is a Java Swing-based desktop application that simulates AI-powered interview questions. It allows users to log in, specify a job role, answer AI-generated questions, receive real-time feedback, and view or export their session summaries.

## ✨ Features
- 🔐 User login and signup system (local file-based)
- 🧠 AI-generated interview questions based on job role
- ✅ Real-time feedback on user responses
- ⏱ Countdown timer per question
- 📊 Progress tracker (answered vs. remaining)
- 💾 Save full session summary and result to `.txt` files
- 🧾 Clean MVC-based structure for easy updates

## 🛠 Requirements
- Java 8 or higher
- Internet connection (for AI API calls)

## 📁 File Structure
```
project-root/
├── ClearPassAIApp.java
├── ClearPassAIGUI.java
├── InterviewScreen.java
├── LoginPage.java
├── WelcomeScreen.java
├── ResultScreen.java
├── DataModel.java
├── QuestionData.java
├── GeminiClient.java
├── users.txt
├── config.properties    <-- contains your API key
```

## 🔑 Setup API Key
1. Create a `config.properties` file in the root directory.
2. Add your Gemini API key:

```properties
GEMINI_API_KEY=your_actual_api_key_here
```

## ▶️ How to Run
### If using terminal:
```bash
javac *.java
java ClearPassAIApp
```
### If it doesn't work directly then:  
```bash
javac -cp ".:lib/json-20250107.jar" *.java
java -cp ".:lib/json-20250107.jar" ClearPassAIApp
```

### If using an IDE:
- Open the project.
- Set `ClearPassAIApp` as the main class.
- Run the project.

## 💬 Sample Credentials
- Username: `dhruvparmar`
- Password: `1234`

You can create new users via the Sign-Up screen.

## 📂 Output Files
- `username_answers.txt` → contains each Q&A + feedback
- `username_summary.txt` → full interview session
- `username_result.txt` → saved from Result screen (if user clicks save)

## ✅ Future Improvements
- Add profile picture upload per user
- Support for difficulty levels or job domains
- Migrate to JavaFX or FlatLaf for better visuals
- Export results as PDF

---
Made with 💙 by Dhruv Parmar
