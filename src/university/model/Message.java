package university.model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private String messageId;
    private Employee from;
    private Employee to;
    private String content;
    private Date timestamp;
    private boolean isRead;

    public Message(String messageId, Employee from, Employee to, String content) {
        this.messageId = messageId;
        this.from = from;
        this.to = to;
        this.content = content;
        this.timestamp = new Date();
        this.isRead = false;
    }

    public void markAsRead()          { this.isRead = true; }
    public boolean isRead()           { return isRead; }
    public Employee getFrom()         { return from; }
    public Employee getTo()           { return to; }
    public String getContent()        { return content; }
    public Date getTimestamp()        { return timestamp; }
    public String getMessageId() { return messageId; }
    @Override
    public String toString() {
        return "[" + timestamp + "] From: " + from.getLogin() + " → " + to.getLogin() + ": " + content;
    }
}
