🤖 AI Task Manager
An intelligent Android task management application powered by Google Gemini AI. Built with Java and Android Studio as part of my mobile development and Agentic AI learning journey.

📱 Screenshots

Add your app screenshots here after running the app


✨ Features

➕ Add Tasks — Quickly add tasks with a title and optional description
🤖 AI Analysis — Google Gemini AI analyses each task and provides:

Priority level (High / Medium / Low)
Suggested deadline
Breakdown into 3 actionable subtasks


🔄 Status Tracking — Cycle tasks between Pending → In Progress → Done
🗑️ Delete Tasks — Remove completed or unwanted tasks
🎨 Clean Modern UI — Professional blue and white design


🛠️ Tech Stack
TechnologyUsageJavaPrimary programming languageAndroid StudioDevelopment environmentGoogle Gemini AI APIAI-powered task analysisOkHttpHTTP client for API callsRecyclerViewTask list displayCardViewUI card componentsMaterial DesignUI components and styling

🚀 Getting Started
Prerequisites

Android Studio (latest version)
Android device or emulator (API 24+)
Google Gemini API key (free at aistudio.google.com)

Installation

Clone the repository:

bashgit clone https://github.com/ayaanfodkarr/AI-Task-Manager.git

Open in Android Studio:

Open Android Studio
Click File → Open
Select the cloned folder


Add your Gemini API Key:

Open app/src/main/java/com/ayaan/aitaskmanager/GeminiHelper.java
Replace YOUR_GEMINI_API_KEY_HERE with your actual API key:



java   private static final String API_KEY = "your_api_key_here";

Run the app:

Connect your Android device or start an emulator
Click the green Run ▶ button




📖 How to Use

Add a task — Type a task title and optional description, tap Add Task
Analyse with AI — Tap the Analyse button on any task
View AI results — Gemini AI will show priority, deadline, and subtasks
Update status — Tap the task to cycle through Pending → In Progress → Done
Delete task — Tap the delete button to remove a task


🏗️ Project Structure
app/src/main/
├── java/com/ayaan/aitaskmanager/
│   ├── MainActivity.java       # Main screen and app logic
│   ├── GeminiHelper.java       # Gemini AI API integration
│   ├── Task.java               # Task data model
│   └── TaskAdapter.java        # RecyclerView adapter
├── res/
│   ├── layout/
│   │   ├── activity_main.xml   # Main screen layout
│   │   └── item_task.xml       # Task item layout
│   ├── drawable/
│   │   ├── input_bg.xml        # Input field background
│   │   ├── card_bg.xml         # Card background
│   │   └── badge_bg.xml        # Priority badge background
│   └── values/
│       ├── colors.xml          # App color palette
│       ├── strings.xml         # App strings
│       └── themes.xml          # App theme
└── AndroidManifest.xml         # App permissions and config

🔑 API Configuration
This app uses the Google Gemini API (free tier available):

Visit Google AI Studio
Sign in with your Google account
Click Get API Key → Create API Key
Copy and paste it into GeminiHelper.java


📚 What I Learned

Android app development using Java
Integrating REST APIs in Android using OkHttp
Working with RecyclerView and custom adapters
Material Design UI principles
Implementing Google Gemini AI in a mobile application
Asynchronous programming with callbacks in Android


🎓 About
This project was built as part of my Mobile Application Development coursework and personal learning at Thompson Rivers University. It combines my skills in:

Android/Java mobile development
Python & web development background
Agentic AI & LLM integration interest


👨‍💻 Developer
Ayaan Imtiyaz Fodkar

GitHub: @ayaanfodkarr
University: Thompson Rivers University, BC, Canada
Program: Bachelor of Computer Science (2nd Year)


📄 License
This project is open source and available under the MIT License.
