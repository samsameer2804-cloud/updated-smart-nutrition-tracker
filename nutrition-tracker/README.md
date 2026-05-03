# Smart Nutrition Tracker

A full-stack, data-dense nutrition tracking application inspired by clinical UI designs. This application allows users to log their daily food intake, track macronutrients in real-time, and view comprehensive nutritional summaries.

## 🚀 Technologies Used
* **Frontend:** Vanilla HTML, CSS, JavaScript (ES6)
* **Backend:** Java 17, Spring Boot 3, Spring Security (JWT)
* **Database:** MongoDB Atlas (NoSQL)
* **Design System:** Glassmorphism, CSS Grid/Flexbox, Dynamic CSS Variables

## ✨ Features
* **User Authentication:** Secure signup and login with hashed passwords.
* **Macronutrient Tracking:** Real-time calculation of Calories, Protein, Carbohydrates, and Fats.
* **Clinical UI Dashboard:** Data-dense, tabular diary view for efficient food logging.
* **Dynamic Progress Bars:** Visual indicators of daily macronutrient goals.
* **RESTful API:** Fully functional Spring Boot backend handling cross-origin requests and data persistence.

## 📂 Project Structure
* `/frontend`: Contains the UI assets (HTML, CSS, JS) and API communication logic.
* `/backend`: Contains the Spring Boot Java application, MongoDB configurations, and security filters.

## 🛠️ How to Run Locally

### Backend Setup
1. Ensure Java 17+ and Maven are installed.
2. Navigate to the `backend` directory.
3. Open `src/main/resources/application.properties` and add your MongoDB Atlas URI.
4. Run the application: `mvn spring-boot:run`
5. The API will start on `http://localhost:8085`.

### Frontend Setup
1. Navigate to the `frontend` directory.
2. Open `index.html` in any modern web browser.
3. The app will automatically connect to the local backend.
