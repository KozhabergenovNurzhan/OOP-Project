package university.model;

import university.enums.Department;
import university.enums.ManagerType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Manager extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private ManagerType managerType;
    private List<Course> managedCourses;
    private List<News> newsList;

    public Manager(String userId, String login, String password, String email,
                   String employeeId, double salary, Department department,
                   ManagerType managerType) {
        super(userId, login, password, email, employeeId, salary, department);
        this.managerType = managerType;
        this.managedCourses = new ArrayList<>();
        this.newsList = new ArrayList<>();
    }

    public void approveRegistration(Student student, Course course) {
        System.out.println("Registration approved: " + student.getLogin()
                + " → " + course.getName());
    }

    public void addCourseForRegistration(Course course, String major, int year) {
        if (!managedCourses.contains(course)) {
            managedCourses.add(course);
            System.out.println("Course added for registration: " + course.getName()
                    + " | Major: " + major + " | Year: " + year);
        }
    }

    public void assignCourseToTeacher(Course course, Teacher teacher) {
        teacher.manageCourse(course);
        System.out.println("Assigned " + course.getName() + " to " + teacher.getLogin());
    }

    public void viewStudents(List<Student> students, Comparator<Student> comparator) {
        System.out.println("=== Students ===");
        students.stream().sorted(comparator).forEach(System.out::println);
    }

    public void viewTeachers(List<Teacher> teachers, Comparator<Teacher> comparator) {
        System.out.println("=== Teachers ===");
        teachers.stream().sorted(comparator).forEach(System.out::println);
    }

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

    public void manageNews(News news) {
        newsList.add(news);
        System.out.println("News published: " + news.getTitle());
    }

    public void viewRequests() {
        // Placeholder: in a full system, requests come from Employees
        System.out.println("No pending requests.");
    }

    @Override
    public String getUserInfo() {
        return "Manager{name='" + getLogin() + "', type=" + managerType + "}";
    }

    public ManagerType getManagerType()           { return managerType; }
    public List<Course> getManagedCourses()       { return managedCourses; }
    public List<News> getNewsList()               { return newsList; }

    @Override
    public String toString() { return getUserInfo(); }
}
