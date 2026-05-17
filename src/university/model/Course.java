package university.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseId;
    private String name;
    private int credits;
    private List<Teacher> instructors; // more than 1 allowed
    private List<Lesson> lessons;
    private List<Student> enrolledStudents;
    private String major;
    private int yearIntended;

    public Course(String courseId, String name, int credits, String major, int yearIntended) {
        this.courseId = courseId;
        this.name = name;
        this.credits = credits;
        this.major = major;
        this.yearIntended = yearIntended;
        this.instructors = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
    }

    public void addInstructor(Teacher t) {
        if (!instructors.contains(t)) instructors.add(t);
    }

    public void removeInstructor(Teacher t) {
        instructors.remove(t);
    }

    public void addLesson(Lesson l) {
        lessons.add(l);
    }

    public void enrollStudent(Student s) {
        if (!enrolledStudents.contains(s)) enrolledStudents.add(s);
    }

    public void removeStudent(Student s) {
        enrolledStudents.remove(s);
    }

    // Getters
    public String getCourseId()               { return courseId; }
    public String getName()                   { return name; }
    public int getCredits()                   { return credits; }
    public List<Teacher> getInstructors()     { return instructors; }
    public List<Lesson> getLessons()          { return lessons; }
    public List<Student> getEnrolledStudents(){ return enrolledStudents; }
    public String getMajor()                  { return major; }
    public int getYearIntended()              { return yearIntended; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        return Objects.equals(courseId, ((Course) o).courseId);
    }

    @Override
    public int hashCode() { return Objects.hash(courseId); }

    @Override
    public String toString() {
        return "Course{id='" + courseId + "', name='" + name + "', credits=" + credits + "}";
    }
}
