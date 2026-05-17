package university.model;

import university.exceptions.CreditLimitException;
import university.exceptions.FailLimitException;
import university.exceptions.LowHIndexException;
import university.model.research.Researcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bachelor student in the University Information System.
 * <p>
 * A student can register for courses, view marks and transcript,
 * rate teachers, and optionally have a research supervisor (4th year only).
 * Credit and fail limits are enforced automatically.
 * </p>
 *
 * @author Kozhabergenov Nurzhan, Yelmuratova Madina, Kainazar Nurdaulet
 * @version 1.0
 * @see User
 * @see Course
 * @see Mark
 * @see Transcript
 */
public class Student extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Maximum number of credits a student can register per semester. */
    private static final int MAX_CREDITS = 30;

    /** Maximum number of course failures allowed before exception is thrown. */
    private static final int MAX_FAILS = 3;

    /** Unique student identifier. */
    private String studentId;

    /** Current GPA calculated from transcript records. */
    private double gpa;

    /** Total credits registered in the current semester. */
    private int credits;

    /** Number of failed courses. */
    private int failCount;

    /** Academic year of study (1 to 4). */
    private int year;

    /** Major field of study (e.g. "CS", "IS"). */
    private String major;

    /** List of courses the student is enrolled in. */
    private List<Course> courses;

    /** Academic transcript containing all grade records. */
    private Transcript transcript;

    /** Research supervisor assigned to 4th year students. */
    private Researcher supervisor;

    /** History of ratings given to teachers. */
    private List<Integer> teacherRatings;

    /**
     * Constructs a new Student with the given details.
     *
     * @param userId    unique user identifier
     * @param login     login name for authentication
     * @param password  plain-text password (will be hashed)
     * @param email     email address
     * @param studentId unique student identifier
     * @param year      year of study (1–4)
     * @param major     field of study
     */
    public Student(String userId, String login, String password, String email,
                   String studentId, int year, String major) {
        super(userId, login, password, email);
        this.studentId = studentId;
        this.year = year;
        this.major = major;
        this.credits = 0;
        this.failCount = 0;
        this.gpa = 0.0;
        this.courses = new ArrayList<>();
        this.transcript = new Transcript(this);
        this.teacherRatings = new ArrayList<>();
    }

    /**
     * Registers the student for a given course.
     * <p>
     * Validates that the course exists in the database and that
     * adding it will not exceed the credit limit ({@value MAX_CREDITS}).
     * </p>
     *
     * @param course the course to register for
     * @throws CreditLimitException     if registering would exceed the credit limit
     * @throws IllegalArgumentException if the course does not exist in the system
     */
    public void registerCourse(Course course) throws CreditLimitException {
        if (!university.patterns.Database.getInstance().getCourses().contains(course))
            throw new IllegalArgumentException(
                "Course does not exist in the system: " + course.getName());
        if (credits + course.getCredits() > MAX_CREDITS) {
            throw new CreditLimitException(credits, course.getCredits());
        }
        if (!courses.contains(course)) {
            courses.add(course);
            course.enrollStudent(this);
            credits += course.getCredits();
            System.out.println(getLogin() + " registered for: " + course.getName());
        }
    }

    /**
     * Records a mark for a given course and updates the GPA.
     * Increments fail count if the mark is not passing.
     *
     * @param course the course for which the mark is recorded
     * @param mark   the mark object containing attestation and final exam scores
     * @throws FailLimitException if the student has exceeded the maximum allowed failures
     */
    public void recordMark(Course course, Mark mark) throws FailLimitException {
        transcript.addRecord(course, mark);
        if (!mark.isPassed()) {
            failCount++;
            if (failCount > MAX_FAILS) {
                throw new FailLimitException();
            }
        }
        gpa = transcript.getGpa();
    }

    /**
     * Prints all courses the student is currently enrolled in.
     */
    public void viewCourses() {
        System.out.println("=== Courses for " + getLogin() + " ===");
        courses.forEach(c -> System.out.println("  " + c));
    }

    /**
     * Prints the student's marks for all courses.
     */
    public void viewMarks() {
        System.out.println(transcript.generateReport());
    }

    /**
     * Returns the student's academic transcript.
     *
     * @return the {@link Transcript} object for this student
     */
    public Transcript viewTranscript() {
        return transcript;
    }

    /**
     * Submits a rating for a teacher.
     * Rating must be between 1 and 5 inclusive.
     *
     * @param teacher the teacher to rate
     * @param rating  the rating value (1–5)
     */
    public void rateTeacher(Teacher teacher, int rating) {
        if (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5.");
            return;
        }
        teacher.addRating(rating);
        teacherRatings.add(rating);
        System.out.println(getLogin() + " rated " + teacher.getLogin() + ": " + rating + "/5");
    }

    /**
     * Assigns a research supervisor to this student.
     * <p>
     * Only 4th year students can have a supervisor.
     * The supervisor must have an h-index of at least 3.
     * </p>
     *
     * @param supervisor the researcher to assign as supervisor
     * @throws LowHIndexException if the supervisor's h-index is below 3
     */
    public void setSupervisor(Researcher supervisor) throws LowHIndexException {
        if (year != 4) {
            System.out.println("Only 4th year students can have a research supervisor.");
            return;
        }
        if (supervisor.getHIndex() < 3) {
            throw new LowHIndexException(supervisor.getHIndex());
        }
        this.supervisor = supervisor;
        System.out.println("Supervisor assigned to " + getLogin());
    }

    /**
     * Returns a formatted string with this student's information.
     *
     * @return string containing student ID, login, year, major, GPA, credits, and fail count
     */
    @Override
    public String getUserInfo() {
        return "Student{id='" + studentId + "', name='" + getLogin()
                + "', year=" + year + ", major='" + major
                + "', gpa=" + String.format("%.2f", gpa)
                + ", credits=" + credits + ", fails=" + failCount + "}";
    }

    /** @return the student's unique ID */
    public String getStudentId()           { return studentId; }
    /** @return current GPA */
    public double getGpa()                 { return gpa; }
    /** @return total registered credits */
    public int getCredits()                { return credits; }
    /** @return number of failed courses */
    public int getFailCount()              { return failCount; }
    /** @return academic year (1–4) */
    public int getYear()                   { return year; }
    /** @param year new academic year */
    public void setYear(int year)          { this.year = year; }
    /** @return field of study */
    public String getMajor()               { return major; }
    /** @return list of enrolled courses */
    public List<Course> getCourses()       { return courses; }
    /** @return the student's transcript */
    public Transcript getTranscript()      { return transcript; }
    /** @return assigned research supervisor, or null */
    public Researcher getSupervisor()      { return supervisor; }

    @Override
    public String toString() { return getUserInfo(); }
}
