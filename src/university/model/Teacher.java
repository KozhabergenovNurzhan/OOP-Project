package university.model;

import university.enums.Department;
import university.enums.TeacherTitle;
import university.exceptions.FailLimitException;
import university.model.research.Researcher;
import university.model.research.ResearchPaper;
import university.model.research.ResearchProject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a teacher (instructor) in the University System.
 * <p>
 * A teacher can manage courses, put marks for enrolled students,
 * send messages to other employees, and optionally conduct research.
 * Teachers with the title {@link TeacherTitle#PROFESSOR} are automatically
 * designated as researchers.
 * </p>
 *
 * @author Kozhabergenov Nurzhan, Yelmuratova Madina, Kainazar Nurdaulet
 * @version 1.0
 * @see Employee
 * @see Researcher
 * @see Course
 */
public class Teacher extends Employee implements Researcher, Serializable {
    private static final long serialVersionUID = 1L;

    /** Unique teacher identifier. */
    private String teacherId;

    /** Academic title of this teacher. */
    private TeacherTitle title;

    /** List of courses assigned to this teacher. */
    private List<Course> courses;

    /** List of ratings received from students. */
    private List<Integer> ratings;

    /** List of research papers published by this teacher. */
    private List<ResearchPaper> papers;

    /** List of research projects this teacher participates in. */
    private List<ResearchProject> projects;

    /** Indicates whether this teacher is a researcher. Professors are always researchers. */
    private boolean isResearcher;

    /**
     * Constructs a new Teacher with the given details.
     * Professors are automatically set as researchers.
     *
     * @param userId       unique user identifier
     * @param login        login name for authentication
     * @param password     plain-text password (will be hashed)
     * @param email        email address
     * @param employeeId   unique employee identifier
     * @param salary       monthly salary
     * @param department   department this teacher belongs to
     * @param teacherId    unique teacher identifier
     * @param title        academic title (TUTOR, LECTURER, SENIOR_LECTURER, PROFESSOR)
     */
    public Teacher(String userId, String login, String password, String email,
                   String employeeId, double salary, Department department,
                   String teacherId, TeacherTitle title) {
        super(userId, login, password, email, employeeId, salary, department);
        this.teacherId = teacherId;
        this.title = title;
        this.courses = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
        this.isResearcher = (title == TeacherTitle.PROFESSOR);
    }

    /**
     * Prints all courses assigned to this teacher.
     */
    public void viewCourses() {
        System.out.println("=== Courses taught by " + getLogin() + " ===");
        courses.forEach(c -> System.out.println("  " + c));
    }

    /**
     * Assigns this teacher to a course.
     * If the teacher is not already an instructor, adds them to the course.
     *
     * @param course the course to manage
     */
    public void manageCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
            course.addInstructor(this);
            System.out.println(getLogin() + " assigned to course: " + course.getName());
        }
    }

    /**
     * Prints all students enrolled in a given course.
     *
     * @param course the course whose students to display
     */
    public void viewStudents(Course course) {
        System.out.println("=== Students in " + course.getName() + " ===");
        course.getEnrolledStudents().forEach(System.out::println);
    }

    /**
     * Records a mark for a student in a specific course.
     * <p>
     * Validates that this teacher is assigned to the course and
     * that the student is enrolled in it before recording the mark.
     * </p>
     *
     * @param student the student to grade
     * @param course  the course for which the mark is given
     * @param mark    the mark object with attestation and exam scores
     * @throws FailLimitException       if the student exceeds the maximum fail limit
     * @throws IllegalArgumentException if the teacher is not assigned to the course,
     *                                  or the student is not enrolled
     */
    public void putMark(Student student, Course course, Mark mark) throws FailLimitException {
        if (!courses.contains(course))
            throw new IllegalArgumentException(
                "Teacher " + getLogin() + " is not assigned to course: " + course.getName());
        if (!course.getEnrolledStudents().contains(student))
            throw new IllegalArgumentException(
                "Student " + student.getLogin() + " is not enrolled in: " + course.getName());
        student.recordMark(course, mark);
        System.out.println("Mark recorded for " + student.getLogin() + " in " + course.getName());
    }

    /**
     * Adds a student rating to this teacher's rating list.
     *
     * @param rating the rating value (1–5)
     */
    public void addRating(int rating) {
        ratings.add(rating);
    }

    /**
     * Calculates and returns the average student rating for this teacher.
     *
     * @return average rating, or 0.0 if no ratings exist
     */
    public double getAverageRating() {
        return ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    /**
     * Adds a research paper to this teacher's publication list.
     * Only available if the teacher is a researcher.
     *
     * @param paper the research paper to add
     */
    @Override
    public void addPaper(ResearchPaper paper) {
        if (!isResearcher) {
            System.out.println(getLogin() + " is not a researcher.");
            return;
        }
        papers.add(paper);
    }

    /**
     * Returns all research papers published by this teacher.
     *
     * @return list of research papers
     */
    @Override
    public List<ResearchPaper> getPapers() { return papers; }

    /**
     * Prints all research papers sorted by the given comparator.
     *
     * @param comparator the comparator to sort papers by (e.g. by citations, pages, or date)
     */
    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        Researcher.printPapersSorted(getLogin(), papers, comparator);
    }

    /**
     * Returns all research projects this teacher participates in.
     *
     * @return list of research projects
     */
    @Override
    public List<ResearchProject> getProjects() { return projects; }

    /**
     * Adds this teacher to a research project.
     * Only available if the teacher is a researcher.
     *
     * @param project the research project to join
     */
    @Override
    public void joinProject(ResearchProject project) {
        if (!isResearcher) return;
        if (!projects.contains(project)) projects.add(project);
    }

    /**
     * Returns a formatted string with this teacher's information.
     *
     * @return string with teacher ID, login, title, rating, and researcher status
     */
    @Override
    public String getUserInfo() {
        return "Teacher{id='" + teacherId + "', name='" + getLogin()
                + "', title=" + title
                + ", rating=" + String.format("%.1f", getAverageRating())
                + ", researcher=" + isResearcher + "}";
    }

    /** @return the teacher's unique ID */
    public String getTeacherId()          { return teacherId; }
    /** @return the teacher's academic title */
    public TeacherTitle getTitle()        { return title; }

    /**
     * Updates the teacher's academic title.
     * If set to PROFESSOR, automatically enables researcher status.
     *
     * @param t the new academic title
     */
    public void setTitle(TeacherTitle t)  {
        this.title = t;
        if (t == TeacherTitle.PROFESSOR) this.isResearcher = true;
    }
    /** @return list of assigned courses */
    public List<Course> getCourses()      { return courses; }
    /** @return true if this teacher is a researcher */
    public boolean isResearcher()         { return isResearcher; }
    /** @param r set researcher status */
    public void setResearcher(boolean r)  { this.isResearcher = r; }

    @Override
    public String toString() { return getUserInfo(); }
}
