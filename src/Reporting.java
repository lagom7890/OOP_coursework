import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Reporting {
    private final CustomerManagement customerManagement;
    private final TaskManagement taskManagement;
    private final CommunicationTracking communicationTracking;

    public Reporting(CustomerManagement customerManagement, TaskManagement taskManagement, CommunicationTracking communicationTracking) {
        this.customerManagement = customerManagement;
        this.taskManagement = taskManagement;
        this.communicationTracking = communicationTracking;
    }

    public void generateCommunicationReport() {
        System.out.println("--- Communication Frequency Report ---");
        List<CommunicationTracking.CommunicationLog> history = communicationTracking.getCommunicationHistory();
        
        Map<Integer, Long> counts = history.stream()
                .collect(Collectors.groupingBy(CommunicationTracking.CommunicationLog::getCustomerId, Collectors.counting()));

        for (CustomerINFO customer : customerManagement.getAllCustomers()) {
            long count = counts.getOrDefault(customer.getId(), 0L);
            System.out.printf("Customer: %s (ID: %d) - Communications: %d%n", customer.getName(), customer.getId(), count);
            
            // Specifically list communication details to use getType and getTimestamp
            history.stream()
                .filter(log -> log.getCustomerId() == customer.getId())
                .forEach(log -> System.out.printf("   > [%s] %s%n", log.getTimestamp().toString(), log.getType()));
        }
        System.out.println("--------------------------------------");
    }

    public void generateTaskCompletionReport() {
        System.out.println("--- Task Completion Rate Report ---");
        for (CustomerINFO customer : customerManagement.getAllCustomers()) {
            List<TaskManagement.Task> tasks = taskManagement.getTasksByCustomer(customer.getId());
            if (tasks.isEmpty()) {
                System.out.printf("Customer: %s (ID: %d) - No tasks assigned.%n", customer.getName(), customer.getId());
                continue;
            }
            long completed = tasks.stream().filter(TaskManagement.Task::isCompleted).count();
            double rate = (double) completed / tasks.size() * 100;
            System.out.printf("Customer: %s (ID: %d) - Tasks: %d Total, %d Completed, Rate: %.2f%%%n", 
                    customer.getName(), customer.getId(), tasks.size(), completed, rate);
        }
        System.out.println("-----------------------------------");
    }

    public void displaySummaryReport() {
        System.out.println("======= CUSTOMER ACTIVITY SUMMARY =======");
        generateCommunicationReport();
        generateTaskCompletionReport();
        System.out.println("=========================================");
    }
}
