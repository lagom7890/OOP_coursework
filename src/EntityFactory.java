import java.time.LocalDateTime;

public class EntityFactory {

    public static CustomerINFO createCustomer(int id, String name, int age, String gender, long phoneNumber, String email, String address, String notes) {
        return new CustomerINFO(gender, name, age, id, phoneNumber, email, address, notes);
    }

    public static TaskManagement.Task createTask(int taskId, int customerId, String description, String type, LocalDateTime dueDateTime) {
        return new TaskManagement.Task(taskId, customerId, description, type, dueDateTime);
    }

    public static Logger createLogger(String type, CommunicationTracking tracking) {
        return switch (type.toLowerCase()) {
            case "email" -> new EmailLogger(tracking);
            case "phone" -> new PhoneLogger(tracking);
            case "meeting" -> new MeetingLogger(tracking);
            default -> throw new IllegalArgumentException("Unknown logger type: " + type);
        };
    }
}

