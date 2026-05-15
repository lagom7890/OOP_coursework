public class PhoneLogger implements Logger {
    private final CommunicationTracking tracking;

    public PhoneLogger(CommunicationTracking tracking) {
        this.tracking = tracking;
    }

    @Override
    public void process(CustomerINFO contactINFO, Communications content) {
        System.out.println("Logging phone call details...");
        System.out.println("Phone number: " + contactINFO.getPhoneNumber());
        System.out.println("Content: " + content.getPhoneContent());
        if (content.getNotes() != null) {
            System.out.println("Additional Notes: " + content.getNotes());
        }
        if (content.getTags() != null) {
            System.out.println("Tags: " + content.getTags());
        }
        System.out.println("Phone call logged successfully.");
        if (tracking != null) {
            tracking.logCommunication(contactINFO.getId(), "Phone");
        }
    }
}
