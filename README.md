# 🏦 Banking Microservices System

A production-ready, cloud-native banking application built with **Spring Boot** and **Spring Cloud**.  
This system handles user authentication, account management, loan processing, transactions, email and push notifications — all as independent microservices.

---

## 📌 Table of Contents

- [Architecture Overview](#architecture-overview)
- [Microservices Documentation](#microservices-documentation)
  - [Eureka Server](#1-eureka-server)
  - [Config Server](#2-config-server)
  - [API Gateway](#3-api-gateway)
  - [Auth Service](#4-auth-service)
  - [User Service](#5-user-service)
  - [Account Service](#6-account-service)
  - [Transaction Service](#7-transaction-service)
  - [Loan Service](#8-loan-service--banking-loan)
  - [Notification Service](#9-notification-service)
  - [Email Service](#10-email-service)
  - [Logging Service](#11-logging-service)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Configuration & Secrets](#configuration--secrets)
- [Running the Services](#running-the-services)
- [Security](#security)
- [Project Structure](#project-structure)

---

## 🏗️ Architecture Overview

```
Client (Browser / Mobile App)
            │
            ▼
  ┌─────────────────────┐
  │      API Gateway    │  ← Port 8080
  │  Routing, Auth      │
  │  Filter, Rate Limit │
  └──────────┬──────────┘
             │
    ┌────────┴──────────────────────────────────┐
    │                                           │
    ▼                                           ▼
┌──────────────┐   ┌──────────────┐   ┌─────────────────┐
│ Auth Service │   │ User Service │   │ Account Service │
│  Port: 8081  │   │  Port: 8082  │   │   Port: 8083    │
└──────────────┘   └──────────────┘   └─────────────────┘
    │                                           │
    ▼                                           ▼
┌──────────────┐   ┌──────────────┐   ┌─────────────────┐
│ Transaction  │   │ Loan Service │   │  Notification   │
│  Port: 8085  │   │  Port: 8084  │   │   Port: 8086    │
└──────────────┘   └──────────────┘   └─────────────────┘
    │
    ▼
┌──────────────┐   ┌──────────────┐
│    Email     │   │   Logging    │
│  Port: 8087  │   │  Port: 8088  │
└──────────────┘   └──────────────┘

Supporting Infrastructure:
┌──────────────────────────────────────────────────┐
│  Eureka Server (8761)  │  Config Server (8888)   │
└──────────────────────────────────────────────────┘
```

---

## 🧩 Microservices Documentation

---

### 1. Eureka Server
**Port:** `8761`

#### What it does
Acts as the **Service Registry**. All microservices register themselves here on startup. Services discover each other through Eureka instead of hardcoded URLs.

#### Key Features
- Service registration and discovery
- Health monitoring of all services
- Load balancing support
- Dashboard to view all running services

#### Dashboard
```
http://localhost:8761
```

#### Configuration
```yaml
server:
  port: 8761
spring:
  application:
    name: eureka-server
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

> ⚠️ **Must start FIRST** before all other services

---

### 2. Config Server
**Port:** `8888`

#### What it does
Centralized configuration management. All microservices fetch their configuration from this server. It reads config from a **GitHub repository**.

#### Key Features
- Centralized config for all services
- Environment-specific configs (dev, prod)
- Config refresh without restart
- Secured with GitHub token

#### Configuration
```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/Rahul21000/config-repo.git
          username: Rahul21000
          password: ${GITHUB_TOKEN}
          default-label: banking-loan-prod
```

#### How Other Services Connect
```yaml
# In each microservice bootstrap.yml
spring:
  cloud:
    config:
      uri: http://localhost:8888
```

> ⚠️ **Must start SECOND** — after Eureka Server

---

### 3. API Gateway
**Port:** `8080`

#### What it does
Single entry point for all client requests. Routes requests to the correct microservice, validates JWT tokens, applies rate limiting, and handles CORS.

#### Key Features
- Request routing to all microservices
- JWT token validation before forwarding
- Rate limiting per user/IP
- CORS configuration
- Circuit breaker with fallback responses
- Centralized logging of all requests

#### Routing Rules
| Route | Forwarded To |
|---|---|
| `/api/auth/**` | Auth Service (8081) |
| `/api/users/**` | User Service (8082) |
| `/api/accounts/**` | Account Service (8083) |
| `/api/loans/**` | Loan Service (8084) |
| `/api/transactions/**` | Transaction Service (8085) |
| `/api/notifications/**` | Notification Service (8086) |

#### Public Endpoints (No Auth Required)
```
POST /api/auth/register
POST /api/auth/login
```

#### Protected Endpoints (JWT Required)
```
All other endpoints require:
Authorization: Bearer <your_jwt_token>
```

---

### 4. Auth Service
**Port:** `8081`

#### What it does
Handles all authentication and authorization. Issues JWT tokens on login, validates tokens for other services, and manages user roles.

#### Key Features
- User registration
- Login with JWT token generation
- Token validation (used by API Gateway)
- Role-based access control (ADMIN, USER)
- BCrypt password encryption
- JWT cookie management

#### API Endpoints

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | ❌ Public | Register new user |
| POST | `/api/auth/login` | ❌ Public | Login and get JWT token |
| POST | `/api/auth/validate` | ✅ Required | Validate JWT token |
| POST | `/api/auth/logout` | ✅ Required | Logout user |
| GET | `/api/roles` | ✅ ADMIN | Get all roles |
| POST | `/api/roles` | ✅ ADMIN | Create new role |

#### Request/Response Examples

**Register:**
```json
POST /api/auth/register
{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "password": "password123",
  "role": "USER"
}
```

**Login:**
```json
POST /api/auth/login
{
  "email": "rahul@example.com",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "expiresIn": 86400000
}
```

#### Configuration
```properties
secretKey=${JWT_SECRET}
jwtExpirationMs=86400000
jwtCookieName=jwt
```

#### Database
```sql
Database: auth_service
Tables: users, roles
```

---

### 5. User Service
**Port:** `8082`

#### What it does
Manages customer profiles. Handles customer registration details, profile updates, and provides customer data to other services via Feign clients.

#### Key Features
- Customer CRUD operations
- Customer profile management
- Role assignment
- Integration with Auth Service for registration
- Integration with Account Service for account creation

#### API Endpoints

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/users` | ❌ Public | Create new customer |
| GET | `/api/users/{id}` | ✅ Required | Get customer by ID |
| GET | `/api/users` | ✅ ADMIN | Get all customers |
| PUT | `/api/users/{id}` | ✅ Required | Update customer profile |
| DELETE | `/api/users/{id}` | ✅ ADMIN | Delete customer |
| GET | `/api/config` | ✅ Required | Get config values |

#### Request Example
```json
POST /api/users
{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "phone": "9876543210",
  "address": "Indore, MP"
}
```

#### Database
```sql
Database: user_service
Tables: customers, roles
```

---

### 6. Account Service
**Port:** `8083`

#### What it does
Manages bank accounts. Handles account creation, balance management, and account status updates. Communicates with Transaction Service for balance updates.

#### Key Features
- Bank account creation (Savings, Current)
- Balance enquiry
- Account status management (ACTIVE, INACTIVE, BLOCKED)
- Auto-generated account numbers
- Integration with Transaction Service

#### API Endpoints

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/accounts` | ✅ Required | Create new account |
| GET | `/api/accounts/{id}` | ✅ Required | Get account by ID |
| GET | `/api/accounts/user/{userId}` | ✅ Required | Get accounts by user |
| PUT | `/api/accounts/{id}/status` | ✅ ADMIN | Update account status |
| GET | `/api/accounts/{id}/balance` | ✅ Required | Get account balance |

#### Request Example
```json
POST /api/accounts
{
  "customerId": 1,
  "accountType": "SAVINGS",
  "initialDeposit": 5000.00
}

Response:
{
  "accountNumber": "RB123456789",
  "accountType": "SAVINGS",
  "balance": 5000.00,
  "status": "ACTIVE"
}
```

#### Database
```sql
Database: account_service
Tables: accounts
```

---

### 7. Transaction Service
**Port:** `8085`

#### What it does
Handles all money movements — deposits, withdrawals, and transfers. Maintains full transaction history and integrates with Email Service for transaction alerts.

#### Key Features
- Deposit money
- Withdraw money
- Transfer between accounts
- Full transaction history
- Insufficient balance validation
- Email notification on every transaction
- Integration with Account Service for balance updates

#### API Endpoints

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/transactions/deposit` | ✅ Required | Deposit money |
| POST | `/api/transactions/withdraw` | ✅ Required | Withdraw money |
| POST | `/api/transactions/transfer` | ✅ Required | Transfer money |
| GET | `/api/transactions/{id}` | ✅ Required | Get transaction by ID |
| GET | `/api/transactions/account/{accountId}` | ✅ Required | Get account transactions |
| GET | `/api/config` | ✅ Required | Get config values |

#### Request Examples

**Deposit:**
```json
POST /api/transactions/deposit
{
  "accountNumber": "RB123456789",
  "amount": 10000.00,
  "paymentMethod": "NEFT"
}
```

**Transfer:**
```json
POST /api/transactions/transfer
{
  "fromAccountNumber": "RB123456789",
  "toAccountNumber": "RB987654321",
  "amount": 5000.00
}
```

#### Database
```sql
Database: transaction_service
Tables: transactions, payment_methods
```

---

### 8. Loan Service / Banking Loan
**Port:** `8084` / `8089`

#### What it does
Manages the complete loan lifecycle — from application to approval to EMI management. Includes AI-based loan prediction and approval workflow.

#### Key Features
- Loan application submission
- Loan eligibility check
- Admin approval/rejection workflow
- EMI calculation
- AI-based loan prediction scoring
- Payment tracking
- Email notification on approval/rejection

#### API Endpoints

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/loans/apply` | ✅ Required | Apply for loan |
| GET | `/api/loans/{id}` | ✅ Required | Get loan details |
| GET | `/api/loans/customer/{customerId}` | ✅ Required | Get customer loans |
| PUT | `/api/loans/{id}/approve` | ✅ ADMIN | Approve loan |
| PUT | `/api/loans/{id}/reject` | ✅ ADMIN | Reject loan |
| GET | `/api/loans` | ✅ ADMIN | Get all loans |
| POST | `/api/loans/predict` | ✅ Required | Loan eligibility prediction |
| POST | `/api/transactions/payment` | ✅ Required | Make loan EMI payment |

#### Request Example
```json
POST /api/loans/apply
{
  "customerId": 1,
  "loanAmount": 500000.00,
  "loanType": "HOME_LOAN",
  "tenureMonths": 120,
  "annualIncome": 800000.00
}

Response:
{
  "loanId": "LN2024001",
  "status": "PENDING",
  "emiAmount": 5372.00,
  "interestRate": 8.5
}
```

#### Database
```sql
Database: loan_service
Tables: customers, loans, accounts, transactions, payment_methods
```

---

### 9. Notification Service
**Port:** `8086`

#### What it does
Handles push notifications and in-app alerts. Sends real-time notifications to users for important banking events.

#### Key Features
- Push notification on transactions
- Loan status notifications
- Account activity alerts
- Notification history

#### Events That Trigger Notifications
```
✅ Money deposited
✅ Money withdrawn
✅ Transfer sent / received
✅ Loan application submitted
✅ Loan approved / rejected
✅ EMI due reminder
✅ Account status changed
```

---

### 10. Email Service
**Port:** `8087`

#### What it does
Handles all email communications using HTML email templates — welcome emails, transaction alerts, and loan status updates.

#### Key Features
- Welcome email on registration
- Transaction confirmation emails
- Loan approval/rejection emails
- HTML email templates
- SMTP integration (Gmail)

#### Email Templates
```
📧 Welcome Email        → on new registration
📧 Transaction Alert    → on deposit/withdrawal/transfer
📧 Loan Status Update   → on loan approval/rejection
📧 EMI Reminder         → before EMI due date
```

#### Configuration
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${EMAIL_USERNAME}
spring.mail.password=${EMAIL_PASSWORD}
```

---

### 11. Logging Service
**Port:** `8088`

#### What it does
Centralized logging for all microservices. Collects, stores, and provides searchable logs from all services for debugging and monitoring.

#### Key Features
- Centralized log collection from all services
- Request/response logging
- Error tracking and alerting
- Service health monitoring

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| Language | Java 17+ |
| Framework | Spring Boot 3.x |
| Service Discovery | Spring Cloud Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Config Management | Spring Cloud Config |
| Service Communication | OpenFeign |
| Security | Spring Security + JWT |
| Database | MySQL 8.0 |
| ORM | Spring Data JPA / Hibernate |
| Email | Spring Mail (SMTP) |
| Build Tool | Apache Maven |

---

## 🚀 Getting Started

### Prerequisites

| Tool | Version |
|---|---|
| Java | 17 or higher |
| Maven | 3.8+ |
| MySQL | 8.0+ |
| Git | Latest |

### Clone the Repository

```bash
git clone https://github.com/Rahul21000/banking-microservices-system.git
cd banking-microservices-system
```

---

## ⚙️ Configuration & Secrets

### Step 1 — Create MySQL Databases

```sql
CREATE DATABASE auth_service;
CREATE DATABASE user_service;
CREATE DATABASE account_service;
CREATE DATABASE loan_service;
CREATE DATABASE transaction_service;
```

### Step 2 — Create application-local.properties

Create this file in EACH service under `src/main/resources/`:

```properties
# ⚠️ DO NOT PUSH THIS FILE — it is already in .gitignore

# Database
DB_USERNAME=root
DB_PASSWORD=your_mysql_password

# JWT (for auth-service)
JWT_SECRET=your_jwt_secret_key_here

# GitHub Token (for config-server only)
GITHUB_TOKEN=your_github_personal_access_token

# Email (for email service only)
EMAIL_USERNAME=your_email@gmail.com
EMAIL_PASSWORD=your_email_app_password
```

### Step 3 — Generate GitHub Personal Access Token

1. Go to → [github.com/settings/tokens](https://github.com/settings/tokens)
2. Click **Generate new token (classic)**
3. Select scope: `repo`
4. Copy token → paste in `application-local.properties`

---

## ▶️ Running the Services

### ⚠️ Start in This Exact Order

```bash
# 1. Eureka Server — Service Registry
cd eureka-server && mvn spring-boot:run

# 2. Config Server — Centralized Config
cd config-server && mvn spring-boot:run

# 3. Core Services
cd auth-service        && mvn spring-boot:run
cd user-service        && mvn spring-boot:run
cd account-service     && mvn spring-boot:run

# 4. Business Services
cd transaction-service && mvn spring-boot:run
cd loan-service        && mvn spring-boot:run

# 5. Supporting Services
cd notification-service && mvn spring-boot:run
cd email               && mvn spring-boot:run
cd logging-service     && mvn spring-boot:run

# 6. API Gateway — Start Last
cd api-gateway && mvn spring-boot:run
```

### Verify All Services Running

Open Eureka Dashboard:
```
http://localhost:8761
```
All services should show **UP** ✅

---

## 🔐 Security

### JWT Authentication Flow

```
1. Register   →  POST /api/auth/register
2. Login      →  POST /api/auth/login      →  Get JWT Token
3. Use Token  →  Add to every request:
                 Authorization: Bearer eyJhbGci...
4. Gateway    →  Validates token before routing
5. Access     →  Request reaches microservice ✅
```

### Roles

| Role | Access |
|---|---|
| `USER` | Own profile, accounts, transactions, loans |
| `ADMIN` | Everything + approve loans, manage all users |

---

## 📁 Project Structure

```
banking-microservices-system/
│
├── .gitignore                   ← Ignores secrets, target/, .idea/
├── README.md                    ← This file
│
├── eureka-server/               ← Service Registry
├── config-server/               ← Centralized Config
├── api-gateway/                 ← Single Entry Point
│
├── auth-service/                ← JWT Authentication
├── user-service/                ← Customer Management
├── account-service/             ← Bank Accounts
├── transaction-service/         ← Money Transfers
├── loan-service/                ← Loan Management
├── banking-loan/                ← Legacy Loan App
│
├── notification-service/        ← Push Notifications
├── email/                       ← Email Notifications
└── logging-service/             ← Centralized Logging
```

Each service follows:
```
service-name/
└── src/main/
    ├── java/com/rbank/
    │   ├── controller/      ← REST APIs
    │   ├── service/         ← Business Logic
    │   ├── model/           ← Database Entities
    │   ├── repository/      ← JPA Repositories
    │   ├── dto/             ← Data Transfer Objects
    │   └── exception/       ← Exception Handlers
    └── resources/
        ├── application.yml              ✅ pushed to GitHub
        └── application-local.properties ❌ NOT pushed (secrets)
```

---

## 👨‍💻 Author

**Rahul** — [@Rahul21000](https://github.com/Rahul21000)

---

## 📄 License

This project is built for educational and portfolio purposes.
