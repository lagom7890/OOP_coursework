import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class GUIUnitTests {

    public static void main(String[] args) {
        System.out.println("Running GUI Unit Tests...");
        try {
            testMainGUIInitialization();
            testObserverUpdate();
            System.out.println("All GUI tests passed successfully!");
        } catch (Exception e) {
            System.err.println("GUI Tests failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testMainGUIInitialization() throws Exception {
        System.out.println("Testing MainGUI Initialization...");
        
        MainGUI gui = new MainGUI();
        
        // Use reflection to verify internal components are initialized correctly
        Field customerManagementField = MainGUI.class.getDeclaredField("customerManagement");
        customerManagementField.setAccessible(true);
        Object cm = customerManagementField.get(gui);
        if (cm == null) {
            throw new AssertionError("CustomerManagement was not initialized in MainGUI.");
        }
        
        Field taskManagementField = MainGUI.class.getDeclaredField("taskManagement");
        taskManagementField.setAccessible(true);
        Object tm = taskManagementField.get(gui);
        if (tm == null) {
            throw new AssertionError("TaskManagement was not initialized in MainGUI.");
        }

        System.out.println("MainGUI Initialization passed.");
    }

    private static void testObserverUpdate() {
        System.out.println("Testing GUI Observer behavior...");
        
        SessionManager.getInstance().setNotificationsEnabled(false); // disable popup for tests
        MainGUI gui = new MainGUI();
        
        try {
            gui.update("Test Message");
            System.out.println("Observer update method handles gracefully when popups are disabled.");
        } catch (Exception e) {
            throw new AssertionError("Observer update execution failed: " + e.getMessage(), e);
        }
        
        System.out.println("GUI Observer behavior passed.");
    }
}

