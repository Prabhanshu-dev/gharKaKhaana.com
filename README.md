Overview

Gharkakhaana is a backend-driven food-tech platform designed to connect home food providers with end users seeking affordable, hygienic, and trustworthy home-cooked meals.
The project focuses on scalable backend architecture with future scope for AI-powered personalization and demand prediction.

This project is built to simulate real-world production systems, not just CRUD operations.


Problem Statement

Urban professionals and students often struggle to find:

hygienic and affordable home-cooked food

consistent quality and nutrition

reliable local providers

On the other side, many home cooks lack:

a digital platform to reach customers

demand visibility and planning tools

Gharkakhaana bridges this gap.

Key Features

👤 User & Vendor (Home Cook) Management

🍽️ Meal Listings & Availability

🛒 Order Placement & Lifecycle Tracking

🔐 Secure Authentication & Authorization

📦 Order Status Management

📊 Scalable backend design with AI-readiness

🏗️ System Architecture

Backend: Java, Spring Boot

Architecture Style: Microservices-based design

API Style: RESTful APIs

Authentication: Spring Security

Persistence: Relational Database (PostgreSQL / MySQL)

Build & CI: Maven, Jenkins

Testing: JUnit, Mockito

The system is designed with separation of concerns, clean domain modeling, and extensibility in mind.


AI & Data (Planned / In Progress)

The platform is designed to support Applied AI use-cases, including:

🔍 Meal recommendations based on user preferences

📈 Demand prediction for home cooks

🧮 Nutrition-based meal tagging

👥 User preference clustering

Current implementation focuses on backend readiness, with AI modules planned as incremental enhancements.

🗂️ Modules (High Level)

User Service – User registration, authentication, preferences

Vendor Service – Home cook onboarding & meal management

Order Service – Order lifecycle, tracking, and status updates

Recommendation Engine (Planned) – AI-driven personalization

🧪 Testing & Quality

Unit testing with JUnit & Mockito

API testing using Postman

Code quality checks via SonarQube (planned)


How to Run Locally
# Clone the repository
git clone https://github.com/your-username/gharkakhaana.git

# Navigate to project
cd gharkakhaana

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run


Learning Outcomes

Designing real-world backend systems

Applying microservices principles

Writing clean, testable Java code

Preparing systems for AI integration

Understanding product-level trade-offs

🔮 Future Enhancements

AI-based recommendation engine

Demand forecasting using ML models

Role-based dashboards

Cloud deployment (AWS)

Observability & monitoring

👨‍💻 Author

Prabhanshu
Software Development Engineer | Java Backend | AI Aspirant
