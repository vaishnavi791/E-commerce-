# Frontend API Reference

This document describes the REST API currently implemented in this repository. URLs are relative to the running Spring Boot server. No API base path is configured by the controllers.

## Shared Rules

- Requests with a body use `Content-Type: application/json`.
- Protected requests use `Authorization: Bearer <token>`.
- `/auth/register` and `/auth/login` are public. Every other application endpoint requires authentication.
- Authentication is stateless JWT authentication. The JWT subject is the user's email.
- There are no role-specific route restrictions in `SecurityConfig`; an authenticated user can reach all protected routes.
- Unauthorized requests are returned as HTTP `401` with the response status/error produced by `JwtAuthEntryPoint`.
- Application errors use a JSON object such as `{ "message": "..." }` for not-found (`404`), duplicate-user (`409`), unauthorized-service (`401`), and validation (`400`) errors.

## 1. Authentication Endpoints

### Register

**METHOD:** `POST`  
**URL:** `/auth/register`  
**REQUEST BODY:**

```json
{
  "name": "Ava Customer",
  "email": "ava@example.com",
  "password": "secret"
}
```

`name` and `password` must not be blank; `email` must be a valid email address.

**HEADERS:** `Content-Type: application/json`  
**AUTHENTICATION REQUIRED:** No  
**RESPONSE FORMAT:** HTTP `201`, `AuthResponse`:

```json
{
  "token": "<jwt>",
  "message": "User registered successfully",
  "email": "ava@example.com"
}
```

**PURPOSE:** Creates a user with the `USER` role and returns a JWT.

### Login

**METHOD:** `POST`  
**URL:** `/auth/login`  
**REQUEST BODY:**

```json
{
  "email": "ava@example.com",
  "password": "secret"
}
```

**HEADERS:** `Content-Type: application/json`  
**AUTHENTICATION REQUIRED:** No  
**RESPONSE FORMAT:** HTTP `200`, `AuthResponse` with `token`, `message`, and `email` fields.  
**PURPOSE:** Verifies credentials and returns a JWT.

## 2. User Endpoints

### List Users

**METHOD:** `GET`  
**URL:** `/users`  
**REQUEST BODY:** NOT AVAILABLE  
**HEADERS:** `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `200`, an array of `UserDTO` objects:

```json
[
  {
    "id": 1,
    "name": "Ava Customer",
    "email": "ava@example.com",
    "role": "USER"
  }
]
```

**PURPOSE:** Returns all users. There is no single-user, create-user, update-user, or delete-user endpoint in `UserController`.

## 3. Product Endpoints

`ProductDTO` request fields are `pName`, `price`, `quantity`, `category`, `subcategory`, `brand`, and `imageUrl`. Text fields must not be blank, `price` must be at least `1`, and `quantity` must be at least `0`.

### Add Product

**METHOD:** `POST`  
**URL:** `/products`  
**REQUEST BODY:**

```json
{
  "id": null,
  "pName": "Running Shoes",
  "price": 79.99,
  "quantity": 10,
  "category": "Footwear",
  "subcategory": "Sneakers",
  "brand": "Veja",
  "imageUrl": "https://images.unsplash.com/example"
}
```

**HEADERS:** `Content-Type: application/json`; `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `201`, a `ProductDTO` object with `id`, `pName`, `price`, `quantity`, `category`, `subcategory`, `brand`, and `imageUrl`.
**PURPOSE:** Creates a product.

### List Products

**METHOD:** `GET`  
**URL:** `/products`  
**REQUEST BODY:** NOT AVAILABLE  
**HEADERS:** `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `200`, an array of `ProductDTO` objects containing `id`, `pName`, `price`, `quantity`, `category`, `subcategory`, `brand`, and `imageUrl`.
**PURPOSE:** Returns all products.

### Get Product

**METHOD:** `GET`  
**URL:** `/products/{id}`  
**REQUEST BODY:** NOT AVAILABLE  
**HEADERS:** `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `200`, one `ProductDTO` with `id`, `pName`, `price`, `quantity`, `category`, `subcategory`, `brand`, and `imageUrl`.
**PURPOSE:** Returns a product by database ID.

### Update Product

