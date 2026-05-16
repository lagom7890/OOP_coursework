import java.time.LocalDateTime;
import java.util.List;

public class CLIUnitTests {

    public static void main(String[] args) {
        testCustomerFiltering();
        testTaskManagement();
        System.out.println("All tests passed (manual check)!");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            System.err.println("TEST FAIL: " + message);
            throw new RuntimeException("Assertion failed: " + message);
        }
    }

    private static void testCustomerFiltering() {
        System.out.println("Testing Customer Filtering...");
        CustomerManagement cm = new CustomerManagement();
        cm.addCustomer(EntityFactory.createCustomer(1, "Alice", 25, "Female", 123456, "alice@test.com", "Addr1", ""));
        cm.addCustomer(EntityFactory.createCustomer(2, "Bob", 35, "Male", 789012, "bob@test.com", "Addr2", ""));

        List<CustomerINFO> matches = cm.searchByName("Alice");
        assertTrue(matches.size() == 1, "Expected 1 Alice");
        
        List<CustomerINFO> males = cm.filterByGender("Male");
        assertTrue(males.size() == 1, "Expected 1 Male");
        
        List<CustomerINFO> ageRange = cm.filterByAgeRange(30, 40);
        assertTrue(ageRange.size() == 1 && ageRange.get(0).getName().equals("Bob"), "Expected Bob in age range");
        
        System.out.println("Customer Filtering tests passed.");
    }

    private static void testTaskManagement() {
        System.out.println("Testing Task Management...");
        TaskManagement tm = new TaskManagement();
        tm.addTask(1, "Call Alice", "Phone", LocalDateTime.now().plusDays(1));
        
        List<TaskManagement.Task> pending = tm.getPendingTasks();
        assertTrue(pending.size() == 1, "Expected 1 pending task");
        
        int taskId = pending.get(0).getTaskId();
        tm.markTaskAsCompleted(taskId);
        
        assertTrue(tm.getPendingTasks().isEmpty(), "Expected 0 pending tasks after completion");
        System.out.println("Task Management tests passed.");
    }
}
