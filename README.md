# SunBaseAssignment

This project is a CRUD (Create, Read, Update, Delete) application for managing customer data. It is built using Spring Boot for the backend and HTML/CSS/JavaScript for the frontend. The application also implements JWT (JSON Web Token) authentication for secure API access.

#Features

1.Customer Management: Add, update, view, and delete customer information.
2.JWT Authentication: Secure API endpoints with JWT tokens.
3.Pagination, Sorting, and Searching: Get a list of customers with pagination, sorting, and searching capabilities.
4.Data Sync: Sync customer data from a remote API, updating existing records or adding new ones as necessary.

#Project Structure

1.Backend: Spring Boot, Spring Data JPA, Spring Security, JWT.
2.Database: MySQL.
3.Frontend: HTML, CSS, JavaScript.

#Requirements

1.Java 11 or higher
2.MySQL
3.Maven

#Setup

1. Clone the repository

git clone https://github.com/your-repo/customer-crud-application.git
cd customer-crud-application

2. Set up MySQL Database
   
>Install MySQL if not already installed.
>Create a database named customer_db.

CREATE DATABASE customer_db;

>Create a customer table within the customer_db database.

USE customer_db;
CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    street VARCHAR(100),
    address VARCHAR(100),
    city VARCHAR(50),
    state VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20)
);

3. Configure the Application

Update the database configuration in src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/customer_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

5. Build and Run the Application

Navigate to the project directory and use Maven to build and run the application.
bash
Copy code
mvn clean install
mvn spring-boot:run

7. Access the Application
The application will be accessible at http://localhost:8080.

#Endpoints

Authentication

Login: POST /authenticate

#Customer API

1.Create a Customer: POST /api/customers

2.Update a Customer: PUT /api/customers/{id}

3.Get a List of Customers: GET /api/customers (supports pagination, sorting, and searching)

4.Get a Single Customer by ID: GET /api/customers/{id}

5.Delete a Customer: DELETE /api/customers/{id}

6.Sync Customers: GET /api/customers/sync

#Frontend

Login Screen: src/main/resources/static/login.html
Customer List Screen: src/main/resources/static/customers.html
Add a New Customer: src/main/resources/static/add-customer.html

#Usage

1.Login: Navigate to the login page (/login.html), enter the username and password to receive a JWT token.
2.Customer Management:
View customers on the customer list screen (/customers.html).
Add a new customer using the add customer form (/add-customer.html).
Edit or delete existing customers from the list.
3.Sync Data: Use the sync button on the customer list screen to fetch and update customer data from a remote API.

#Syncing Data

Authenticate with the remote API to get a bearer token.
Fetch Customer List from the remote API using the token.
Update existing customers or add new customers in the local database.
