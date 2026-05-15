public class Communications {
    private final String phoneContent;
    private final String emailContent;
    private final String meetingContent;
    private String notes;
    private String tags;

    public Communications(String phoneContent, String emailContent, String meetingContent) {
        this.phoneContent = phoneContent;
        this.emailContent = emailContent;
        this.meetingContent = meetingContent;
    }

    public String getPhoneContent() {
        return phoneContent;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public String getMeetingContent() {
        return meetingContent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
