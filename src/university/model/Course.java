package university.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an academic course in the University Information System.
 * <p>
 * A course can have multiple instructors, enrolled students, and scheduled lessons.
 * It is associated with a specific major and intended year of study.
 * </p>
 *
 * @author Kozhabergenov Nurzhan, Yelmuratova Madina, Kainazar Nurdaulet
 * @version 1.0
 * @see Teacher
 * @see Student
 * @see Lesson
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Unique identifier for this course. */
    private String courseId;

    /** Full name of the course. */
    private String name;

    /** Number of academic credits this course is worth. */
    private int credits;

    /** List of teachers assigned to instruct this course. */
    private List<Teacher> instructors;

    /** List of scheduled lessons for this course. */
    private List<Lesson> lessons;

    /** List of students currently enrolled in this course. */
    private List<Student> enrolledStudents;

    /** The major this course belongs to (e.g. "CS", "IS"). */
    private String major;

    /** The academic year this course is intended for (1–4). */
    private int yearIntended;

    /**
     * Constructs a new Course with the given details.
     *
     * @param courseId     unique course identifier
     * @param name         full name of the course
     * @param credits      number of academic credits
     * @param major        major field this course belongs to
     * @param yearIntended intended academic year (1–4)
     */
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

    /**
     * Adds a teacher as an instructor for this course.
     * Duplicate instructors are not added.
     *
     * @param t the teacher to add as instructor
     */
    public void addInstructor(Teacher t) {
        if (!instructors.contains(t)) instructors.add(t);
    }

    /**
     * Removes a teacher from the list of instructors for this course.
     *
     * @param t the teacher to remove
     */
    public void removeInstructor(Teacher t) {
        instructors.remove(t);
    }

    /**
     * Adds a scheduled lesson to this course.
     *
     * @param l the lesson to add
     */
    public void addLesson(Lesson l) {
        lessons.add(l);
    }

    /**
     * Enrolls a student in this course.
     * Duplicate enrollments are not allowed.
     *
     * @param s the student to enroll
     */
    public void enrollStudent(Student s) {
        if (!enrolledStudents.contains(s)) enrolledStudents.add(s);
    }

    /**
     * Removes a student from this course's enrollment list.
     *
     * @param s the student to remove
     */
    public void removeStudent(Student s) {
        enrolledStudents.remove(s);
    }

    /** @return the unique course ID */
    public String getCourseId()               { return courseId; }
    /** @return the course name */
    public String getName()                   { return name; }
    /** @return number of credits */
    public int getCredits()                   { return credits; }
    /** @return list of instructors */
    public List<Teacher> getInstructors()     { return instructors; }
    /** @return list of scheduled lessons */
    public List<Lesson> getLessons()          { return lessons; }
    /** @return list of enrolled students */
    public List<Student> getEnrolledStudents(){ return enrolledStudents; }
    /** @return the major this course belongs to */
    public String getMajor()                  { return major; }
    /** @return the intended academic year */
    public int getYearIntended()              { return yearIntended; }

    /**
     * Checks equality based on course ID.
     *
     * @param o the object to compare
     * @return {@code true} if both courses have the same ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        return Objects.equals(courseId, ((Course) o).courseId);
    }

    /**
     * Returns a hash code based on the course ID.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() { return Objects.hash(courseId); }

    /**
     * Returns a string representation of this course.
     *
     * @return string with course ID, name, and credits
     */
    @Override
    public String toString() {
        return "Course{id='" + courseId + "', name='" + name + "', credits=" + credits + "}";
    }
}
