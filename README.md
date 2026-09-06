# Mira: E-Commerce with AI/ML Recommendation System

A full-stack women's e-commerce platform built with **React, Spring Boot, MySQL, and a Python FastAPI recommendation service**. The project includes user authentication, product browsing, cart management, wishlist, checkout, order management, and an AI/ML-based product recommendation system.
---

# ✨ Features

### 👤 User Management

* User registration
* User login
* JWT-based authentication
* Protected backend APIs
* Role-based security structure

### 🛍️ Product Management

* Browse products
* Product details
* Category and subcategory organization
* Product search
* Product images
* Brand and description information

### 🛒 Shopping Cart

* Add products to cart
* Update product quantity
* Remove products
* Calculate cart total
* Checkout

### ❤️ Wishlist

* Add/remove products from wishlist
* View wishlist

### 📦 Orders

* Place orders through checkout
* View order history
* Track order details
* Order status management

### 🤖 AI/ML Recommendations

* Tracks user-product interactions
* Uses weighted interactions
* Identifies preferred product subcategories
* Uses TF-IDF to represent product content
* Uses cosine similarity to measure product similarity
* Prioritizes the user's strongest product preference
* Provides fallback recommendations when sufficient data is unavailable

---

# 🏗️ System Architecture

```text
                         ┌─────────────────────┐
                         │    React Frontend    │
                         │     Vite + React     │
                         └──────────┬──────────┘
                                    │
                           HTTP REST Requests
                                    │
                         Authorization: Bearer JWT
                                    │
                                    ▼
                    ┌────────────────────────────┐
                    │     Spring Boot Backend    │
                    │                            │
                    │ Controllers                │
                    │ Services                   │
                    │ Repositories               │
                    │ Spring Security + JWT      │
                    └─────────────┬──────────────┘
                                  │
                     ┌────────────┴────────────┐
                     │                         │
                     ▼                         ▼
             ┌───────────────┐       ┌─────────────────────┐
             │ MySQL Database│       │ Python Recommendation│
             │               │       │ FastAPI Service      │
             │ Users         │       │                      │
             │ Products      │       │ TF-IDF               │
             │ Cart          │       │ Cosine Similarity    │
             │ Orders        │       │ User Interactions    │
             │ Interactions  │       │                      │
             └───────────────┘       └─────────────────────┘
```

---

# 🧩 Technology Stack

## Frontend

* React.js

## Backend

* Java 17
* Spring Boot 3.5.4
* Spring Data JPA
* Spring Security
* JWT
* Lombok

## Database

* MySQL

## Recommendation Service

* Python
* FastAPI
* TF-IDF
* Cosine Similarity

## Testing

* Postman
* Swagger UI

---

# 📁 Project Structure

```text
E-Commerce-Backend-System/
│
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │           └── ...
│   ├── pom.xml
│   └── postman_collection.json
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── context/
│   │   └── services/
│   │
│   ├── package.json
│   └── vite.config.js
│
├── recommendation-service/
│   ├── main.py
│   ├── requirements.txt
│   └── README.md
│
└── README.md
```

### Main entities

* `User`
* `Product`
* `Cart`
* `CartItem`
* `Order`
* `OrderItem`
* `UserInteraction`

The `UserInteraction` data is particularly important for the recommendation system because it provides the behavioral signals used to infer user preferences.

---

# 🌐 Important API Endpoints

The exact endpoints should be checked against the current controller implementation, but the application is organized around APIs such as:

| Area            | Method | Endpoint           |
| --------------- | ------ | ------------------ |
| Authentication  | POST   | `/auth/register`   |
| Authentication  | POST   | `/auth/login`      |
| Products        | GET    | `/products`        |
| Products        | GET    | `/products/{id}`   |
| Cart            | POST   | `/cart/items`      |
| Cart            | GET    | `/cart`            |
| Cart            | PUT    | `/cart/items/{id}` |
| Cart            | DELETE | `/cart/items/{id}` |
| Orders          | POST   | `/orders/checkout` |
| Orders          | GET    | `/orders`          |
| Recommendations | GET    | `/recommendations` |


# 🚀 Installation and Setup

## 1. Clone the Repository

```bash
git clone <your-github-repository-url>
cd E-Commerce-Backend-System
```

---

# 🗄️ 2. Configure MySQL

Create a MySQL database for the project.

Example:

```sql
CREATE DATABASE ecommerce;
```
configuration:

```text
backend/src/main/resources/application.properties
```

---

# ☕ 3. Run the Spring Boot Backend

```bash
cd backend
```
Run:

```bash
mvn spring-boot:run
```
runs on:

```text
http://localhost:8080
```

---

# ⚛️ 4. Run the React Frontend

```bash
cd frontend
```

Install dependencies & Run frontend:

```bash
npm install
```

