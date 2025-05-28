# AuthService

A robust authentication service built with Spring Boot, JWT, jOOQ, PostgreSQL, and Resend email.

## Features

- User registration with email verification
- JWT-based authentication
- Password management (activation and reset)
- Email sending via Resend API
- Clean architecture with layers (Controller → Service → Repository)
- PostgreSQL database with jOOQ for type-safe SQL
- Swagger/OpenAPI documentation

## Project Structure

```
src/main/java/com/acme/app/
├── AuthServiceApplication.java    # Main application entry point
├── common/                        # Shared components and utilities
│   ├── config/                    # Configuration classes
│   ├── exception/                 # Global exception handlers
│   ├── security/                  # Security configuration
│   ├── service/                   # Common services
│   └── util/                      # Utility classes
├── auth/                          # Authentication related components
│   ├── controller/                # Authentication endpoints
│   ├── dto/                       # Data transfer objects
│   ├── model/                     # Authentication domain models
│   ├── repository/                # Token repositories
│   └── service/                   # Authentication services
└── user/                          # User related components
    ├── controller/                # User endpoints
    ├── dto/                       # Data transfer objects
    ├── model/                     # User domain model
    ├── repository/                # User repository
    └── service/                   # User services
```

## Setup and Installation

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL database (or Neon account)
- Resend.com account for email

### Steps to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/authservice.git
   cd authservice
   ```

2. Configure database and email:
   - Update `src/main/resources/application.yml` with your database and Resend credentials
   - Or set environment variables:
     ```bash
     export JWT_SECRET=your_jwt_secret
     export RESEND_API_KEY=your_resend_api_key
     export RESEND_FROM_EMAIL=your_from_email
     export APP_FRONTEND_URL=your_frontend_url
     ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

4. The application will be available at:
   ```
   http://localhost:8080/api
   ```

## Authentication Flows

### Registration and Account Activation

![image](https://github.com/user-attachments/assets/dbecc27b-ab2d-49d7-a3c9-a5509a26f1a1)

### Login Flow

![image](https://github.com/user-attachments/assets/a08f03bb-bc53-4fa2-a7a9-5d24afde39d4)

### Password Reset Flow

![image](https://github.com/user-attachments/assets/511e8d4d-258f-43f5-afb6-dbea6e9aec01)

## API Endpoints

### User Endpoints

- **POST /api/users** - Register a new user
  ```json
  {
    "fullName": "John Doe",
    "email": "john@example.com"
  }
  ```

### Authentication Endpoints

- **POST /api/auth/login** - Login with email and password
  ```json
  {
    "email": "john@example.com",
    "password": "YourSecurePassword123!"
  }
  ```

- **POST /api/auth/activate** - Activate account with verification token
  ```json
  {
    "token": "verification-token-uuid",
    "password": "YourSecurePassword123!"
  }
  ```

- **POST /api/auth/forgot-password** - Request password reset
  ```json
  {
    "email": "john@example.com"
  }
  ```

- **POST /api/auth/reset-password** - Reset password with token
  ```json
  {
    "token": "reset-token-uuid",
    "password": "YourNewSecurePassword123!"
  }
  ```

## Testing with Swagger

1. Start the application
2. Access the Swagger UI at:
   ```
   http://localhost:8080/api/swagger-ui.html
   ```
   
   If that doesn't work, try one of these alternatives:
   ```
   http://localhost:8080/api/swagger-ui/index.html
   http://localhost:8080/api/swagger-ui/
   ```

3. Test the authentication flow:
   - Register a new user
   - Check your email for the activation link
   - Activate your account by setting a password
   - Login to receive a JWT token
   - Use the token in the "Authorize" button in Swagger
   - Test protected endpoints

## Configuration

### Database Configuration

The application uses PostgreSQL with jOOQ for type-safe SQL. The schema is defined in `src/main/resources/schema.sql` and includes:
- `users` table - Stores user information
- `verification_token` table - For email verification tokens
- `password_reset_token` table - For password reset tokens

### Security Configuration

- JWT-based authentication
- Token expiration: 24 hours by default
- Stateless authentication (no sessions)

### Email Configuration

The application uses Resend.com for sending transactional emails:
- Account verification emails
- Password reset emails

## Development

### jOOQ Code Generation

The jOOQ code is generated from the database schema:

1. Make sure your database is running with the correct schema
2. Run:
   ```bash
   ./mvnw generate-sources
   ```

### Running Tests

```bash
./mvnw test
```

### Building for Production

```bash
./mvnw clean package
```

This will generate a JAR file in the `target` directory.

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `JWT_SECRET` | Secret key for JWT signing | zG7gu2L85jLitcQe8K5YMXyJbsY0N1lW |
| `RESEND_API_KEY` | Resend API key | your-resend-api-key |
| `RESEND_FROM_EMAIL` | Email sender address | onboarding@resend.dev |
| `RESEND_FROM_NAME` | Email sender name | Auth Service |
| `APP_FRONTEND_URL` | URL of the frontend application | http://localhost:3000 |
