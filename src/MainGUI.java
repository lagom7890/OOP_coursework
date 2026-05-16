import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainGUI implements Observer {
    private final CustomerManagement customerManagement = new CustomerManagement();
    private final TaskManagement taskManagement = new TaskManagement();
    private final CommunicationTracking communicationTracking = new CommunicationTracking();
    private final Reporting reporting = new Reporting(customerManagement, taskManagement, communicationTracking);
    private final SessionManager sessionManager = SessionManager.getInstance();

    private JFrame frame;
    private JTextArea displayArea;

    public MainGUI() {
        customerManagement.addObserver(this);
        taskManagement.addObserver(this);
    }

    @Override
    public void update(String message) {
        if (sessionManager.isNotificationsEnabled()) {
            SwingUtilities.invokeLater(() -> 
                JOptionPane.showMessageDialog(frame, message, "Notification", JOptionPane.INFORMATION_MESSAGE)
            );
        }
    }

    public void start() {
        frame = new JFrame("CRM System GUI");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                JOptionPane.showMessageDialog(frame, 
                    "Great work today, " + sessionManager.getCurrentUser() + ". Have a wonderful rest of your day!",
                    "Goodbye!", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });
        frame.setSize(800, 600);

        String username = JOptionPane.showInputDialog(frame, "Please enter your name to login:");
        if (username != null && !username.trim().isEmpty()) {
            sessionManager.setCurrentUser(username);
        } else {
            sessionManager.setCurrentUser("Guest");
        }

        int hour = java.time.LocalTime.now().getHour();
        String greeting = (hour < 12) ? "Good morning" : (hour < 18) ? "Good afternoon" : "Good evening";

        JTabbedPane tabbedPane = new JTabbedPane();

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        
        // Home Tab
        JPanel homePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel(greeting + " " + sessionManager.getCurrentUser() + ", let's get things done!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        homePanel.add(welcomeLabel, BorderLayout.CENTER);
        tabbedPane.addTab("Home", homePanel);

        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 5, 5, 5));

        JButton btnListCust = new JButton("List Customers");
        btnListCust.addActionListener(e -> listCustomers());

        JButton btnAddCust = new JButton("Add Customer");
        btnAddCust.addActionListener(e -> addCustomer());

        JButton btnListTasks = new JButton("List Tasks");
        btnListTasks.addActionListener(e -> listTasks());

        JButton btnAddTask = new JButton("Add Task");
        btnAddTask.addActionListener(e -> addTask());

        JButton btnMarkTask = new JButton("Mark Task Done");
        btnMarkTask.addActionListener(e -> markTaskDone());

        JButton btnLogComm = new JButton("Log Comm");
        btnLogComm.addActionListener(e -> logCommunication());

        JButton btnReports = new JButton("Generate Reports");
        btnReports.addActionListener(e -> generateReports());

        JButton btnToggleNotif = new JButton("Toggle Notifications");
        btnToggleNotif.addActionListener(e -> toggleNotifications());

        controlPanel.add(btnListCust);
        controlPanel.add(btnAddCust);
        controlPanel.add(btnListTasks);
        controlPanel.add(btnAddTask);
        controlPanel.add(btnMarkTask);
        controlPanel.add(btnLogComm);
        controlPanel.add(btnReports);
        controlPanel.add(btnToggleNotif);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, scrollPane);
        splitPane.setDividerLocation(300);

        frame.getContentPane().add(splitPane, BorderLayout.CENTER);
        frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void listCustomers() {
        List<CustomerINFO> customers = customerManagement.getAllCustomers();
        StringBuilder sb = new StringBuilder("--- Customers ---\n");
        if (customers.isEmpty()) {
            sb.append("No customers found.\n");
        } else {
            for (CustomerINFO c : customers) {
                sb.append(String.format("ID: %d | Name: %s | Age: %d | Gender: %s | Phone: %d | Email: %s\n", 
                    c.getId(), c.getName(), c.getAge(), c.getGender(), c.getPhoneNumber(), c.getEmail()));
            }
        }
        displayArea.setText(sb.toString());
    }

    private void addCustomer() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID:"));
            String name = JOptionPane.showInputDialog("Enter Name:");
            int age = Integer.parseInt(JOptionPane.showInputDialog("Enter Age:"));
            String gender = JOptionPane.showInputDialog("Enter Gender:");
            long phone = Long.parseLong(JOptionPane.showInputDialog("Enter Phone:"));
            String email = JOptionPane.showInputDialog("Enter Email:");
            String address = JOptionPane.showInputDialog("Enter Address:");

            CustomerINFO customer = EntityFactory.createCustomer(id, name, age, gender, phone, email, address, "");
            customerManagement.addCustomer(customer);
            displayArea.setText("Customer added successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error adding customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listTasks() {
        List<TaskManagement.Task> tasks = taskManagement.getPendingTasks();
        StringBuilder sb = new StringBuilder("--- Pending Tasks ---\n");
        if (tasks.isEmpty()) {
            sb.append("No pending tasks.\n");
        } else {
            for (TaskManagement.Task t : tasks) {
                sb.append(t.toString()).append("\n");
            }
        }
        displayArea.setText(sb.toString());
    }

    private void addTask() {
        try {
            int cid = Integer.parseInt(JOptionPane.showInputDialog("Enter Customer ID:"));
            String desc = JOptionPane.showInputDialog("Enter Description:");
            String type = JOptionPane.showInputDialog("Enter Type:");
            
            taskManagement.addTask(cid, desc, type, java.time.LocalDateTime.now().plusDays(1));
            displayArea.setText("Task added successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error adding task", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markTaskDone() {
        try {
            int tid = Integer.parseInt(JOptionPane.showInputDialog("Enter Task ID:"));
            taskManagement.markTaskAsCompleted(tid);
            displayArea.setText("Task marked as completed.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error updating task", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logCommunication() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Customer ID:"));
            CustomerINFO customer = customerManagement.searchById(id);
            if (customer == null) {
                JOptionPane.showMessageDialog(frame, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] options = {"Email", "Phone", "Meeting"};
            int choice = JOptionPane.showOptionDialog(frame, "Choose communication type", "Log Communication", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            String contentText = JOptionPane.showInputDialog("Enter content:");
            if (contentText == null) return;

            Communications content;
            Logger logger;

            if (choice == 0) {
                content = new Communications("", contentText, "");
                logger = EntityFactory.createLogger("email", communicationTracking);
            } else if (choice == 1) {
                content = new Communications(contentText, "", "");
                logger = EntityFactory.createLogger("phone", communicationTracking);
            } else if (choice == 2) {
                content = new Communications("", "", contentText);
                logger = EntityFactory.createLogger("meeting", communicationTracking);
            } else {
                return;
            }

            String note = JOptionPane.showInputDialog("Add a note (optional):");
            if (note != null && !note.isEmpty()) {
                communicationTracking.addNote(content, note);
            }

            String tag = JOptionPane.showInputDialog("Add a tag (optional):");
            if (tag != null && !tag.isEmpty()) {
                communicationTracking.addTag(content, tag);
            }

            communicationTracking.logCommunicationWithLogger(logger, customer, content);
            displayArea.setText("Communication logged successfully.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error logging communication", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateReports() {
        String reportContent = reporting.getSummaryReportString();
        displayArea.setText(reportContent);
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
        
        displayArea.setText("Notifications " + (sessionManager.isNotificationsEnabled() ? "enabled" : "disabled"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().start());
    }
}
