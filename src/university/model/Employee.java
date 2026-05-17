package university.model;

import university.enums.Department;

import java.io.Serializable;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public abstract class Employee extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String employeeId;
    private double salary;
    private Department department;
    private List<Message> inbox;

    public Employee(String userId, String login, String password, String email,
                    String employeeId, double salary, Department department) {
        super(userId, login, password, email);
        this.employeeId = employeeId;
        this.salary = salary;
        this.department = department;
        this.inbox = new ArrayList<>();
    }

    public void sendMessage(Employee to, String content) {
        String id = "msg-" + UUID.randomUUID();
        Message msg = new Message(id, this, to, content);
        to.receiveMessage(msg);
        System.out.println("Message sent to " + to.getLogin());
    }

    public void receiveMessage(Message msg) {
        inbox.add(msg);
    }

    public void viewMessages() {
        if (inbox.isEmpty()) {
            System.out.println("No messages.");
            return;
        }
        inbox.forEach(System.out::println);
    }

    public void sendComplaint(String text) {
        System.out.println("Complaint filed by " + getLogin() + ": " + text);
    }

    // Getters & Setters
    public String getEmployeeId()              { return employeeId; }
    public double getSalary()                  { return salary; }
    public void setSalary(double salary)       { this.salary = salary; }
    public Department getDepartment()          { return department; }
    public void setDepartment(Department d)    { this.department = d; }
    public List<Message> getInbox()            { return inbox; }
}
