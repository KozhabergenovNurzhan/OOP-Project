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

public class Teacher extends Employee implements Researcher, Serializable {
    private static final long serialVersionUID = 1L;

    private String teacherId;
    private TeacherTitle title;
    private List<Course> courses;
    private List<Integer> ratings;
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;
    private boolean isResearcher;

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

    public void viewCourses() {
        System.out.println("=== Courses taught by " + getLogin() + " ===");
        courses.forEach(c -> System.out.println("  " + c));
    }

    public void manageCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
            course.addInstructor(this);
            System.out.println(getLogin() + " assigned to course: " + course.getName());
        }
    }

    public void viewStudents(Course course) {
        System.out.println("=== Students in " + course.getName() + " ===");
        course.getEnrolledStudents().forEach(System.out::println);
    }

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

    public void addRating(int rating) {
        ratings.add(rating);
    }

    public double getAverageRating() {
        return ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    @Override
    public void addPaper(ResearchPaper paper) {
        if (!isResearcher) {
            System.out.println(getLogin() + " is not a researcher.");
            return;
        }
        papers.add(paper);
    }

    @Override
    public List<ResearchPaper> getPapers() { return papers; }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        Researcher.printPapersSorted(getLogin(), papers, comparator);
    }

    @Override
    public List<ResearchProject> getProjects() { return projects; }

    @Override
    public void joinProject(ResearchProject project) {
        if (!isResearcher) return;
        if (!projects.contains(project)) projects.add(project);
    }

    @Override
    public String getUserInfo() {
        return "Teacher{id='" + teacherId + "', name='" + getLogin()
                + "', title=" + title
                + ", rating=" + String.format("%.1f", getAverageRating())
                + ", researcher=" + isResearcher + "}";
    }

    // Getters & Setters
    public String getTeacherId()          { return teacherId; }
    public TeacherTitle getTitle()        { return title; }
    public void setTitle(TeacherTitle t)  {
        this.title = t;
        if (t == TeacherTitle.PROFESSOR) this.isResearcher = true;
    }
    public List<Course> getCourses()      { return courses; }
    public boolean isResearcher()         { return isResearcher; }
    public void setResearcher(boolean r)  { this.isResearcher = r; }

    @Override
    public String toString() { return getUserInfo(); }
}
