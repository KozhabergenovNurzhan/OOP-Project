package university.model;

import university.enums.Department;
import university.patterns.Database;

import java.io.Serializable;
import java.util.List;

public class Admin extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private String adminId;

    public Admin(String userId, String login, String password, String email,
                 String employeeId, double salary, Department department, String adminId) {
        super(userId, login, password, email, employeeId, salary, department);
        this.adminId = adminId;
    }

    public void addUser(User user) {
        Database.getInstance().addUser(user);
        System.out.println("User added: " + user.getLogin());
    }

    public void removeUser(String userId) {
        Database.getInstance().removeUser(userId);
        System.out.println("User removed: " + userId);
    }

    public void updateUser(User user) {
        Database.getInstance().updateUser(user);
        System.out.println("User updated: " + user.getLogin());
    }

    public void viewLogs() {
        List<Log> logs = Database.getInstance().getLogs();
        if (logs.isEmpty()) {
            System.out.println("No logs found.");
            return;
        }
        System.out.println("=== System Logs ===");
        logs.forEach(System.out::println);
    }

    @Override
    public String getUserInfo() {
        return "Admin{id='" + adminId + "', login='" + getLogin() + "'}";
    }

    public String getAdminId() { return adminId; }

    @Override
    public String toString() { return getUserInfo(); }
}
