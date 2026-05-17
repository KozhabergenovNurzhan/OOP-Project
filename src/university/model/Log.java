package university.model;

import java.io.Serializable;
import java.util.Date;

public class Log implements Serializable {
    private static final long serialVersionUID = 1L;

    private String action;
    private User performedBy;
    private Date timestamp;

    public Log(String action, User performedBy) {
        this.action = action;
        this.performedBy = performedBy;
        this.timestamp = new Date();
    }

    public String getAction()        { return action; }
    public User getPerformedBy()     { return performedBy; }
    public Date getTimestamp()       { return timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + performedBy.getLogin() + " → " + action;
    }
}
