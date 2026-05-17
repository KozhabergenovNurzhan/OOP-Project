package university.model;

import university.enums.Department;
import university.enums.ManagerType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a manager in the University Information System.
 * <p>
 * A manager is responsible for approving course registrations,
 * assigning courses to teachers, generating academic performance reports,
 * and managing university news. Manager types include OR (Office of Registrar),
 * Department, Dean, and Rector.
 * </p>
 *
 * @author Kozhabergenov Nurzhan, Yelmuratova Madina, Kainazar Nurdaulet
 * @version 1.0
 * @see Employee
 * @see ManagerType
 */
public class Manager extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The type of manager (OR, DEPARTMENT, DEAN, RECTOR). */
    private ManagerType managerType;

    /** List of courses managed by this manager. */
    private List<Course> managedCourses;

    /** List of news items published by this manager. */
    private List<News> newsList;

    /**
     * Constructs a new Manager with the given details.
     *
     * @param userId       unique user identifier
     * @param login        login name for authentication
     * @param password     plain-text password (will be hashed)
     * @param email        email address
     * @param employeeId   unique employee identifier
     * @param salary       monthly salary
     * @param department   department this manager belongs to
     * @param managerType  type of manager role
     */
    public Manager(String userId, String login, String password, String email,
                   String employeeId, double salary, Department department,
                   ManagerType managerType) {
        super(userId, login, password, email, employeeId, salary, department);
        this.managerType = managerType;
        this.managedCourses = new ArrayList<>();
        this.newsList = new ArrayList<>();
    }

    /**
     * Approves a student's registration for a course.
     *
     * @param student the student whose registration is approved
     * @param course  the course the student is registering for
     */
    public void approveRegistration(Student student, Course course) {
        System.out.println("Registration approved: " + student.getLogin()
                + " → " + course.getName());
    }

    /**
     * Adds a course to the list of courses available for registration.
     *
     * @param course the course to add
     * @param major  the major this course is intended for
     * @param year   the academic year this course is intended for
     */
    public void addCourseForRegistration(Course course, String major, int year) {
        if (!managedCourses.contains(course)) {
            managedCourses.add(course);
            System.out.println("Course added for registration: " + course.getName()
                    + " | Major: " + major + " | Year: " + year);
        }
    }

    /**
     * Assigns a course to a teacher.
     *
     * @param course  the course to assign
     * @param teacher the teacher to assign the course to
     */
    public void assignCourseToTeacher(Course course, Teacher teacher) {
        teacher.manageCourse(course);
        System.out.println("Assigned " + course.getName() + " to " + teacher.getLogin());
    }

    /**
     * Prints a list of students sorted by the given comparator.
     *
     * @param students   list of students to display
     * @param comparator comparator to sort students (e.g. by GPA or name)
     */
    public void viewStudents(List<Student> students, Comparator<Student> comparator) {
        System.out.println("=== Students ===");
        students.stream().sorted(comparator).forEach(System.out::println);
    }

    /**
     * Prints a list of teachers sorted by the given comparator.
     *
     * @param teachers   list of teachers to display
     * @param comparator comparator to sort teachers
     */
    public void viewTeachers(List<Teacher> teachers, Comparator<Teacher> comparator) {
        System.out.println("=== Teachers ===");
        teachers.stream().sorted(comparator).forEach(System.out::println);
    }

    /**
     * Generates a statistical academic performance report for a list of students.
     * Displays total students, average GPA, and number of failing students.
     *
     * @param students list of students to include in the report
     */
    public void generateReport(List<Student> students) {
        System.out.println("=== Academic Performance Report ===");
        double avgGpa = students.stream()
                .mapToDouble(Student::getGpa)
                .average().orElse(0.0);
        long failing = students.stream()
                .filter(s -> s.getGpa() < 2.0).count();
        System.out.printf("Total students: %d%n", students.size());
        System.out.printf("Average GPA:    %.2f%n", avgGpa);
        System.out.printf("Failing (GPA<2.0): %d%n", failing);
    }

    /**
     * Publishes a news item to the university news feed.
     *
     * @param news the news item to publish
     */
    public void manageNews(News news) {
        newsList.add(news);
        System.out.println("News published: " + news.getTitle());
    }

    /**
     * Displays pending requests from employees.
     * Requests must be signed by Dean or Rector.
     */
    public void viewRequests() {
        System.out.println("No pending requests.");
    }

    /**
     * Returns a formatted string with this manager's information.
     *
     * @return string with manager login and type
     */
    @Override
    public String getUserInfo() {
        return "Manager{name='" + getLogin() + "', type=" + managerType + "}";
    }

    /** @return the manager type */
    public ManagerType getManagerType()           { return managerType; }
    /** @return list of managed courses */
    public List<Course> getManagedCourses()       { return managedCourses; }
    /** @return list of published news */
    public List<News> getNewsList()               { return newsList; }

    @Override
    public String toString() { return getUserInfo(); }
}
