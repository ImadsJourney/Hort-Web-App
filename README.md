
# Hort Manager

A Spring Boot application for managing daycare / after-school care groups, children, attendance status and notes.

The project was built as a practical backend application for a real school/daycare context. The goal is to provide teachers and educators with a simple system to manage groups, children and daily attendance information.

## Features

* User registration and login
* JWT-based authentication
* Password hashing with BCrypt
* Group management for daycare/school groups
* Child management per group
* Attendance status tracking
* Notes for individual children
* PostgreSQL persistence
* REST API with validation
* Unit and controller tests
* Docker Compose setup for backend and database
* GitHub Actions CI workflow for automated build and tests

## Tech Stack

* Java 21
* Spring Boot
* Spring Web
* Spring Security
* Spring Data JPA
* PostgreSQL
* Maven
* JUnit 5
* Mockito
* MockMvc
* Docker
* Docker Compose
* GitHub Actions

## Project Structure

```text
src/main/java/com/schoolms/school_management
├── auth        # Registration, login and JWT generation
├── child       # Child management, attendance and notes
├── config      # Security configuration
├── hortgroup   # Group management
└── user        # User entity and repository
```

## Security

The application uses JWT-based authentication.

Public endpoints:

```text
POST /auth/register
POST /auth/login
GET  /health
```

All other endpoints require a valid JWT Bearer Token:

```http
Authorization: Bearer <token>
```

Passwords are not stored in plain text. They are hashed with BCrypt before being persisted.

JWT secrets and database credentials are provided through environment variables and are not committed to the repository.

## Environment Variables

Create a local `.env` file based on `.env.example`:

```bash
cp .env.example .env
```

Example:

```env
POSTGRES_USER=user
POSTGRES_PASSWORD=password
POSTGRES_DB=schooldb
JWT_SECRET=replace-with-your-own-long-secret-key
```

The `.env` file contains local secrets and must not be committed.

## Run with Docker Compose

Start the complete application including PostgreSQL:

```bash
docker compose up -d --build
```

The backend will be available at:

```text
http://localhost:8080
```

Stop the containers:

```bash
docker compose down
```

Stop the containers and remove the database volume:

```bash
docker compose down -v
```

## Run Tests

```bash
./mvnw test
```

Or, if Maven is installed locally:

```bash
mvn test
```

## Build Application

```bash
./mvnw clean package
```

## API Examples

### Register User

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"teacher1","password":"password123"}'
```

### Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"teacher1","password":"password123"}'
```

The response contains a JWT token.

### Store Token in Fish Shell

```fish
set TOKEN "paste-token-here"
```

### Create Group

```bash
curl -X POST http://localhost:8080/groups \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"name":"Bärengruppe","gradeLevel":"1. Klasse","supervisorName":"Frau Müller"}'
```

### Get Groups

```bash
curl http://localhost:8080/groups \
  -H "Authorization: Bearer <token>"
```

### Create Child

```bash
curl -X POST http://localhost:8080/children \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"firstName":"Ali","lastName":"Yilmaz","notes":"Darf alleine nach Hause gehen.","hortGroupId":1}'
```

### Get Children

```bash
curl http://localhost:8080/children \
  -H "Authorization: Bearer <token>"
```

### Update Attendance

```bash
curl -X PATCH http://localhost:8080/children/1/attendance \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"attendanceStatus":"PRESENT"}'
```

### Update Notes

```bash
curl -X PATCH http://localhost:8080/children/1/notes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"notes":"Wurde um 15:30 Uhr abgeholt."}'
```

## Testing Approach

The project contains tests for controller and service layers.

Examples:

* Auth controller tests with MockMvc
* Auth service tests with Mockito
* Child controller and service tests
* Hort group controller and service tests

The tests focus on:

* HTTP status codes
* JSON response structure
* request validation
* service logic
* repository interaction through mocks
* authentication flow behavior

## CI/CD

The project includes a GitHub Actions workflow that automatically runs on push and pull requests.

The workflow:

* checks out the repository
* sets up Java 21
* caches Maven dependencies
* runs tests
* builds the application

This ensures that every pushed change is automatically verified.

## Development Notes

This project was developed with a focus on clean backend structure, testability and practical deployment.

The main goals were:

* build a real-world Java/Spring backend
* apply authentication and basic security principles
* use PostgreSQL as persistent storage
* write automated tests
* containerize the application
* provide a reproducible setup with Docker Compose
* document the application from a developer and user perspective

## Future Improvements

Possible next steps:

* React or Vue frontend
* role-based authorization
* attendance history per day
* integration tests with Testcontainers
* OpenAPI / Swagger documentation
* production-ready secret management
* deployment pipeline

```
```
