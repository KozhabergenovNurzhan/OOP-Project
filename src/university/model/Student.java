package university.model;

import university.exceptions.CreditLimitException;
import university.exceptions.FailLimitException;
import university.exceptions.LowHIndexException;
import university.model.research.Researcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int MAX_CREDITS = 30;
    private static final int MAX_FAILS = 3;

    private String studentId;
    private double gpa;
    private int credits;
    private int failCount;
    private int year;           // 1–4
    private String major;
    private List<Course> courses;
    private Transcript transcript;
    private Researcher supervisor;
    private List<Integer> teacherRatings;

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

    public void viewCourses() {
        System.out.println("=== Courses for " + getLogin() + " ===");
        courses.forEach(c -> System.out.println("  " + c));
    }

    public void viewMarks() {
        System.out.println(transcript.generateReport());
    }

    public Transcript viewTranscript() {
        return transcript;
    }

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
     * Assigns a research supervisor (only for 4th year students).
     * Throws LowHIndexException if supervisor's h-index < 3.
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

    @Override
    public String getUserInfo() {
        return "Student{id='" + studentId + "', name='" + getLogin()
                + "', year=" + year + ", major='" + major
                + "', gpa=" + String.format("%.2f", gpa)
                + ", credits=" + credits + ", fails=" + failCount + "}";
    }

    // Getters & Setters
    public String getStudentId()           { return studentId; }
    public double getGpa()                 { return gpa; }
    public int getCredits()                { return credits; }
    public int getFailCount()              { return failCount; }
    public int getYear()                   { return year; }
    public void setYear(int year)          { this.year = year; }
    public String getMajor()               { return major; }
    public List<Course> getCourses()       { return courses; }
    public Transcript getTranscript()      { return transcript; }
    public Researcher getSupervisor()      { return supervisor; }

    @Override
    public String toString() { return getUserInfo(); }
}
