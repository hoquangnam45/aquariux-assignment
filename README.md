# Aquariux Assignment

## Description
This project is a Spring Boot application developed as an assignment for Aquariux. It demonstrates the implementation of a secure web application with data persistence using JPA and H2 database.

## Technologies Used
- Java 17
- Spring Boot 3.4.3
- Spring Data JPA
- Spring Security
- H2 Database
- Flyway for database migrations
- Lombok for reducing boilerplate code
- Maven for dependency management and build automation

## Project Structure

```
aquariux-assignment/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/
│   └── test/
│       └── java/
│           └── com/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── aquariux-assignment.postman_collection.json
├── aquariux.db.lock.db
├── aquariux.db.mv.db
└── README.md
```

## Getting Started

### Prerequisites
- JDK 17
- Maven

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Run the following command:

```
./mvnw spring-boot:run
```

4. The application will start and be available at `http://localhost:8080`

## API Documentation
The API endpoints can be tested using the provided Postman collection: `aquariux-assignment.postman_collection.json`

## Database
This project uses an H2 in-memory database. The database files are:
- `aquariux.db.lock.db`
- `aquariux.db.mv.db`

## Testing
To run the tests, use the following command:

```
./mvnw test
```

## Building
To build the project, use:

```
./mvnw clean package
```

This will create a JAR file in the `target` directory.
