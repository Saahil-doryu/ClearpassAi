# 🧠 ClearPass AI - Interview Coach

> **Empowering your interview success with the help of AI!**  
> Practice questions, get instant feedback, and sharpen your skills with ClearPass AI.

---

## 📜 Project Overview
ClearPass AI is an interactive desktop application designed to help users **prepare for job interviews**.  
It uses **Google Gemini API** to:
- 🔹 Generate realistic, role-specific interview questions.
- 🔹 Allow users to answer and practice.
- 🔹 Provide **instant AI-powered feedback** on answers.

The app includes a **login system**, **progress tracker**, **save session feature**, and a **beautiful modern UI**.

---

## 🛠️ Features
- ✨ **Login Page** with username/password input.
- 🧠 **AI-Powered Question Generator** (based on job role input).
- ✍️ **User Answer Input** with protected text fields (no copy-paste).
- 📝 **Real-time AI Feedback** on answers.
- 📈 **Progress Tracker** (questions completed).
- 💾 **Save Session** to a local file.
- 🎨 **Modern Color Theme** for a calming, professional experience.
- 🔄 **New Question** & **Clear Answer** buttons.
- 🔒 **Logout** functionality.

---

- **Login Page**
- **Main Dashboard**
- **Generated Questions & Feedback**

---

## 🧩 Technologies Used
- **Java 17**
- **Java Swing (UI Framework)**
- **Google Gemini API (Language Model)**  
- **HTTP Communication (URLConnection)**

---

## 🚀 Installation & Setup

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/your-username/clearpass-ai.git
   cd clearpass-ai
   ```

2. **Get Gemini API Key:**
   - Go to [Google AI Studio](https://makersuite.google.com/) and create an API key.
   - Replace the placeholder API key in `GeminiClient.java`:
     ```java
     geminiClient = new GeminiClient("YOUR_API_KEY", "gemini-1.5-flash");
     ```

3. **Run the App:**
   - Compile the project:
     ```bash
     javac ClearPassAIApp.java GeminiClient.java AiPrompt.java
     ```
   - Run:
     ```bash
     java ClearPassAIApp
     ```

4. **(Optional)**: Package into `.jar` for easy use.

---

## 🧹 Project Structure

```
/ClearPassAI
 ├── ClearPassAIApp.java    # Main GUI Application
 ├── GeminiClient.java      # Handles API communication with Gemini
 ├── AiPrompt.java          # Command-line version (optional)
 ├── /assets                # (Optional) App icons, logo
 ├── README.md              # Documentation
```

---

## ⚙️ Future Improvements (Planned)
- 🌙 Add Dark Mode.
- 🔐 Connect real signup/login authentication with SQLite.
- 🧑‍💻 Daily Challenges (Hard Questions).
- 📈 Performance Analytics (track user's improvement).
- 🗣️ Voice Answer Input using Java Speech API.

---

## 📄 License
This project is licensed under the **MIT License** - feel free to use, modify, and share!  

---

## 🤝 Acknowledgments
- Thanks to **Google Gemini API** for free access to AI models.
- Designed with ❤️ by [Your Name].

---

# 🧠 ClearPass AI - Unlock your true potential.