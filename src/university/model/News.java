package university.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class News implements Serializable {
    private static final long serialVersionUID = 1L;

    private String newsId;
    private String title;
    private String content;
    private Manager publishedBy;
    private Date date;

    public News(String newsId, String title, String content, Manager publishedBy) {
        this.newsId = newsId;
        this.title = title;
        this.content = content;
        this.publishedBy = publishedBy;
        this.date = new Date();
    }

    public String getNewsId()           { return newsId; }
    public String getTitle()            { return title; }
    public String getContent()          { return content; }
    public Manager getPublishedBy()     { return publishedBy; }
    public Date getDate()               { return date; }
    public void setContent(String c)    { this.content = c; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof News)) return false;
        return Objects.equals(newsId, ((News) o).newsId);
    }

    @Override
    public int hashCode() { return Objects.hash(newsId); }

    @Override
    public String toString() {
        return "[" + date + "] " + title + " (by " + publishedBy.getLogin() + ")";
    }
}
