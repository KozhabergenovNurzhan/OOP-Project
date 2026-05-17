package university.patterns;

import university.enums.Department;
import university.model.*;
import university.model.research.ResearchPaper;
import university.model.research.ResearchProject;
import university.model.research.Researcher;

import java.io.*;
import java.util.ArrayList;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton pattern: single instance of the data store.
 * Also handles serialization (save/load).
 */
public class Database implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "university_data.ser";

    private static Database instance;

    private List<User> users;
    private List<Course> courses;
    private List<ResearchPaper> allPapers;
    private List<ResearchProject> projects;
    private List<Log> logs;
    private List<News> newsFeed;

    private Database() {
        users = new ArrayList<>();
        courses = new ArrayList<>();
        allPapers = new ArrayList<>();
        projects = new ArrayList<>();
        logs = new ArrayList<>();
        newsFeed = new ArrayList<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // ── User management ───────────────────────────────────────────────

    public void addUser(User user) {
        if (!users.contains(user)) users.add(user);
        log("Added user: " + user.getLogin(), user);
    }

    public void removeUser(String userId) {
        users.removeIf(u -> u.getUserId().equals(userId));
    }

    public void updateUser(User user) {
        removeUser(user.getUserId());
        users.add(user);
        log("Updated user: " + user.getLogin(), user);
    }

    public User findUserByLogin(String login) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst().orElse(null);
    }

    public User authenticate(String login, String password) {
        User user = findUserByLogin(login);
        if (user != null && user.authenticate(password)) {
            log("Login: " + login, user);
            return user;
        }
        return null;
    }

    // ── Research helpers ──────────────────────────────────────────────

    public void addPaper(ResearchPaper paper) {
        if (!allPapers.contains(paper)) allPapers.add(paper);
    }

    public void printAllPapers(Comparator<ResearchPaper> comparator) {
        System.out.println("=== All Research Papers ===");
        allPapers.stream().sorted(comparator).forEach(System.out::println);
    }

    public void printTopCitedResearchers(Department department) {
        System.out.println("=== Top Cited Researchers — " + department + " ===");
        users.stream()
                .filter(u -> u instanceof Teacher)
                .map(u -> (Teacher) u)
                .filter(t -> t.isResearcher() && t.getDepartment() == department && !t.getPapers().isEmpty())
                .sorted(Comparator.comparingInt(Teacher::getHIndex).reversed())
                .forEach(t -> System.out.println(t.getLogin() + " | H-Index: " + t.getHIndex()));
    }

    public void printTopCitedResearchersByYear(int year) {
        System.out.println("=== Top Cited Researchers of " + year + " (all schools) ===");
        users.stream()
                .filter(u -> u instanceof Researcher)
                .map(u -> (Researcher) u)
                .filter(r -> !r.getPapers().isEmpty())
                .sorted((a, b) -> Integer.compare(citationsInYear(b, year), citationsInYear(a, year)))
                .forEach(r -> System.out.println(r + " | Citations in " + year + ": " + citationsInYear(r, year)));
    }

    private int citationsInYear(Researcher r, int year) {
        return r.getPapers().stream()
                .filter(p -> p.getDatePublished().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate().getYear() == year)
                .mapToInt(ResearchPaper::getCitations)
                .sum();
    }

    public void addProject(ResearchProject project) {
        if (!projects.contains(project)) projects.add(project);
    }

    // ── Logging ───────────────────────────────────────────────────────

    public void log(String action, User user) {
        logs.add(new Log(action, user));
    }

    // ── Serialization ─────────────────────────────────────────────────

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }

    public static void load() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("No saved data found, starting fresh.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            instance = (Database) ois.readObject();
            System.out.println("Data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Load failed: " + e.getMessage());
        }
    }

    // ── Getters ───────────────────────────────────────────────────────

    public List<User> getUsers()                { return users; }
    public List<Course> getCourses()            { return courses; }
    public List<ResearchPaper> getAllPapers()    { return allPapers; }
    public List<ResearchProject> getProjects()  { return projects; }
    public List<Log> getLogs()                  { return logs; }
    public List<News> getNewsFeed()             { return newsFeed; }

    public List<Student> getStudents() {
        return users.stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .collect(Collectors.toList());
    }

    public List<Teacher> getTeachers() {
        return users.stream()
                .filter(u -> u instanceof Teacher)
                .map(u -> (Teacher) u)
                .collect(Collectors.toList());
    }
}