**METHOD:** `PUT`  
**URL:** `/products/{id}`  
**REQUEST BODY:** Same `ProductDTO` structure as `POST /products`.  
**HEADERS:** `Content-Type: application/json`; `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `200`, a `ProductDTO` with `id`, `pName`, `price`, `quantity`, `category`, `subcategory`, `brand`, and `imageUrl`.
**PURPOSE:** Replaces the editable fields of an existing product.

### Delete Product

**METHOD:** `DELETE`  
**URL:** `/products/{id}`  
**REQUEST BODY:** NOT AVAILABLE  
**HEADERS:** `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `204`, no response body.  
**PURPOSE:** Deletes a product by database ID.

## 4. Category Endpoints

**NOT AVAILABLE.** There is no `Category` model, repository, service, DTO, or controller. The product `category` is only a required plain string field.

## 5. Cart Endpoints

Cart responses use `CartDTO` with `cartId`, `productIds`, and `productNames`. The `productId` field exists in the DTO for the add request and is not populated by the cart response mapper, so it may serialize as `null`.

### Add Item

**METHOD:** `POST`  
**URL:** `/cart/items`  
**REQUEST BODY:**

```json
{
  "productId": 1
}
```

`productId` is required.

**HEADERS:** `Content-Type: application/json`; `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `201`, `CartDTO` containing the cart ID, product IDs, and product names.  
**PURPOSE:** Adds the product to the authenticated user's cart. The implementation stores repeated product references for quantity, but does not accept a quantity in this request.

### View Cart

**METHOD:** `GET`  
**URL:** `/cart`  
**REQUEST BODY:** NOT AVAILABLE  
**HEADERS:** `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `200`, `CartDTO` with `cartId`, `productIds`, and `productNames`.  
**PURPOSE:** Returns the authenticated user's cart, creating an empty cart when none exists.

### Update Item Quantity

**METHOD:** `PUT`  
**URL:** `/cart/items/{productId}?quantity={quantity}`  
**REQUEST BODY:** NOT AVAILABLE  
**HEADERS:** `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `200`, updated `CartDTO`.  
**PURPOSE:** Sets the number of references to a product. A quantity of `0` removes it.

### Remove Item

**METHOD:** `DELETE`  
**URL:** `/cart/items/{productId}`  
**REQUEST BODY:** NOT AVAILABLE  
**HEADERS:** `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `200`, updated `CartDTO`.  
**PURPOSE:** Removes all references to the product from the cart.

### Clear Cart

**METHOD:** `DELETE`  
**URL:** `/cart`  
**REQUEST BODY:** NOT AVAILABLE  
**HEADERS:** `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `204`, no response body.  
**PURPOSE:** Removes all products from the authenticated user's cart.

## 6. Order Endpoints

Order responses use `OrderDTO` with `id`, `orderDate`, `status`, `totalAmount`, `productIds`, and `customerEmail`.

### Checkout

**METHOD:** `POST`  
**URL:** `/orders/checkout`  
**REQUEST BODY:** NOT AVAILABLE  
**HEADERS:** `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `201`, one `OrderDTO`.  
**PURPOSE:** Creates an order from the authenticated user's cart and clears the cart. The total is calculated from product prices.

### Order History

**METHOD:** `GET`  
**URL:** `/orders`  
**REQUEST BODY:** NOT AVAILABLE  
**HEADERS:** `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `200`, an array of `OrderDTO` objects.  
**PURPOSE:** Returns orders belonging to the authenticated user.

### Cancel Order

**METHOD:** `PUT`  
**URL:** `/orders/{orderId}/cancel`  
**REQUEST BODY:** NOT AVAILABLE  
**HEADERS:** `Authorization: Bearer <token>`  
**AUTHENTICATION REQUIRED:** Yes  
**RESPONSE FORMAT:** HTTP `200`, the updated `OrderDTO`.  
**PURPOSE:** Sets the selected order's status to `CANCELLED`.

## 7. Search, Filter, and Sort

**NOT AVAILABLE.** There are no controller query parameters, repository finder methods, or service methods for product search, category filtering, price filtering, or sorting. `GET /products` always returns the complete unpaged list.

## 8. Pagination

**NOT AVAILABLE.** Product, user, and order list endpoints return `List` values and do not accept pagination parameters or return Spring `Page` metadata.

## 9. Product Images

Product images are supported through the `imageUrl` string field. The URL is stored in MySQL and returned by the product endpoints; image files are not stored in MySQL. There is no multipart upload endpoint.

## 10. CORS

**NOT CONFIGURED.** No `CorsConfiguration`, CORS filter, `WebMvcConfigurer`, or `@CrossOrigin` annotation was found. A browser frontend hosted on another origin may therefore need backend CORS configuration before it can call this API directly.
