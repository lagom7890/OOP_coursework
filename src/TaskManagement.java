import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManagement extends Subject {
    private static int nextTaskId = 1;
    private final List<Task> tasks = new ArrayList<>();

    public static class Task {
        private final int taskId;
        private final int customerId;
        private final String description;
        private final String type; // e.g., "Follow-up", "Meeting"
        private final LocalDateTime dueDateTime;
        private boolean isCompleted;

        public Task(int taskId, int customerId, String description, String type, LocalDateTime dueDateTime) {
            this.taskId = taskId;
            this.customerId = customerId;
            this.description = description;
            this.type = type;
            this.dueDateTime = dueDateTime;
            this.isCompleted = false;
        }

        public int getTaskId() { return taskId; }
        public int getCustomerId() { return customerId; }
        public String getDescription() { return description; }
        public String getType() { return type; }
        public LocalDateTime getDueDateTime() { return dueDateTime; }
        public boolean isCompleted() { return isCompleted; }
        public void setCompleted(boolean completed) { isCompleted = completed; }

        @Override
        public String toString() {
            return String.format("[%s] Task ID: %d, Customer ID: %d, Type: %s, Description: %s, Due: %s, Status: %s",
                    isCompleted ? "DONE" : "PENDING", taskId, customerId, type, description, dueDateTime, isCompleted ? "Completed" : "Pending");
        }
    }

    public void addTask(int customerId, String description, String type, LocalDateTime dueDateTime) {
        Task newTask = EntityFactory.createTask(nextTaskId++, customerId, description, type, dueDateTime);
        tasks.add(newTask);
        System.out.println("Task added successfully: " + newTask);
        notifyObservers("New task assigned: " + description + " for customer " + customerId);
    }

    public List<Task> getTasksByCustomer(int customerId) {
        return tasks.stream()
                .filter(t -> t.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    public List<Task> getPendingTasks() {
        return tasks.stream()
                .filter(t -> !t.isCompleted())
                .collect(Collectors.toList());
    }

    public void markTaskAsCompleted(int taskId) {
        for (Task task : tasks) {
            if (task.getTaskId() == taskId) {
                task.setCompleted(true);
                System.out.println("🎉 Awesome job! Task marked as completed: " + task.getDescription());
                notifyObservers("Task completed (Keep it up!): " + task.getDescription());
                return;
            }
        }
        System.out.println("Task not found with ID: " + taskId);
    }

    public void checkReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderThreshold = now.plusHours(24);
        
        List<Task> upcomingTasks = tasks.stream()
                .filter(t -> !t.isCompleted() && t.getDueDateTime().isAfter(now) && t.getDueDateTime().isBefore(reminderThreshold))
                .collect(Collectors.toList());

        if (upcomingTasks.isEmpty()) {
            System.out.println("No upcoming reminders for the next 24 hours.");
        } else {
            System.out.println("--- TASK REMINDERS (Next 24 Hours) ---");
            for (Task task : upcomingTasks) {
                System.out.println("REMINDER: " + task);
            }
        }
    }
}
