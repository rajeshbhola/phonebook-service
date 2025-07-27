# Phonebook Application

This is a simple Phonebook application built with Spring Boot for the backend REST API and a static HTML/JavaScript frontend.

## Features

- Create Contact
- Display All Contacts
- Edit and Update Contact Details
- Delete Contact
- Multiple phone numbers per contact
- Masked phone numbers for display
- Responsive UI with animations

## Technologies Used

**Backend:**
- Spring Boot
- Spring Data JPA
- H2 Database (in-memory)
- RESTful API principles
- SLF4J for logging

**Frontend:**
- HTML5
- Tailwind CSS (via CDN)
- JavaScript (Fetch API for API interaction)

## Project Structure

The project follows a standard Spring Boot application structure with packages for different layers:

- `com.bholacodecamp.phonebook.controller`: Contains REST controllers for handling API requests.
- `com.bholacodecamp.phonebook.service`: Contains service layer for business logic.
- `com.bholacodecamp.phonebook.repository`: Contains Spring Data JPA repositories for data access.
- `com.bholacodecamp.phonebook.entity`: Contains JPA entities (`Contact`, `PhoneNumber`).
- `com.bholacodecamp.phonebook.dto`: Contains Data Transfer Objects (DTOs) for API communication.
- `com.bholacodecamp.phonebook.exception`: Contains global exception handler.

**Frontend Files:**
- `src/main/resources/static/index.html`: The main HTML file for the user interface.
- `src/main/resources/static/js/app.js`: JavaScript for API calls, UI rendering, and dynamic form handling.
- `src/main/resources/static/js/confirm.js`: JavaScript for modal and toast notifications.

## How to Run

1.  **Prerequisites:**
    - Java 17 or higher
    - Maven

2.  **Build the project:**
    ```bash
    ./mvnw clean install
    ```

3.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```

4.  **Access the application:**
    Open your web browser and navigate to `http://localhost:8081` (or the port configured in `application.properties`).

## API Endpoints

All API endpoints are prefixed with `/api/contacts`.

-   **GET /api/contacts**: Get all contacts.
-   **GET /api/contacts/{id}**: Get a contact by ID.
-   **POST /api/contacts**: Create a new contact.
-   **PUT /api/contacts/{id}**: Update an existing contact.
-   **DELETE /api/contacts/{id}**: Delete a contact by ID.

## Logging

Logging is configured using SLF4J. You can view the application logs in your console when running the application.
