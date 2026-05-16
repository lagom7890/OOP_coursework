import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.List;

public class MainCLI implements Observer {
    private final CustomerManagement customerManagement = new CustomerManagement();
    private final TaskManagement taskManagement = new TaskManagement();
    private final CommunicationTracking communicationTracking;
    private final Reporting reporting;
    private final Scanner scanner = new Scanner(System.in);
    private final SessionManager sessionManager = SessionManager.getInstance();

    public MainCLI() {
        this.communicationTracking = new CommunicationTracking();
        this.reporting = new Reporting(customerManagement, taskManagement, communicationTracking);

        // Register as observer
        customerManagement.addObserver(this);
        taskManagement.addObserver(this);
    }

    @Override
    public void update(String message) {
        if (sessionManager.isNotificationsEnabled()) {
            System.out.println("\n[NOTIFICATION]: " + message);
        }
    }

    public void start() {
        System.out.println("=== Welcome to CRM System ===");
        System.out.print("Please enter your name to login: ");
        String username = scanner.nextLine();
        sessionManager.setCurrentUser(username);
        
        int hour = java.time.LocalTime.now().getHour();
        String greeting = (hour < 12) ? "Good morning" : (hour < 18) ? "Good afternoon" : "Good evening";
        System.out.println("\n" + greeting + ", " + sessionManager.getCurrentUser() + "! Let's get things done today.");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": addCustomer(); break;
                case "2": listCustomers(); break;
                case "3": searchCustomers(); break;
                case "4": addTask(); break;
                case "5": listTasks(); break;
                case "6": markTaskDone(); break;
                case "7": taskManagement.checkReminders(); break;
                case "8": logCommunication(); break;
                case "9": generateReports(); break;
                case "10": toggleNotifications(); break;
                case "0": 
                    running = false; 
                    System.out.println("Great work today, " + sessionManager.getCurrentUser() + ". Have a wonderful rest of your day!");
                    break;
                default: System.out.println("Invalid choice.");
            }
        }
        System.out.println("Exiting... Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n--- CRM MENU ---");
        System.out.println("1. Add Customer");
        System.out.println("2. List All Customers");
        System.out.println("3. Search Customers");
        System.out.println("4. Add Task");
        System.out.println("5. List Pending Tasks");
        System.out.println("6. Mark Task as Completed");
        System.out.println("7. Task Reminders");
        System.out.println("8. Log Communication");
        System.out.println("9. Generate Reports");
        System.out.println("10. Toggle Notifications (Current: " + (sessionManager.isNotificationsEnabled() ? "ON" : "OFF") + ")");
        System.out.println("11. Exit");
        System.out.print("Enter choice: ");
    }

    private void addCustomer() {
        System.out.print("Enter ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter Phone: ");
        long phone = Long.parseLong(scanner.nextLine());
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        CustomerINFO customer = EntityFactory.createCustomer(id, name, age, gender, phone, email, address, "");
        customerManagement.addCustomer(customer);
    }

    private void listCustomers() {
        List<CustomerINFO> customers = customerManagement.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (CustomerINFO c : customers) {
                System.out.printf("ID: %d | Name: %s | Age: %d | Gender: %s | Phone: %d | Email: %s | Address: %s | Notes: %s%n", 
                    c.getId(), c.getName(), c.getAge(), c.getGender(), c.getPhoneNumber(), c.getEmail(), c.getAddress(), c.getNotes());
            }
        }
    }

    private void searchCustomers() {
        System.out.println("Search by: 1. Name 2. Contact (Email/Phone) 3. Gender 4. Age Range");
        String type = scanner.nextLine();
        List<CustomerINFO> results = null;

        switch (type) {
            case "1":
                System.out.print("Enter name: ");
                results = customerManagement.searchByName(scanner.nextLine());
                break;
            case "2":
                System.out.print("Enter email or phone: ");
                results = customerManagement.searchByContact(scanner.nextLine());
                break;
            case "3":
                System.out.print("Enter gender: ");
                results = customerManagement.filterByGender(scanner.nextLine());
                break;
            case "4":
                System.out.print("Enter min age: ");
                int min = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter max age: ");
                int max = Integer.parseInt(scanner.nextLine());
                results = customerManagement.filterByAgeRange(min, max);
                break;
        }

        if (results == null || results.isEmpty()) {
            System.out.println("No results found.");
        } else {
            results.forEach(c -> System.out.printf("ID: %d | Name: %s | Age: %d | Gender: %s | Phone: %d | Email: %s | Address: %s | Notes: %s%n", 
                c.getId(), c.getName(), c.getAge(), c.getGender(), c.getPhoneNumber(), c.getEmail(), c.getAddress(), c.getNotes()));
        }
    }

    private void addTask() {
        System.out.print("Enter Customer ID: ");
        int cid = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Description: ");
        String desc = scanner.nextLine();
        System.out.print("Enter Type (e.g., Follow-up): ");
        String type = scanner.nextLine();
        
        taskManagement.addTask(cid, desc, type, LocalDateTime.now().plusDays(1));
    }

    private void listTasks() {
        List<TaskManagement.Task> tasks = taskManagement.getPendingTasks();
        if (tasks.isEmpty()) {
            System.out.println("No pending tasks.");
        } else {
            for (TaskManagement.Task t : tasks) {
                System.out.println(t);
            }
        }
    }

    private void markTaskDone() {
        System.out.print("Enter Task ID: ");
        int tid = Integer.parseInt(scanner.nextLine());
        taskManagement.markTaskAsCompleted(tid);
    }

    private void generateReports() {
        reporting.displaySummaryReport();
    }

    private void toggleNotifications() {
        boolean currentlyEnabled = sessionManager.isNotificationsEnabled();
        sessionManager.setNotificationsEnabled(!currentlyEnabled);
        
        if (!currentlyEnabled) {
            customerManagement.addObserver(this);
            taskManagement.addObserver(this);
        } else {
            customerManagement.removeObserver(this);
            taskManagement.removeObserver(this);
        }
        
        System.out.println("Notifications " + (sessionManager.isNotificationsEnabled() ? "enabled" : "disabled"));
    }

    private void logCommunication() {
        System.out.print("Enter Customer ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        CustomerINFO customer = customerManagement.searchById(id);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("Choose communication type:");
        System.out.println("1. Email");
        System.out.println("2. Phone");
        System.out.println("3. Meeting");
        String type = scanner.nextLine();

        System.out.print("Enter content: ");
        String contentText = scanner.nextLine();

        Communications content;
        Logger logger;

        switch (type) {
            case "1":
                content = new Communications("", contentText, "");
                logger = EntityFactory.createLogger("email", communicationTracking);
                break;
            case "2":
                content = new Communications(contentText, "", "");
                logger = EntityFactory.createLogger("phone", communicationTracking);
                break;
            case "3":
                content = new Communications("", "", contentText);
                logger = EntityFactory.createLogger("meeting", communicationTracking);
                break;
            default:
                System.out.println("Invalid type.");
                return;
        }

        System.out.print("Add a note (optional, press Enter to skip): ");
        String note = scanner.nextLine();
        if (!note.isEmpty()) {
            communicationTracking.addNote(content, note);
        }

        System.out.print("Add a tag (optional, press Enter to skip): ");
        String tag = scanner.nextLine();
        if (!tag.isEmpty()) {
            communicationTracking.addTag(content, tag);
        }

        communicationTracking.logCommunicationWithLogger(logger, customer, content);
    }

    public static void main(String[] args) {
        new MainCLI().start();
    }
}
