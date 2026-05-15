# Customer Relations Manager (CRM) Program

## Project Overview
This project is a Customer Relations Manager (CRM) program built in Java. It allows businesses to manage customer information, track communications, and schedule follow-ups. The application is designed focusing on usability, scalability, and adherence to software development best practices, implementing core Object-Oriented Programming (OOP) design patterns.

## Core Features
- **Customer Management**: Add, update, and manage customer profiles including contact information and notes. Support for searching by name and filtering by gender or age range.
- **Communication Tracking**: Log interactions with customers, including Emails, Phone calls, and Meetings. Notes and tags can be added to logs. Users can specify a customer and communication method to load specific interaction contents.
- **Task Management**: Create tasks associated with customers (e.g., follow-ups). Mark tasks as completed, track pending tasks, and provide reminders/notifications for pending tasks.
- **Reporting**: Report on customer activity such as communication frequency and task completion rates.
- **User Interface**: A Command-Line Interface (CLI) is provided to interact with the system easily.

## Software Patterns Implemented
- **Singleton Pattern**: Managed via `SessionManager` to handle application state and active user sessions globally.
- **Observer Pattern**: Used by `Subject` and `Observer` interfaces to handle notifications (e.g., task reminders or updates to customer data and tasks) to users like the MainCLI.
- **Factory Pattern**: The `EntityFactory` creates instances of customer profiles, tasks, and communication logs dynamically.

## Project Structure
- `MainCLI.java` - The main entry point featuring the CLI loop.
- `MainGUI.java` - The alternative Graphical User Interface entry point.
- `CustomerManagement.java` & `CustomerINFO.java` - Handles customer data logic and model.
- `CommunicationTracking.java`, `Communications.java` (along with `EmailLogger`, `PhoneLogger`, `MeetingLogger`) - Manages communication logs and records for specific contacts.
- `TaskManagement.java` - Manages tasks and follow-ups.
- `Reporting.java` - Generates activity reports.
- `SessionManager.java` - Manages application state.
- `EntityFactory.java` - Creates core entities.
- `UnitTests.java` - Unit tests for checking critical functionality like filtering and task statuses.
- `GUIUnitTests.java` - Unit tests designed to verify GUI component initialization and observer patterns.

## Setup Instructions
1. Ensure you have Java (JDK 8 or higher) installed on your system.
2. Clone or download the project files and place all `.java` files in the `src` folder.
3. Open the project in your preferred IDE or command line.

## Usage Guide
1. **Compilation**: 
   Compile the Java files in the `src` directory:
   ```bash
   javac *.java
   ```
2. **Running the Application**: 
   Start the CLI by running the `MainCLI` class:
   ```bash
   java MainCLI
   ```
   Or start the Graphical Interface by running:
   ```bash
   java MainGUI
   ```
3. **Running Tests**:
   To run the core logic unit tests:
   ```bash
   java -ea UnitTests
   ```
   To run the GUI unit tests:
   ```bash
   java -ea GUIUnitTests
   ```
4. **Navigating the CLI**: Follow the on-screen prompts to manage customers, log communications tied to specific contact methods, schedule tasks, and view reports.
