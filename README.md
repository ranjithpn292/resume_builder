# resume_builder

Resume Builder Backend - End-to-End Testing Guide

1. Prerequisites
   To test the Resume Builder backend, ensure the following tools and dependencies are installed on your
   system:

- Java 17+
- PostgreSQL 13+
- Maven
- Postman or curl

2. Backend Setup
1. Clone the repository or download the source code.
2. Navigate to the root directory.
3. Create an application.properties file under src/main/resources with the following content:
   spring.datasource.url=jdbc:postgresql://localhost:5432/resume_builder_db
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   application.security.jwt.secret-key=your-very-secret-key
   openai.api.key=sk-your-openai-key
4. Run the application using:
   ./mvnw spring-boot:run
3. PostgreSQL Setup
1. Open your PostgreSQL client.
2. Create the database:
   CREATE DATABASE resume_builder_db;
3. The required tables will be created automatically when the application starts.
4. API Testing via Postman
   Use the following endpoints for testing the flow:

- POST /api/auth/register
  Request Body:
  {
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "password123"
  }
- POST /api/auth/login
  Request Body:
  {
  "email": "john@example.com",
  "password": "password123"
  }
  Response:
  {
  "token": "JWT_TOKEN_HERE"
  }
- POST /api/ai/improve
  Headers:
  Authorization: Bearer JWT_TOKEN_HERE
  Request Body:
  {
  "content": "Experienced Java developer with..."
  }

5. Troubleshooting

- Ensure PostgreSQL service is running.
- Double-check your database credentials.
- If OpenAI fails, verify your API key.
- Check the backend logs for stack traces.