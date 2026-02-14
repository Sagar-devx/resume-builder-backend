# 📌 Resume Builder Backend (Spring Boot 4.0.2)

A production-ready backend service for a Resume Builder platform built using **Spring Boot 4.0.2**.  
This backend provides user authentication, resume management, secure file uploads, email notifications, and online payments.

---

## 🚀 Features

- 🔐 **User Authentication using Spring Security + JWT**
- 📝 **Resume Creation & Management APIs**
- 🌥️ **Cloudinary Integration for Image/File Uploads**
- ✉️ **Email Notifications via Brevo SMTP**
- 💳 **Online Payment Integration using Razorpay**
- 🗄️ **MongoDB Database Support**
- 🧩 **Lombok for reducing boilerplate**
- 🧪 **Test dependencies for MongoDB, Validation, Web, Actuator**
- 🌍 **REST API for frontend integration**
- ⚙️ **Environment variable–based secure configuration**

---

## 🛠️ Tech Stack

| Technology | Role |
|-----------|------|
| **Spring Boot 4.0.2** | Core backend framework |
| **Spring Security** | Authentication + Authorization |
| **JWT (jjwt 0.11.5)** | Token-based security |
| **MongoDB** | NoSQL Database |
| **Cloudinary SDK** | File upload & storage |
| **Razorpay (1.4.6)** | Payment Gateway |
| **Brevo SMTP** | Email Service |
| **Lombok** | Code simplification |
| **Maven** | Project & dependency management |

---

## 📦 Dependencies Used 

- spring-boot-starter-webmvc  
- spring-boot-starter-security  
- spring-boot-starter-data-mongodb  
- spring-boot-starter-validation  
- spring-boot-starter-mail  
- spring-boot-starter-actuator  
- jjwt-api, jjwt-impl, jjwt-jackson  
- cloudinary-http44  
- razorpay-java  
- lombok  

---

## 📁 Project Structure

src<br>
├── main<br>
│ ├── java/com/sagar/resume<br>
│ │ ├── Config/ → Application configurations<br>
│ │ ├── Controller/ → REST Controllers (API endpoints)<br>
│ │ ├── Document/ → MongoDB documents/entities<br>
│ │ ├── Dto/ → Data Transfer Objects<br>
│ │ ├── Exception/ → Custom exceptions & handlers<br>
│ │ ├── Repository/ → MongoDB repositories<br>
│ │ ├── Security/ → JWT + Spring Security configuration<br>
│ │ ├── Service/ → Business logic layer<br>
│ │ ├── Util/ → Utility/helper classes<br>
│ │ └── ResumeBuilderApplication.java → Main Spring Boot file<br>
│ │<br>
│ └── resources/<br>
│        ├── application.properties<br>
│<br>
└── test/java/com/sagar/resume/ → Unit tests

---

## 🔐 Environment Variables Required

Your application expects these values in your system environment or `.env`:

BREVO_SMTP_HOST=<br>
BREVO_SMTP_USERNAME=<br>
BREVO_SMTP_PASSWORD=<br>
BREVO_SMTP_FROM_EMAIL=<br>

CLOUDINARY_CLOUD_NAME=<br>
CLOUDINARY_API_KEY=<br>
CLOUDINARY_API_SECRET=<br>

RAZORPAY_KEY_ID=<br>
RAZORPAY_KEY_SECRET=<br>

JWT_SECRET=<br>

MONGO_URI=mongodb://localhost:27017/resumeBuilder

---

## 🔗 API Endpoints

### 🔐 Authentication (`/api/auth`)
- POST /register  
- POST /login  
- POST /verify-email  
- POST /resend-verification  
- GET  /profile  
- POST /upload-image  

### 📄 Resumes (`/api/resumes`)
- GET /  
- POST /  
- GET /{id}  
- PUT /{id}  
- DELETE /{id}  
- POST /{id}/upload-images  

### ✉ Email (`/api/email`)
- POST /send-resume  

### 💳 Payments (`/api/payment`)
- POST /create-order  
- POST /verify-payment  
- GET /order/{orderId}  

### 🎨 Templates (`/api/templates`)
- GET /

---

## 🧰 How to Run the Project

### 1. Clone the repository
git clone https://github.com/Sagar-devx/resume-builder-backend.git<br>
cd resume-builder-backend

### 2. Add environment variables
Create a `.env` or set system variables.

### 3. Install dependencies
mvn clean install

### 4. Run the project
mvn spring-boot:run




