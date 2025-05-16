
# MortgageEase - Microservices Backend

This repository contains the backend microservices for **MortgageEase**, a cloud-native Loan Eligibility & Pre-Approval System. Built with Java, Spring Boot, and AWS, this architecture demonstrates real-world software engineering practices using distributed services and secure API gateways.

---

## Microservice Architecture

```

microservices/
├── application-service/       # Manages loan applications and status tracking
├── gateway-service/           # API gateway and request router with JWT validation
├── loan-evaluation-service/   # Applies business rules and evaluates loan eligibility
├── notification-service/      # (Optional) Sends email/in-app notifications
├── user-service/              # Handles user registration, login, and JWT token issuance

````

---

## Tech Stack

- **Java 17**, **Spring Boot**
- **Spring Cloud Gateway**
- **Spring Security + JWT**
- **MongoDB** (NoSQL)
- **Gradle** (per-service build)
- **Docker** (optional containerization)
- **Postman** (for manual API testing)
- (Optional: AWS ECS, Secrets Manager, S3)

---

## Security Model

- The `user-service` generates JWT tokens after user login.
- The `gateway-service` authenticates incoming requests using the token.
- Routes like `/auth/**` are public.
- All other microservices (`/api/applications`, `/api/evaluation`, etc.) require a valid JWT with `ROLE_USER`.

---

## How to Run Locally

Each service is a standalone Spring Boot app with its own `application.yml`.

1. Clone the repo:
```bash
   git clone git@github.com:wapo-isai/loan-preapproval-microservices.git
   cd microservices
````

2. Start MongoDB locally (e.g., via Docker or installed MongoDB):

```bash
   docker run -d -p 27017:27017 --name mongodb mongo:latest
```

3. Start the services in this order:
    * `user-service`
    * `loan-evaluation-service`
    * `application-service`
    * `gateway-service`

4. Test with Postman or frontend:

    * Register/Login via: `POST http://localhost:8080/auth/register`
    * Submit application via: `POST http://localhost:8080/api/applications`

---

## API Endpoints (via Gateway)

| Service              | Path                         | Method |
| -------------------- | ---------------------------- | ------ |
| User Registration    | `/auth/register`             | `POST` |
| User Login           | `/auth/login`                | `POST` |
| Submit Loan App      | `/api/applications`          | `POST` |
| Check Loan Status    | `/api/applications/{userId}` | `GET`  |
| Evaluate Eligibility | `/api/evaluation`            | `POST` |

> All secured endpoints require: `Authorization: Bearer <JWT>`

---

## Folder Structure

```
microservices/
├── user-service/
│   └── src/main/java/com/loanapproval/userservice
├── loan-evaluation-service/
├── application-service/
├── gateway-service/
├── notification-service/ (optional)
├── .gitignore
└── README.md
```

---

## Useful Scripts

* Generate JWT key:

```java
  SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
  String base64 = Encoders.BASE64.encode(key.getEncoded());
```

* Clean & Build all:

```bash
  ./gradlew clean build
```

---

## Status

* Core services implemented
* Secure JWT authentication via gateway
* Notification service is optional/in progress
* MongoDB schema defined and populated via Postman
* Gateway validates JWT for secured routes

---

## Author

**Isai Martinez**
Cloud Software Engineer & Full-Stack Developer
[LinkedIn](https://www.linkedin.com/in/isai-martinez/) | [GitHub](https://github.com/wapo-isai/)

---

## License

MIT License

```
Would you like this exported as a `.md` file or placed in your folder structure directly with a CLI script?
```
