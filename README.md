# 🍔 FoodAuto

A **Full Stack Food Ordering Web Application** built using **Java, JSP, Servlets, JDBC, MySQL, HTML, CSS, and JavaScript**.

FoodAuto simulates the workflow of a modern food delivery platform where users can register, browse restaurants, order food, manage their cart, and track their orders through a clean and user-friendly interface.

---

## 📖 About the Project

FoodAuto is a full-stack web application developed to gain practical experience in Java web development and understand how online food ordering systems work.

The project demonstrates user authentication, restaurant browsing, menu management, shopping cart functionality, order processing, session management, and database connectivity using Java EE technologies.

---

## ✨ Features

### 👤 Customer Module

- Secure User Registration
- User Login & Logout
- Browse Restaurants
- Search Restaurants
- View Restaurant Menus
- Add Items to Cart
- Update Cart Quantity
- Remove Items from Cart
- Checkout Process
- Multiple Payment Options (UI)
- Order Confirmation
- Order History
- User Profile

### 🛠️ Admin Module

- Restaurant Management
- Menu Management
- Customer Management
- Order Management

---

## 🛠 Tech Stack

| Category | Technologies |
|----------|--------------|
| **Frontend** | HTML5, CSS3, JavaScript, JSP |
| **Backend** | Java, Servlets |
| **Database** | MySQL |
| **Database Connectivity** | JDBC |
| **Web Server** | Apache Tomcat |
| **IDE** | Eclipse IDE |
| **Version Control** | Git & GitHub |

---

## 🏗️ Project Architecture

The application follows the **MVC (Model-View-Controller)** Architecture.

```
User
   │
   ▼
JSP Pages (View)
   │
   ▼
Servlets (Controller)
   │
   ▼
DAO Layer + Model
   │
   ▼
MySQL Database
```

---

## 📂 Project Structure

```
FoodAuto
│
├── src
│   ├── Controller
│   ├── DAO
│   ├── DAOImplementation
│   ├── Model
│   └── Utility
│
├── WebContent
│   ├── CSS
│   ├── Images
│   ├── JSP Pages
│   └── HTML Pages
│
├── screenshots
│
└── README.md
```

---

# 📸 Application Screenshots

## 🔐 Login

![Login](screenshots/login.png)

---

## 📝 Registration

![Registration](screenshots/register.png)

---

## 🏠 Home Page

![Home](screenshots/home.png)

---

## 🍽 Restaurant Listing

![Restaurant](screenshots/home1.png)

---

## 📖 Restaurant Menu

![Menu](screenshots/menu.png)

---

## 🛒 Shopping Cart

![Cart](screenshots/cart%20(2).png)

---

## 📦 Checkout

![Checkout](screenshots/checkout.png)

---

## 💳 Payment

![Payment](screenshots/payment.png)

---

## ✅ Order Success

![Order Success](screenshots/ordersuccess.png)

---

## ⚙️ Installation Guide

### 1. Clone the Repository

```bash
git clone https://github.com/Mjn-001/Food-Auto.git
```

### 2. Open in Eclipse

Import the project as a **Dynamic Web Project**.

### 3. Configure Apache Tomcat

Add Apache Tomcat to Eclipse and deploy the project.

### 4. Configure MySQL

Create the required database and import the SQL file.

Update your JDBC credentials before running.

Example:

```java
String url = "jdbc:mysql://localhost:3306/foodauto";
String username = "root";
String password = "your_password";
```

### 5. Run the Project

Start Apache Tomcat and open:

```
http://localhost:8080/FoodAuto/
```

---

## 🔄 Application Workflow

```
Login / Register
        │
        ▼
Browse Restaurants
        │
        ▼
View Menu
        │
        ▼
Add to Cart
        │
        ▼
Checkout
        │
        ▼
Select Payment Method
        │
        ▼
Place Order
        │
        ▼
Order Confirmation
```

---

## 📚 What I Learned

Through this project, I gained practical experience in:

- Java Full Stack Development
- Java Servlets
- JSP
- JDBC
- MySQL Database
- MVC Architecture
- DAO Design Pattern
- Session Management
- CRUD Operations
- Database Connectivity
- Git & GitHub

---

## 🚀 Future Enhancements

- Online Payment Gateway Integration
- Email Notifications
- Live Order Tracking
- Restaurant Ratings & Reviews
- Google Maps Integration
- Responsive Mobile UI
- Spring Boot Migration
- REST API Development
- Docker Deployment

---

## 👨‍💻 Developer

**Manjunath N**

Engineering Student | Java Full Stack Developer | Content Creator

**GitHub:** https://github.com/Mjn-001

---

## ⭐ Support

If you found this project useful, consider giving it a **Star ⭐** on GitHub.

It motivates me to build more exciting projects and continue learning.

---

**Thank you for visiting my repository! 🚀**