```bash
npm run dev
```
runs on:

```text
http://localhost:5173
```
---

# 🤖 5. Run the Recommendation Service

```bash
cd recommendation-service
```

Create a virtual environment:

### Windows

```bash
python -m venv .venv
```
Activate it:

```powershell
.venv\Scripts\Activate.ps1
```

Install dependencies & Run FastAPI:

```bash
pip install -r requirements.txt
```

```bash
python -m uvicorn main:app --reload
```
runs on:

```text
http://localhost:8000
```

---

# 🔄 User Flow

## 1. Registration / Login

```text
User
 ↓
React Login/Register
 ↓
Spring Boot AuthController
 ↓
AuthService
 ↓
MySQL User
 ↓
JWT generated
 ↓
JWT returned to frontend
```

The frontend uses the JWT when accessing protected APIs.

---

## 2. Browsing Products

```text
User opens Products page
        ↓
React sends GET request
        ↓
Spring Boot ProductController
        ↓
ProductService
        ↓
ProductRepository
        ↓
MySQL
        ↓
Products returned to React
```

---

## 3. Product Interaction

User actions can generate recommendation signals such as:

```text
VIEW
CART_ADD
PURCHASE
```

These interactions are associated with the user and product.

The recommendation system uses these interactions to understand user preferences.

---

# 🛒 Order / Checkout Flow

The main checkout flow is:

```text
User
 ↓
Add Product
 ↓
POST /cart/items
 ↓
Cart updated
 ↓
User opens Checkout
 ↓
POST /orders/checkout
 ↓
Backend authenticates user using JWT
 ↓
Backend loads user's cart
 ↓
Order is created
 ↓
Order items are created
 ↓
Total is calculated
 ↓
Cart is cleared
 ↓
Order response returned
```

The important point is that **the frontend does not decide whether the user is authenticated**. The backend validates the JWT and establishes the authenticated user context before processing protected requests.

---

# 🔐 Authentication and JWT

Mira uses **JWT-based authentication** with Spring Security.

### Login flow

```text
React
 ↓
POST /auth/login
 ↓
Spring Boot
 ↓
Validate username/email + password
 ↓
Generate JWT
 ↓
Return JWT
 ↓
Frontend stores token
```

For subsequent protected requests:

```http
Authorization: Bearer <JWT>
```

The backend processes the token through the Spring Security authentication flow.


# 🤖 AI/ML Recommendation System

Mira uses a **content-based recommendation approach** implemented as a separate Python FastAPI service.

### Technologies

* Python
* FastAPI
* Scikit-learn
* TF-IDF
* Cosine Similarity

---

## Recommendation Problem

The initial recommendation approach used product text such as:

```text
category + subcategory + brand + description
```

and calculated cosine similarity across products.

A problem with this approach was that products from different subcategories could be recommended simply because their descriptions contained similar generic words.

For example:

```text
User preference:
Jewellery

Potential recommendation:
Watch
```

Even though both products belonged to the broader `Accessories` category, the textual similarity could cause the watch to receive a high similarity score.

---

# 🎯 Hierarchical Recommendation Strategy

To make recommendations more meaningful, the recommendation logic was designed around the user's **subcategory preference first**, followed by content similarity.

The general strategy is:

```text
User Interactions
       ↓
Calculate weighted preference
       ↓
Find strongest subcategory
       ↓
Filter/prioritize products from that subcategory
       ↓
TF-IDF + Cosine Similarity
       ↓
Rank products
       ↓
If insufficient products:
expand to other preferred subcategories
       ↓
Broader category/fallback candidates
       ↓
Return recommendations
```

---

# 📊 Interaction Weighting

Different interactions represent different levels of user interest.

| Interaction | Weight |
| ----------- | -----: |
| VIEW        |      1 |
| CART_ADD    |      2 |
| PURCHASE    |      3 |


# 🧠 TF-IDF

**TF-IDF** stands for **Term Frequency-Inverse Document Frequency**.

It converts textual information into numerical vectors.

For the recommendation model, product content can be represented using:

```text
brand + description
```

For example:

```text
"Zara floral printed cotton dress"
```

is converted into a numerical vector.

Words that are useful for distinguishing products receive greater importance than words that appear frequently across many products.

---

# 📐 Cosine Similarity

Cosine similarity measures how similar two vectors are.

Conceptually:

```text
Similarity(A, B)
        ↓
Compare their TF-IDF vectors
        ↓
Higher cosine similarity
        =
More similar product content
```

A value closer to `1` means the vectors point in a more similar direction.

The recommendation system uses this score to rank products **within the appropriate candidate group**.

---
**Mira is a full-stack women's e-commerce platform built with React, Spring Boot, MySQL, and a Python-based TF-IDF recommendation service that provides personalized, subcategory-aware product recommendations based on user interactions.**

