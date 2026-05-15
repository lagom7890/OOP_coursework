public class MeetingLogger implements Logger{
    private final CommunicationTracking tracking;

    public MeetingLogger(CommunicationTracking tracking) {
        this.tracking = tracking;
    }

    @Override
    public void process(CustomerINFO contactINFO, Communications content) {
        System.out.println("Logging meeting details...");
        System.out.println("Meeting with: " + contactINFO.getName());
        System.out.println("Content: " + content.getMeetingContent());
        if (content.getNotes() != null) {
            System.out.println("Additional Notes: " + content.getNotes());
        }
        if (content.getTags() != null) {
            System.out.println("Tags: " + content.getTags());
        }
        System.out.println("Meeting logged successfully.");
        if (tracking != null) {
            tracking.logCommunication(contactINFO.getId(), "Meeting");
        }
    }
}
