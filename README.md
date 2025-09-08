  # 📋 NOVACARE BACKEND MVC PROJECT

A **secure, scalable, and robust RESTful API backend** for healthcare management built with **Spring Boot**, supporting **Two-Factor Authentication (2FA)**, **role-based authorization**, and comprehensive management of doctors, appointments, duty rosters, and more.

---

## 📚 Table of Contents

- [✨ Features](#✨features)  
- [🛠 Tech Stack & Dependencies](#🛠-tech-stack--dependencies)  
- [🏗 Architecture](#🏗-architecture)  
- [🔐 Security & Authentication](#🔐-security--authentication)  
- [🚀 Getting Started](#🚀-getting-started)  
- [🐳 Docker Setup](#🐳-docker-setup)  
- [⚙ Environment Variables / Configuration](#⚙-environment-variables--configuration)  
- [🧪 Testing](#🧪-testing)  
- [📊 Logging & Monitoring](#📊-logging--monitoring)  
- [🤝 Contributing](#🤝-contributing)  
- [📄 License](#📄-license)  
- [📞 Contact](#📞-contact)

---

## ✨ Features

✅ **User Authentication & Authorization** with JWT tokens  
✅ **Two-Factor Authentication (2FA)** with OTP via email or SMS  
✅ **Role-Based Access Control** for Admin, Internal Users, and Clients  
✅ **Doctor Management:**  
  - Create, Edit, Inactivate doctors  
  - Add doctor's profile image  
✅ **Specialization Management:**  
  - Create, Edit, Delete specializations  
✅ **Internal User Management:**  
  - Add and manage internal users  
  - Handle patients and normal users  
✅ **Patient Management:**  
  - Internal users can manage patient records  
✅ **Duty Roster Management:**  
  - Create duty rosters with available slots and schedules  
✅ **File Upload Support:**  
  - Upload profile images and documents  
✅ **CORS Configuration** to enable frontend integration  
✅ **Stateless Session Management** for scalability  
✅ **Exception Handling & Validation** for better error management  
✅ **Database Migrations** with Hibernate  
✅ **Docker Support** for containerized deployments  
✅ **Secure configurations** including password hashing and JWT encryption

---

## 🛠 Tech Stack & Dependencies

### Frameworks & Libraries
- **Spring Boot 3.x** – Simplified development and configuration  
- **Spring Security** – Authentication and authorization  
- **Spring Data JPA** – ORM layer with Hibernate  
- **JWT (JSON Web Token)** – Stateless authentication  
- **Lombok** – Reducing boilerplate code  
- **MySQL/PostgreSQL** – Relational database  
- **Hibernate Validator** – Input validation  
- **Apache Commons IO / File Upload** – Handling multipart requests  
- **Spring Mail** – Email OTP services  
- **Swagger/OpenAPI** – API documentation (optional)  

### Build & Dev Tools
- **Maven** – Build management  
- **Docker** – Containerization  
- **JUnit & Mockito** – Unit and integration testing  
- **Logback** – Logging framework

---

## 🏗 Architecture

```
Frontend <-> API Gateway <-> Spring Boot Backend <-> Database
```

- Stateless REST architecture  
- JWT-based authentication and authorization  
- Secure API routing with role-based access  
- Modular layers: Controllers, Services, Repositories, Security Filters  

---

## 🔐 Security & Authentication

### ✅ Two-Factor Authentication (2FA)

1. **Login Flow:**
   - User sends username and password.
   - Backend validates credentials.
   - Generates and sends OTP via email or SMS.
   - User submits OTP.
   - Backend verifies OTP and issues JWT.

2. **Authorization Flow:**
   - JWT token must be included in the `Authorization: Bearer <token>` header.
   - Access restricted based on user role (`ROLE_ADMIN`, `ROLE_INTERNAL`, `ROLE_USER`).

3. **Security Filters:**
   - `JwtAuthenticationFilter`: Validates requests.
   - CORS enabled for trusted frontend origins.

4. **Password Management:**
   - BCrypt password encoder used for secure storage.

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java JDK 17 or higher  
- Maven 3.6 or higher  
- MySQL or PostgreSQL database  
- Docker (optional)  
- SMTP provider (Gmail, SendGrid) for OTP emails  

### ✅ Installation Steps

1. **Clone the repository:**

   ```bash
   git clone https://github.com/SACHINUPADHYAY414/Novacare-Backend-MVC.git
   cd Novacare-Backend-MVC
   ```

2. **Configure the database connection in `application.properties`:**

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/novacare
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Configure email/SMS service for OTP:**

   ```properties
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_email_password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   ```

4. **Build and Run Locally:**

   ```bash
   mvn clean package -DskipTests
   java -jar target/healthcare-0.0.1-SNAPSHOT.jar
   ```

5. **Access APIs:**

   Visit `http://localhost:8080/api` and explore the functionality via frontend integration.

---

## 🐳 Docker Setup

1. **Build Docker Image:**

   ```bash
   docker build -t novacare-backend .
   ```

2. **Run Docker Container:**

   ```bash
   docker run -d -p 8080:8080 --name novacare novacare-backend
   ```

3. **Use `docker-compose.yml` (optional) for multi-container setups.**

---

## ⚙ Environment Variables / Configuration

| Property                | Description                   |
|-----------------------|------------------------------|
| `spring.datasource.*`  | Database connection details |
| `spring.mail.*`        | Email server for OTPs      |
| `jwt.secret`           | Secret key for token signing |
| `jwt.expiration`       | Token expiration time       |
| `cors.allowedOrigins`  | Frontend URLs allowed       |

Example:

```properties
jwt.secret=mysecretkey
jwt.expiration=3600000
cors.allowedOrigins=http://localhost:3000
```

---

## 🧪 Testing

Run tests with:

```bash
mvn test
```

Use **JUnit 5** and **Mockito** for unit tests. Integration tests can be configured with an in-memory database.

---

## 📊 Logging & Monitoring

- **Logback** configured with different levels (INFO, DEBUG, ERROR).  
- Customize logging format in `logback-spring.xml`.  
- Can integrate with monitoring tools like Prometheus or ELK stack.

---

## 🤝 Contributing

Contributions, issues, and pull requests are welcome!  
Steps to contribute:

1. Fork the repository  
2. Create a feature branch  
3. Make your changes  
4. Test thoroughly  
5. Submit a pull request with detailed description

Please follow coding conventions and ensure tests pass.

---

## 📞 Contact

**Sachin Upadhyay**  
📧 Email: upadhyaysachin@example.com  
📱 Contact: +91 7294890821

---

## 📄 License

This project is licensed under the **MIT License** – see the [LICENSE](LICENSE) file for details.

---

Built with ❤️ using **Spring Boot**, **JWT**, and **2FA** for secure healthcare management.
