package university.patterns;

import university.enums.Department;
import university.enums.ManagerType;
import university.enums.TeacherTitle;
import university.model.*;

import java.util.UUID;

/**
 * Factory pattern: creates User instances without exposing constructors.
 */
public class UserFactory {

    private UserFactory() {} // static utility class

    public static Student createStudent(String login, String password, String email,
                                        int year, String major) {
        String id = "STU-" + UUID.randomUUID().toString().substring(0, 8);
        return new Student(id, login, password, email, id, year, major);
    }

    public static Teacher createTeacher(String login, String password, String email,
                                        double salary, Department department,
                                        TeacherTitle title) {
        String id = "TCH-" + UUID.randomUUID().toString().substring(0, 8);
        return new Teacher(id, login, password, email, id, salary, department, id, title);
    }

    public static Manager createManager(String login, String password, String email,
                                        double salary, Department department,
                                        ManagerType type) {
        String id = "MGR-" + UUID.randomUUID().toString().substring(0, 8);
        return new Manager(id, login, password, email, id, salary, department, type);
    }

    public static Admin createAdmin(String login, String password, String email) {
        String id = "ADM-" + UUID.randomUUID().toString().substring(0, 8);
        return new Admin(id, login, password, email, id, 0, Department.SCHOOL_OF_INFORMATION_TECHNOLOGY_AND_ENGINEERING, id);
    }
}
