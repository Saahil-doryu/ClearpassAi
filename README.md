# ClearPass AI - Java Swing Interview Simulator

ClearPass AI is a Java Swing-based desktop application that simulates AI-powered interview questions. It allows users to log in, specify a job role, answer AI-generated questions, receive real-time feedback, and view or export their session summaries.

## ✨ Features
- 🔐 User login and signup system (local file-based)
- 🧠 AI-generated interview questions based on job role (using Google's Gemini API)
- 😊 Sentiment analysis of answers (using HuggingFace's DistilBERT model)
- ✅ Real-time feedback on user responses
- ⏱ Countdown timer per question
- 📊 Progress tracker (answered vs. remaining)
- 💾 Save full session summary and result to `.txt` files
- 🧾 Clean MVC-based structure for easy updates
- 🔊 Text-to-Speech using Microsoft Edge-TTS
  - High-quality voice synthesis
  - Play/Pause/Stop controls
  - Non-blocking audio playback
- 🎯 Interview Features:
  - Role-specific questions
  - Time management per question
  - Answer submission with feedback
  - Progress tracking
- 📝 Result Features:
  - Comprehensive session summary
  - Question-wise feedback
  - Export functionality
  - Clean, scrollable interface

## 🛠 Requirements
- Java 8 or higher
- Python 3.x (for Edge-TTS)
- Internet connection (for AI API calls)
- JSON library (org.json)
- Edge-TTS Python package
- FFmpeg (for audio conversion)

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
├── EdgeTTSClient.java
├── users.txt
├── config.properties    <-- contains your API keys
├── venv/               <-- Python virtual environment
└── lib/               <-- External libraries
```

## 🔑 Setup API Keys
1. Create a `config.properties` file in the root directory.
2. Add your API keys:

```properties
GEMINI_API_KEY=your_gemini_api_key_here
```

## 🚀 Installation
1. Install Python dependencies:
```bash
python3 -m venv venv
source venv/bin/activate
pip install edge-tts
```

2. Install FFmpeg (macOS):
```bash
brew install ffmpeg
```

## ▶️ How to Run
### If using terminal:
```bash
javac -cp ".:lib/json-20250107.jar" *.java
java -cp ".:lib/json-20250107.jar" ClearPassAIApp
```

### If using an IDE:
- Open the project
- Add the JSON library to your project dependencies
- Set `ClearPassAIApp` as the main class
- Run the project

## 💬 Sample Credentials
- Username: `saahil`
- Password: `123`

You can create new users via the Sign-Up screen.

## 📂 Output Files
- `username_answers.txt` → contains each Q&A + feedback
- `username_summary.txt` → full interview session
- `username_result.txt` → saved from Result screen (if user clicks save)

## 🎯 Usage Guide
1. **Login/Signup**
   - Use existing credentials or create a new account
   - Secure local authentication

2. **Interview Process**
   - Select your job role
   - Read questions with text-to-speech
   - Submit answers within time limit
   - Receive immediate feedback

3. **Results**
   - View complete interview summary
   - Export results to text files
   - Review feedback and performance

## ✅ Future Improvements
- Add more AI models for different types of analysis
- Implement voice input for answers
- Add support for different languages
- Enhance the UI with modern design elements
- Add interview session recording
- Implement progress saving/resume functionality

---
Made with 💙 by Dhruv Parmar
