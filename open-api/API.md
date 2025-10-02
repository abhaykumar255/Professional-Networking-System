# Professional Networking System API Documentation

## Overview

This document provides comprehensive API documentation for the Professional Networking System, a microservices-based professional networking platform built with Spring Boot.

### Base URLs
- **Local Development**: `http://localhost:9000/api/v1`
- **Production**: `http://34.107.215.249/api/v1`

### Authentication
All endpoints (except authentication endpoints) require JWT Bearer token authentication.

```http
Authorization: Bearer <your-jwt-token>
```

## Table of Contents

1. [User Service API](#user-service-api)
2. [Posts Service API](#posts-service-api)
3. [Connection Service API](#connection-service-api)
4. [Notification Service API](#notification-service-api)
5. [Health Check API](#health-check-api)
6. [Error Handling](#error-handling)
7. [Data Models](#data-models)

---

## User Service API

### Authentication Endpoints

#### User Registration
```http
POST /users/auth/signup
```

**Request Body:**
```json
{
  "name": "Abhay Kumar",
  "email": "abhay@gmail.com",
  "password": "securepassword"
}
```

**Response (201):**
```json
{
  "id": 1,
  "name": "Abhay Kumar",
  "email": "abhay@gmail.com",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

#### User Login
```http
POST /users/auth/login
```

**Request Body:**
```json
{
  "email": "abhay@gmail.com",
  "password": "securepassword"
}
```

**Response (200):**
```json
"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Reset Password
```http
POST /users/auth/reset-password
```

**Request Body:**
```json
{
  "email": "abhay@gmail.com",
  "newPassword": "newsecurepassword"
}
```

**Response (200):** Success message

#### Get User by ID
```http
GET /users/auth/{userId}
```

**Parameters:**
- `userId` (path, required): User ID

**Response (200):**
```json
{
  "id": 1,
  "name": "Abhay Kumar",
  "email": "abhay@gmail.com",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

---

## Posts Service API

### Post Management

#### Create Post
```http
POST /posts/core
```

**Request Body:**
```json
{
  "content": "Excited to share my latest project on microservices architecture!"
}
```

**Response (201):**
```json
{
  "id": 1,
  "content": "Excited to share my latest project on microservices architecture!",
  "userId": 1,
  "authorName": "Abhay Kumar",
  "likeCount": 0,
  "isLikedByCurrentUser": false,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

#### Get All Posts
```http
GET /posts/core?page=0&size=10
```

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 10)

**Response (200):**
```json
[
  {
    "id": 1,
    "content": "Excited to share my latest project!",
    "userId": 1,
    "authorName": "Abhay Kumar",
    "likeCount": 5,
    "isLikedByCurrentUser": true,
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T10:30:00Z"
  }
]
```

#### Get Post by ID
```http
GET /posts/core/{postId}
```

**Parameters:**
- `postId` (path, required): Post ID

#### Update Post
```http
PUT /posts/core/{postId}
```

**Parameters:**
- `postId` (path, required): Post ID

**Request Body:**
```json
{
  "content": "Updated post content with new insights!"
}
```

#### Delete Post
```http
DELETE /posts/core/{postId}
```

**Parameters:**
- `postId` (path, required): Post ID

**Response (204):** No content

#### Get Posts by User
```http
GET /posts/core/users/{userId}/posts?page=0&size=10
```

**Parameters:**
- `userId` (path, required): User ID
- `page` (optional): Page number
- `size` (optional): Page size

### Post Interactions

#### Like Post
```http
POST /posts/likes/{postId}
```

**Parameters:**
- `postId` (path, required): Post ID

**Response (200):**
```json
true
```

#### Unlike Post
```http
DELETE /posts/likes/{postId}
```

**Parameters:**
- `postId` (path, required): Post ID

**Response (200):**
```json
false
```

#### Get Like Count
```http
GET /posts/likes/{postId}/count
```

**Parameters:**
- `postId` (path, required): Post ID

**Response (200):**
```json
15
```

---

## Connection Service API

### Connection Management

#### Get First Degree Connections
```http
GET /connections/core/first-degree
```

**Response (200):**
```json
[
  {
    "id": 1,
    "userId": 2,
    "name": "John Doe",
    "email": "john@example.com"
  }
]
```

#### Get Second Degree Connections (Suggestions)
```http
GET /connections/core/second-degree
```

**Response (200):**
```json
[
  {
    "id": 2,
    "userId": 3,
    "name": "Jane Smith",
    "email": "jane@example.com"
  }
]
```

### Connection Requests

#### Send Connection Request
```http
POST /connections/core/request/{userId}
```

**Parameters:**
- `userId` (path, required): Target user ID

**Response (200):**
```json
true
```

#### Accept Connection Request
```http
POST /connections/core/accept/{userId}
```

**Parameters:**
- `userId` (path, required): Sender user ID

**Response (200):**
```json
true
```

#### Reject Connection Request
```http
POST /connections/core/reject/{userId}
```

**Parameters:**
- `userId` (path, required): Sender user ID

**Response (200):**
```json
true
```

#### Get Pending Requests
```http
GET /connections/core/pending-requests
```

**Response (200):**
```json
[
  {
    "id": 1,
    "senderId": 2,
    "receiverId": 1,
    "senderName": "John Doe",
    "receiverName": "Abhay Kumar",
    "status": "PENDING",
    "createdAt": "2024-01-15T10:30:00Z"
  }
]
```

#### Get Sent Requests
```http
GET /connections/core/sent-requests
```

**Response (200):**
```json
[
  {
    "id": 2,
    "senderId": 1,
    "receiverId": 3,
    "senderName": "Abhay Kumar",
    "receiverName": "Jane Smith",
    "status": "PENDING",
    "createdAt": "2024-01-15T11:00:00Z"
  }
]
```

---

## Notification Service API

### Notification Management

#### Get User Notifications
```http
GET /notifications?page=0&size=10&unreadOnly=false
```

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 10)
- `unreadOnly` (optional): Filter unread notifications only (default: false)

**Response (200):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "type": "CONNECTION_REQUEST",
    "title": "New Connection Request",
    "message": "John Doe wants to connect with you",
    "isRead": false,
    "relatedEntityId": 2,
    "createdAt": "2024-01-15T10:30:00Z"
  }
]
```

#### Mark Notification as Read
```http
PUT /notifications/{notificationId}/read
```

**Parameters:**
- `notificationId` (path, required): Notification ID

**Response (200):** Success

#### Mark All Notifications as Read
```http
PUT /notifications/mark-all-read
```

**Response (200):** Success

#### Get Unread Count
```http
GET /notifications/unread-count
```

**Response (200):**
```json
5
```

---

## Health Check API

### Service Health Monitoring

#### User Service Health
```http
GET /users/actuator/health
```

#### Posts Service Health
```http
GET /posts/actuator/health
```

#### Connection Service Health
```http
GET /connections/actuator/health
```

#### Notification Service Health
```http
GET /notifications/actuator/health
```

**Health Response Example:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "redis": {
      "status": "UP",
      "details": {
        "version": "7.0.0"
      }
    }
  }
}
```

---

## Error Handling

All API endpoints return standardized error responses:

### Error Response Format
```json
{
  "error": "Bad Request",
  "message": "Validation failed for field 'email'",
  "statusCode": "BAD_REQUEST",
  "status": 400,
  "timestamp": "2024-01-15T10:30:00Z",
  "path": "/api/v1/users/auth/signup",
  "method": "POST",
  "traceId": "abc123def456",
  "validationErrors": {
    "email": "Email format is invalid"
  },
  "details": [
    "Email must be a valid email address"
  ]
}
```

### Common HTTP Status Codes

| Status Code | Description |
|-------------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 204 | No Content - Request successful, no content returned |
| 400 | Bad Request - Invalid request data |
| 401 | Unauthorized - Authentication required |
| 403 | Forbidden - Access denied |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server error |

---

## Data Models

### User Models

#### SignUpRequestDTO
```json
{
  "name": "string (2-100 chars, required)",
  "email": "string (email format, required)",
  "password": "string (6-100 chars, required)"
}
```

#### LoginRequestDTO
```json
{
  "email": "string (email format, required)",
  "password": "string (required)"
}
```

#### UserDTO
```json
{
  "id": "integer (int64)",
  "name": "string",
  "email": "string (email format)",
  "createdAt": "string (date-time)"
}
```

### Post Models

#### PostCreateRequestDTO
```json
{
  "content": "string (1-5000 chars, required)"
}
```

#### PostDTO
```json
{
  "id": "integer (int64)",
  "content": "string",
  "userId": "integer (int64)",
  "authorName": "string",
  "likeCount": "integer",
  "isLikedByCurrentUser": "boolean",
  "createdAt": "string (date-time)",
  "updatedAt": "string (date-time)"
}
```

### Connection Models

#### PersonDTO
```json
{
  "id": "integer (int64)",
  "userId": "integer (int64)",
  "name": "string",
  "email": "string (email format)"
}
```

#### ConnectionRequestDTO
```json
{
  "id": "integer (int64)",
  "senderId": "integer (int64)",
  "receiverId": "integer (int64)",
  "senderName": "string",
  "receiverName": "string",
  "status": "enum [PENDING, ACCEPTED, REJECTED]",
  "createdAt": "string (date-time)"
}
```

### Notification Models

#### NotificationDTO
```json
{
  "id": "integer (int64)",
  "userId": "integer (int64)",
  "type": "enum [CONNECTION_REQUEST, CONNECTION_ACCEPTED, POST_LIKED, POST_CREATED]",
  "title": "string",
  "message": "string",
  "isRead": "boolean",
  "relatedEntityId": "integer (int64)",
  "createdAt": "string (date-time)"
}
```

---

## Rate Limiting

The API implements rate limiting to ensure fair usage:

- **Default Rate**: 10 requests per second
- **Burst Capacity**: 20 requests
- **User-specific Rate Limiting**: Applied per authenticated user
- **Posts Service**: 20 requests per second, 40 burst capacity

Rate limit headers are included in responses:
```http
X-RateLimit-Remaining: 15
X-RateLimit-Reset: 1642248600
```

---

## Examples

### Complete User Registration Flow

1. **Register User**
```bash
curl -X POST http://localhost:9000/api/v1/users/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "securepassword"
  }'
```

2. **Login User**
```bash
curl -X POST http://localhost:9000/api/v1/users/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "securepassword"
  }'
```

3. **Use JWT Token**
```bash
curl -X GET http://localhost:9000/api/v1/posts/core \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Create and Interact with Posts

1. **Create Post**
```bash
curl -X POST http://localhost:9000/api/v1/posts/core \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Just deployed my microservices project!"
  }'
```

2. **Like Post**
```bash
curl -X POST http://localhost:9000/api/v1/posts/likes/1 \
  -H "Authorization: Bearer <token>"
```

### Connection Management

1. **Send Connection Request**
```bash
curl -X POST http://localhost:9000/api/v1/connections/core/request/2 \
  -H "Authorization: Bearer <token>"
```

2. **Accept Connection Request**
```bash
curl -X POST http://localhost:9000/api/v1/connections/core/accept/2 \
  -H "Authorization: Bearer <token>"
```

---

## Support

For API support and questions:
- **GitHub Issues**: [Report bugs and request features](https://github.com/abhaykumar255/Professional-Networking-System/issues)
- **Email**: support@professionalnetworking.com
- **Documentation**: [Complete API Reference](http://34.107.201.81:9020/users/swagger-ui.html)

---

**Last Updated**: January 2024  
**API Version**: 1.0.0  
**License**: MIT