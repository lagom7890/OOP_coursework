public class EmailLogger implements Logger{
    private final CommunicationTracking tracking;

    public EmailLogger(CommunicationTracking tracking) {
        this.tracking = tracking;
    }

    @Override
    public void process(CustomerINFO contactINFO, Communications content) {
        System.out.println("Logging email details...");
        System.out.println("Email address: " + contactINFO.getEmail());
        System.out.println("Content: " + content.getEmailContent());
        if (content.getNotes() != null) {
            System.out.println("Additional Notes: " + content.getNotes());
        }
        if (content.getTags() != null) {
            System.out.println("Tags: " + content.getTags());
        }
        System.out.println("Email logged successfully.");
        if (tracking != null) {
            tracking.logCommunication(contactINFO.getId(), "Email");
        }
    }
}
