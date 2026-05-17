package university.model;

import university.enums.LessonType;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Lesson implements Serializable {
    private static final long serialVersionUID = 1L;

    private String lessonId;
    private Course course;
    private LessonType type;
    private Date date;
    private String room;

    public Lesson(String lessonId, Course course, LessonType type, Date date, String room) {
        this.lessonId = lessonId;
        this.course = course;
        this.type = type;
        this.date = date;
        this.room = room;
    }

    public String getLessonInfo() {
        return "Lesson{id='" + lessonId + "', course='" + course.getName()
                + "', type=" + type + ", date=" + date + ", room='" + room + "'}";
    }

    public String getLessonId()      { return lessonId; }
    public Course getCourse()        { return course; }
    public LessonType getType()      { return type; }
    public Date getDate()            { return date; }
    public String getRoom()          { return room; }
    public void setRoom(String room) { this.room = room; }
    public void setDate(Date date)   { this.date = date; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson)) return false;
        return Objects.equals(lessonId, ((Lesson) o).lessonId);
    }

    @Override
    public int hashCode() { return Objects.hash(lessonId); }

    @Override
    public String toString() { return getLessonInfo(); }
}
