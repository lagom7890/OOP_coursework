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

    public String generateCommunicationReportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Communication Frequency Report ---\n");
        List<CommunicationTracking.CommunicationLog> history = communicationTracking.getCommunicationHistory();
        
        Map<Integer, Long> counts = history.stream()
                .collect(Collectors.groupingBy(CommunicationTracking.CommunicationLog::getCustomerId, Collectors.counting()));

        for (CustomerINFO customer : customerManagement.getAllCustomers()) {
            long count = counts.getOrDefault(customer.getId(), 0L);
            sb.append(String.format("Customer: %s (ID: %d) - Communications: %d\n", customer.getName(), customer.getId(), count));
            
            history.stream()
                .filter(log -> log.getCustomerId() == customer.getId())
                .forEach(log -> sb.append(String.format("   > [%s] %s\n", log.getTimestamp().toString(), log.getType())));
        }
        sb.append("--------------------------------------\n");
        return sb.toString();
    }

    public String generateTaskCompletionReportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Task Completion Rate Report ---\n");
        for (CustomerINFO customer : customerManagement.getAllCustomers()) {
            List<TaskManagement.Task> tasks = taskManagement.getTasksByCustomer(customer.getId());
            if (tasks.isEmpty()) {
                sb.append(String.format("Customer: %s (ID: %d) - No tasks assigned.\n", customer.getName(), customer.getId()));
                continue;
            }
            long completed = tasks.stream().filter(TaskManagement.Task::isCompleted).count();
            double rate = (double) completed / tasks.size() * 100;
            sb.append(String.format("Customer: %s (ID: %d) - Tasks: %d Total, %d Completed, Rate: %.2f%%\n", 
                    customer.getName(), customer.getId(), tasks.size(), completed, rate));
        }
        sb.append("-----------------------------------\n");
        return sb.toString();
    }

    public String getSummaryReportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("======= CUSTOMER ACTIVITY SUMMARY =======\n");
        sb.append(generateCommunicationReportString());
        sb.append(generateTaskCompletionReportString());
        sb.append("=========================================\n");
        return sb.toString();
    }

    public void displaySummaryReport() {
        System.out.print(getSummaryReportString());
    }
}
