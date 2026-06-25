# 🥗 Smart Nutrition Tracker

A full-stack web application to help users track daily food intake, monitor macronutrients, and receive personalized meal recommendations — all secured with JWT authentication.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)
![MongoDB](https://img.shields.io/badge/MongoDB-NoSQL-brightgreen)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6-yellow)

---

## ✨ Features

- 🔐 Secure JWT-based user authentication (register/login)
- 📊 Real-time macro tracking (calories, protein, carbs, fat)
- 📅 Daily food diary with meal logging
- 📈 Weekly analytics with Chart.js visualizations
- 🤖 AI-driven smart meal recommendations
- ⭐ User feedback system with star ratings
- 🎨 Modern Glassmorphism UI design

---

## 🛠️ Tech Stack

### Frontend
- HTML5, CSS3, JavaScript (ES6)
- Chart.js for data visualization
- Glassmorphism design system with CSS Variables

### Backend
- Java 17
- Spring Boot 3
- Spring Security with JWT
- RESTful API architecture

### Database
- MongoDB (NoSQL)

---

## 📁 Project Structure

```
updated-smart-nutrition-tracker/
├── frontend/
│   ├── index.html
│   ├── css/
│   │   └── styles.css
│   └── js/
│       ├── api.js
│       └── app.js
└── backend/
    └── src/main/java/com/tracker/nutrition/
        ├── model/
        │   ├── User.java
        │   ├── FoodEntry.java
        │   └── Feedback.java
        ├── repository/
        ├── service/
        │   ├── NutritionService.java
        │   ├── AnalyticsService.java
        │   └── RecommendationService.java
        ├── controller/
        │   ├── AuthController.java
        │   ├── FoodEntryController.java
        │   ├── AnalyticsController.java
        │   ├── RecommendationController.java
        │   └── FeedbackController.java
        └── security/
            ├── SecurityConfig.java
            └── CustomUserDetailsService.java
```

---

## 🚀 Getting Started

### Prerequisites
Make sure you have the following installed:
- [Java 17+](https://www.oracle.com/java/technologies/downloads/)
- [Maven](https://maven.apache.org/download.cgi)
- [MongoDB](https://www.mongodb.com/try/download/community)
- [Node.js](https://nodejs.org/) (optional, for frontend tooling)

---

### Installation

#### 1. Clone the repository
```
git clone https://github.com/samsameer2804-cloud/updated-smart-nutrition-tracker.git
cd updated-smart-nutrition-tracker
```

#### 2. Start MongoDB
Make sure MongoDB is running locally on port 27017:
```
mongod
```

#### 3. Run the Backend
```
cd backend
mvn spring-boot:run
```
The backend will start on `http://localhost:8080`

#### 4. Open the Frontend
Open `frontend/index.html` directly in your browser
or use Live Server in VS Code.

---

## 🔗 API Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | /register | Register new user | ❌ |
| POST | /login | Login and get JWT token | ❌ |
| GET | /api/food/ | Get today's food entries | ✅ |
| POST | /api/food/ | Log a new meal | ✅ |
| GET | /api/analytics/ | Get weekly analytics data | ✅ |
| GET | /api/recommendations/ | Get smart meal suggestions | ✅ |
| POST | /api/feedback/ | Submit user feedback | ✅ |

---

## 🔄 How It Works

1. **Authentication** — User registers/logs in and receives a JWT token stored in localStorage
2. **Dashboard** — Frontend loads today's food entries automatically using the JWT token
3. **Logging Food** — User submits meal form, backend validates and saves to MongoDB
4. **Analytics** — Weekly calorie trends and highest calorie meals visualized via Chart.js
5. **Smart Suggestions** — Recommendations generated based on remaining daily macro targets

---

## 📸 Screenshots

> Coming soon

---

## 🤝 Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**samsameer2804-cloud**
- GitHub: [@samsameer2804-cloud](https://github.com/samsameer2804-cloud)
