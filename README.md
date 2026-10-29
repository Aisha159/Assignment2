# Order Billing Engine — Assignment 02

## Project Overview
This project refactors a legacy Java order billing system to improve maintainability, testability, and extensibility.

## Technologies
- Java 17
- Maven
- JUnit 5
- Git and GitHub

## Main Features
- Discount calculation for VIP, student, corporate, and regular customers.
- Tax calculation based on country.
- Payment strategies for credit card, EasyPaisa, and cash on delivery.
- Email and SMS notification service abstraction.
- Order repository interface for persistence.
- File-based order audit logging.

## Design Improvements
- **Single Responsibility Principle (SRP):** Billing, discount, tax, payment, notification, and persistence responsibilities are separated.
- **Open/Closed Principle (OCP):** Payment and notification implementations can be extended through interfaces.
- **Dependency Inversion Principle (DIP):** The billing engine accepts repository and notification dependencies through constructors.
- **Strategy Pattern:** Payment methods use separate strategy classes.

## Running Tests
Open the project in NetBeans and run the Maven tests, or execute:

```bash
mvn test
```

## Repository
Add your private GitHub repository URL here before submission.
