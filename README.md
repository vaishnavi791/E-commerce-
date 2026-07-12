# E-Commerce Backend

A production-style Spring Boot backend for an e-commerce application with JWT authentication, product management, cart operations, and order handling.

## Project Overview

This project provides a layered REST API backend built with Java 17, Spring Boot 3.5.x, Spring Security, JWT, Spring Data JPA, MySQL, and Maven. Incoming client requests are intercepted by Spring Security and validated using stateless JWT tokens before reaching the controller layer. Validated requests then transition smoothly through specialized service and repository layers to perform secure data operations on the MySQL database.

## Tech Stack
### Tech Stack

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.5.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![JWT](https://img.shields.io/badge/JWT_Authentication-black?style=for-the-badge&logo=JSON-web-tokens&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-red?style=for-the-badge)
![Swagger](https://img.shields.io/badge/OpenAPI_/_Swagger-85EA2D?style=for-the-badge&logo=openapi-initiative&logoColor=black)

## Folder Structure

```text
src/
  main/
    java/com/ecommerce/backend/
      config/
      controller/
      dto/
      exception/
      model/
      repository/
      security/
      service/
      util/
    resources/application.properties
```

## Installation

1. Clone the repository.
2. Create a MySQL database named `ecommerce`.
3. Update the database credentials in `src/main/resources/application.properties`.
4. Run:

```bash
./mvnw spring-boot:run
```

## MySQL Setup

```sql
CREATE DATABASE ecommerce;
```

## API Endpoints

### Authentication
- POST `/auth/register`
- POST `/auth/login`

### Products
- POST `/products`
- GET `/products`
- GET `/products/{id}`
- PUT `/products/{id}`
- DELETE `/products/{id}`

### Cart
- POST `/cart/items`
- GET `/cart`
- PUT `/cart/items/{productId}`
- DELETE `/cart/items/{productId}`
- DELETE `/cart`

### Orders
- POST `/orders/checkout`
- GET `/orders`
- PUT `/orders/{orderId}/cancel`

## JWT Authentication

Use the token returned by `/auth/login` in the `Authorization` header:

```http
Authorization: Bearer <token>
```

## Postman Screenshots

A Postman collection can be generated from the listed endpoints for manual API testing.

Testing User Registration on Postman:
<img width="1422" height="776" alt="Screenshot 2026-07-12 130541" src="https://github.com/user-attachments/assets/2220df41-44a6-4e22-bdeb-230bb6efa256" />


Testing User Login on Postman:
<img width="1421" height="772" alt="Screenshot 2026-07-12 130808" src="https://github.com/user-attachments/assets/5a1cabe5-325f-462e-83b1-3755bfb1baea" />


## Future Improvements

- Add a minimal frontend using react.js
- Add payment integration
- Introduce inventory management
- Add pagination and filtering
- Add full integration tests
