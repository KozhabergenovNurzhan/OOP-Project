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
 * Central data store for the University Information System.
 * <p>
 * Implements the <b>Singleton</b> design pattern to ensure only one instance
 * of the database exists throughout the application lifecycle.
 * Supports serialization for persistent data storage.
 * </p>
 *
 * <p>Usage example:</p>
 * <pre>
 *   Database db = Database.getInstance();
 *   db.addUser(new Student(...));
 *   db.save();
 * </pre>
 *
 * @author Kozhabergenov Nurzhan, Yelmuratova Madina, Kainazar Nurdaulet
 * @version 1.0
 * @see university.model.User
 * @see university.model.Course
 */
public class Database implements Serializable {
    private static final long serialVersionUID = 1L;

    /** File path for serialized data storage. */
    private static final String DATA_FILE = "university_data.ser";

    /** The single instance of this class (Singleton). */
    private static Database instance;

    /** All registered users in the system. */
    private List<User> users;

    /** All courses available in the system. */
    private List<Course> courses;

    /** All research papers published in the system. */
    private List<ResearchPaper> allPapers;

    /** All active and completed research projects. */
    private List<ResearchProject> projects;

    /** System activity logs. */
    private List<Log> logs;

    /** University news feed. */
    private List<News> newsFeed;

    /**
     * Private constructor to prevent direct instantiation.
     * Initializes all internal lists.
     */
    private Database() {
        users = new ArrayList<>();
        courses = new ArrayList<>();
        allPapers = new ArrayList<>();
        projects = new ArrayList<>();
        logs = new ArrayList<>();
        newsFeed = new ArrayList<>();
    }

    /**
     * Returns the single instance of the Database.
     * Creates a new instance if one does not yet exist.
     *
     * @return the singleton Database instance
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // ── User management ───────────────────────────────────────────────

    /**
     * Adds a user to the database if not already present.
     * Logs the action automatically.
     *
     * @param user the user to add
     */
    public void addUser(User user) {
        if (!users.contains(user)) users.add(user);
        log("Added user: " + user.getLogin(), user);
    }

    /**
     * Removes a user from the database by their user ID.
     *
     * @param userId the ID of the user to remove
     */
    public void removeUser(String userId) {
        users.removeIf(u -> u.getUserId().equals(userId));
    }

    /**
     * Updates an existing user's record in the database.
     * Removes the old record and adds the updated one.
     *
     * @param user the updated user object
     */
    public void updateUser(User user) {
        removeUser(user.getUserId());
        users.add(user);
        log("Updated user: " + user.getLogin(), user);
    }

    /**
     * Finds a user by their login name.
     *
     * @param login the login name to search for
     * @return the matching User, or {@code null} if not found
     */
    public User findUserByLogin(String login) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst().orElse(null);
    }

    /**
     * Authenticates a user by login and password.
     * Logs successful login attempts.
     *
     * @param login    the login name
     * @param password the plain-text password
     * @return the authenticated User, or {@code null} if authentication fails
     */
    public User authenticate(String login, String password) {
        User user = findUserByLogin(login);
        if (user != null && user.authenticate(password)) {
            log("Login: " + login, user);
            return user;
        }
        return null;
    }

    // ── Research helpers ──────────────────────────────────────────────

    /**
     * Adds a research paper to the global papers list if not already present.
     *
     * @param paper the research paper to add
     */
    public void addPaper(ResearchPaper paper) {
        if (!allPapers.contains(paper)) allPapers.add(paper);
    }

    /**
     * Prints all research papers sorted by the given comparator.
     *
     * @param comparator the comparator to sort papers by
     */
    public void printAllPapers(Comparator<ResearchPaper> comparator) {
        System.out.println("=== All Research Papers ===");
        allPapers.stream().sorted(comparator).forEach(System.out::println);
    }

    /**
     * Prints the top cited researchers in a given department,
     * sorted by h-index in descending order.
     *
     * @param department the department to filter by
     */
    public void printTopCitedResearchers(Department department) {
        System.out.println("=== Top Cited Researchers — " + department + " ===");
        users.stream()
                .filter(u -> u instanceof Teacher)
                .map(u -> (Teacher) u)
                .filter(t -> t.isResearcher() && t.getDepartment() == department && !t.getPapers().isEmpty())
                .sorted(Comparator.comparingInt(Teacher::getHIndex).reversed())
                .forEach(t -> System.out.println(t.getLogin() + " | H-Index: " + t.getHIndex()));
    }

    /**
     * Prints the top cited researchers across all departments for a given year.
     *
     * @param year the publication year to filter by
     */
    public void printTopCitedResearchersByYear(int year) {
        System.out.println("=== Top Cited Researchers of " + year + " (all schools) ===");
        users.stream()
                .filter(u -> u instanceof Researcher)
                .map(u -> (Researcher) u)
                .filter(r -> !r.getPapers().isEmpty())
                .sorted((a, b) -> Integer.compare(citationsInYear(b, year), citationsInYear(a, year)))
                .forEach(r -> System.out.println(r + " | Citations in " + year + ": " + citationsInYear(r, year)));
    }

    /**
     * Calculates the total citations for a researcher in a given year.
     *
     * @param r    the researcher
     * @param year the year to count citations for
     * @return total citations in the given year
     */
    private int citationsInYear(Researcher r, int year) {
        return r.getPapers().stream()
                .filter(p -> p.getDatePublished().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate().getYear() == year)
                .mapToInt(ResearchPaper::getCitations)
                .sum();
    }

    /**
     * Adds a research project to the database if not already present.
     *
     * @param project the project to add
     */
    public void addProject(ResearchProject project) {
        if (!projects.contains(project)) projects.add(project);
    }

    // ── Logging ───────────────────────────────────────────────────────

    /**
     * Logs an action performed by a user.
     *
     * @param action the description of the action
     * @param user   the user who performed the action
     */
    public void log(String action, User user) {
        logs.add(new Log(action, user));
    }

    // ── Serialization ─────────────────────────────────────────────────

    /**
     * Serializes and saves the current database state to disk.
     * Saves to {@value DATA_FILE}.
     */
    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }

    /**
     * Deserializes and loads the database state from disk.
     * If no saved file exists, the current instance remains unchanged.
     */
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

    /** @return all registered users */
    public List<User> getUsers()                { return users; }
    /** @return all available courses */
    public List<Course> getCourses()            { return courses; }
    /** @return all research papers */
    public List<ResearchPaper> getAllPapers()    { return allPapers; }
    /** @return all research projects */
    public List<ResearchProject> getProjects()  { return projects; }
    /** @return system activity logs */
    public List<Log> getLogs()                  { return logs; }
    /** @return university news feed */
    public List<News> getNewsFeed()             { return newsFeed; }

    /**
     * Returns all students registered in the system.
     *
     * @return list of Student objects
     */
    public List<Student> getStudents() {
        return users.stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .collect(Collectors.toList());
    }

    /**
     * Returns all teachers registered in the system.
     *
     * @return list of Teacher objects
     */
    public List<Teacher> getTeachers() {
        return users.stream()
                .filter(u -> u instanceof Teacher)
                .map(u -> (Teacher) u)
                .collect(Collectors.toList());
    }
}
