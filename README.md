# 🚀 Mission Me – Smart Task Manager App

> A simple, intuitive, and personal **Task Manager** Android application built for productivity – designed and developed as part of a semester project at **St Aloysius (Deemed to be University)**.

---

## 📱 App Overview

**Mission Me** helps users manage their daily goals and tasks with ease. With Firebase integration and a clean UI, this app ensures tasks are secure, synchronized, and always accessible. Whether you’re planning a day or organizing your life, Mission Me keeps you on track.

---

## 🎯 Features

✅ **User Authentication**  
✅ **Add, Edit, Delete Tasks**  
✅ **Custom Splash Screen**  
✅ **Task Filtering** – View All / Completed / Pending  
✅ **Sorting** – Alphabetically or by Timestamp  
✅ **Task Completion Status**  
✅ **Dark Mode Support**  
✅ **Firebase Firestore for real-time storage**  
✅ **Attractive App Icon & Theming**  

---

## 🔧 Tech Stack

| Tool / Framework      | Purpose                        |
|-----------------------|--------------------------------|
| ![Android](https://img.shields.io/badge/-Android-3DDC84?logo=android&logoColor=white) | UI, Logic & Framework |
| ![Java](https://img.shields.io/badge/-Java-007396?logo=java&logoColor=white)           | Application Logic     |
| ![Firebase](https://img.shields.io/badge/-Firebase-FFCA28?logo=firebase&logoColor=black) | Authentication & Firestore DB |
| ![Material UI](https://img.shields.io/badge/-Material--UI-6200EE?logo=material-design&logoColor=white) | Modern UI Components  |

---

## 🔐 Firebase Integration

- **Authentication**: Email/Password Login & Signup  
- **Cloud Firestore**: Real-time NoSQL database to store per-user tasks  
- **Security Rules**: Implemented to restrict access to only the authenticated user’s data  
- **Timestamps**: Used for task sorting and validation

```js
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId}/tasks/{taskId} {
      allow read, delete: if request.auth != null && request.auth.uid == userId;
      allow create: if request.auth != null && request.auth.uid == userId
        && request.resource.data.timestamp is timestamp;
      allow update: if request.auth != null && request.auth.uid == userId
        && (!request.resource.data.keys().hasAny(['timestamp']) ||
            request.resource.data.timestamp == resource.data.timestamp);
    }
  }
}

```
---

---

## 🧪 Planned Features

| Feature                         | Description                                     | Status       |
|----------------------------------|-------------------------------------------------|--------------|
| 🔔 Push Notifications           | Task alerts and daily reminders                | ❌ Pending     |
| 📅 Due Date Integration          | Assign deadlines to tasks                      | ❌ Pending     |
| 📊 Task Analytics                | Visual insights into completion & habits       | ❌ Pending     |
| 🎨 Custom Themes                 | User-selectable color schemes and modes        | ⚙️ In Progress |
| 🌙 Dark Mode Support             | Support for light/dark themes                  | ✅ Completed   |
| 🔐 User Authentication          | Secure login and user-based task storage       | ✅ Completed   |
| 📝 Task CRUD Functionality      | Create, Read, Update, Delete tasks             | ✅ Completed   |
| 🗂️ Task Filtering & Sorting      | Filter tasks (all/completed/pending) + sort    | ✅ Completed   |
| 📤 Backup & Export               | Export tasks to file/cloud                     | ❌ Pending     |
| 🗓️ Calendar View                 | Visual task scheduling in calendar format      | ❌ Pending     |

---
---

## 📸 Screenshots

> Below are sample screenshots illustrating key features of *Mission Me*:

| Home Screen                        | Add Task                          | Edit Task                         |
|-----------------------------------|-----------------------------------|-----------------------------------|
| ![Home](screenshots/home.png)     | ![Add](screenshots/add_task.png)  | ![Edit](screenshots/edit_task.png)|

> *Note: Place your actual screenshots in a `screenshots/` directory.*

---

## 👨‍💻 Developer

| Name        | College                                     | Email                     |
|-------------|---------------------------------------------|---------------------------|
| **Rahul M** | St Aloysius (Deemed to be University), Mangalore | *Rahulchad34@gmail.com*     |

- 🎓 Developed as part of a **college semester project**
- 💬 Feel free to reach out for feedback or collaborations!

---



