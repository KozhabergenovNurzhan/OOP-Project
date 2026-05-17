package university.patterns;

import university.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer pattern: users subscribe and get notified of system events.
 */
public class NotificationService {

    private static NotificationService instance;
    private List<User> observers;

    private NotificationService() {
        observers = new ArrayList<>();
    }

    public static NotificationService getInstance() {
        if (instance == null) instance = new NotificationService();
        return instance;
    }

    public void subscribe(User user) {
        if (!observers.contains(user)) {
            observers.add(user);
        }
    }

    public void unsubscribe(User user) {
        observers.remove(user);
    }

    public void notifyAll(String event) {
        System.out.println("[NOTIFICATION] " + event);
        // In a real system, push to each user's notification list
        observers.forEach(u ->
            System.out.println("  → Notified: " + u.getLogin())
        );
    }

    public void notifyUser(User user, String event) {
        System.out.println("[NOTIFICATION → " + user.getLogin() + "] " + event);
    }
}
