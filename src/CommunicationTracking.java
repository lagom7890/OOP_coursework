import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommunicationTracking {
    private final List<CommunicationLog> communicationHistory = new ArrayList<>();

    public CommunicationTracking() {
    }

    public void logCommunicationWithLogger(Logger logger, CustomerINFO contactINFO, Communications content){
        if (logger == null) {
            System.out.println("Logger is null.");
            return;
        }
        logger.process(contactINFO, content);
    }

    public void addNote(Communications content, String note) {
        content.setNotes(note);
    }

    public void addTag(Communications content, String tag) {
        String currentTags = content.getTags();
        if (currentTags == null || currentTags.isEmpty()) {
            content.setTags(tag);
        } else {
            content.setTags(currentTags + ", " + tag);
        }
    }

    public static class CommunicationLog {
        private final int customerId;
        private final String type;
        private final LocalDateTime timestamp;

        public CommunicationLog(int customerId, String type, LocalDateTime timestamp) {
            this.customerId = customerId;
            this.type = type;
            this.timestamp = timestamp;
        }

        public int getCustomerId() { return customerId; }
        public String getType() { return type; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    public void logCommunication(int customerId, String type) {
        communicationHistory.add(new CommunicationLog(customerId, type, LocalDateTime.now()));
    }

    public List<CommunicationLog> getCommunicationHistory() {
        return new ArrayList<>(communicationHistory);
    }
}
