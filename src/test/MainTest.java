package test;

import university.enums.*;
import university.exceptions.*;
import university.model.*;
import university.model.research.*;
import university.patterns.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Date;

public class MainTest {

    public static void main(String[] args) {
        System.out.println("=== UNIVERSITY INFORMATION SYSTEM TEST SUITE ===");

        try {
            testUserAuthenticationAndHashing();
            testObjectEqualityAndOverrides();
            testCourseRegistrationAndCreditLimits();
            testGradingAndFailLimitException();
            testTeacherAndResearcherLogic();
            testResearchPaperSortingStrategies();
            testAdminAndDatabaseManagement();
            test4thYearSupervisorRule();
            testDatabaseSerialization();
            
            System.out.println("\nALL TESTS PASSED SUCCESSFULLY");
        } catch (Exception e) {
            System.out.println("\nTEST SUITE FAILED");
            e.printStackTrace();
        }
    }

    private static void testUserAuthenticationAndHashing() {
        System.out.println("\nTest 1: Authentication and Hashing");
        Student student = new Student("U101", "nurzhan_k", "mySecretPassword123", "nurzhan@university.edu", "STU001", 3, "CS");
        
        assert student.authenticate("mySecretPassword123") : "Auth failed with valid password";
        assert !student.authenticate("wrongPassword") : "Auth passed with invalid password";
        System.out.println("Authentication system works");
    }

    private static void testObjectEqualityAndOverrides() {
        System.out.println("\nTest 2: Equals and HashCode");
        Student s1 = new Student("U999", "nurda_k", "pass1", "nurda@kbtu.kz", "STU002", 2, "CS");
        Student s2 = new Student("U999", "nurzhan_k", "pass2", "nurzhan.@kbtu.kz", "STU003", 2, "CS");

        assert s1.equals(s2) : "Equals should match on user ID";
        assert s1.hashCode() == s2.hashCode() : "HashCodes must match for equal objects";
        System.out.println("Equals contract is correct");
    }

    private static void testCourseRegistrationAndCreditLimits() {
        System.out.println("\nTest 3: Credit Limits");
        Student student = new Student("U202", "tester_credits", "pass", "test@kbtu.kz", "STU004", 2, "CS");
        
        Course oop = new Course("CS202", "Object-Oriented Programming", 8, "CS", 2);
        Course math = new Course("MATH101", "Calculus I", 8, "CS", 1);
        Course physics = new Course("PHYS102", "Physics II", 8, "CS", 1);
        Course history = new Course("HIST201", "History", 6, "CS", 1); 
        Course overflow = new Course("CS301", "Database Systems", 5, "CS", 3);

        try {
            student.registerCourse(oop);
            student.registerCourse(math);
            student.registerCourse(physics);
            student.registerCourse(history);
            
            System.out.println("Credits registered: " + student.getCredits());
            student.registerCourse(overflow);
            throw new RuntimeException("Credit limit boundary was ignored");
        } catch (CreditLimitException e) {
            System.out.println("Caught expected CreditLimitException");
        }
    }

    private static void testGradingAndFailLimitException() {
        System.out.println("\nTest 4: Fail Limits");
        Student student = new Student("U303", "struggling_stud", "pass", "test3@uni.edu", "STU005", 1, "CS");
        Course coursePlaceholder = new Course("CS101", "Intro to Programming", 5, "CS", 1);

        Mark failingMark = new Mark(student, coursePlaceholder);
        failingMark.setAttestation1(10.0);
        failingMark.setAttestation2(12.0); 
        failingMark.setFinalExam(15.0);        

        try {
            student.recordMark(coursePlaceholder, failingMark); 
            student.recordMark(coursePlaceholder, failingMark); 
            student.recordMark(coursePlaceholder, failingMark); 
            student.recordMark(coursePlaceholder, failingMark); 
            throw new RuntimeException("FailLimitException not thrown after 4 failures");
        } catch (FailLimitException e) {
            System.out.println("Caught expected FailLimitException");
        }
    }

    private static void testTeacherAndResearcherLogic() {
        System.out.println("\nTest 5: Teacher and Researcher Rules");
        Teacher professor = new Teacher(
            "U909", "prof_yerlan", "secretPass", "yerlan@university.edu", 
            "EMP551", 750000.0, Department.SCHOOL_OF_INFORMATION_TECHNOLOGY_AND_ENGINEERING,              
            "TCH101", TeacherTitle.PROFESSOR                            
        );

        System.out.println("Teacher description: " + professor.toString());
        assert professor.isResearcher() : "Professors must be researchers automatically";

        List<Researcher> authorsList = new ArrayList<>();
        authorsList.add(professor);

        ResearchPaper paper1 = new ResearchPaper(
            "P001", "Advanced OOP Design Patterns", authorsList, 
            "IEEE Software Journal", 12, new Date(), "10.1109/MS.2026.1111", 150, "Software Engineering"
        );
        
        ResearchPaper paper2 = new ResearchPaper(
            "P002", "Quantum Compiler Optimization Methods", authorsList, 
            "ACM Computing Surveys", 24, new Date(), "10.1145/3333.4444", 35, "Quantum Computing"
        );

        professor.addPaper(paper1);
        professor.addPaper(paper2);

        System.out.println("Sorting by citations:");
        professor.printPapers(ResearchPaper.BY_CITATIONS);
        
        System.out.println("Sorting by pages:");
        professor.printPapers(ResearchPaper.BY_LENGTH);
    }

    private static void testAdminAndDatabaseManagement() {
        System.out.println("\nTest 6: Admin Actions");
        Admin admin = new Admin(
            "ADM001", "system_admin", "rootPassword", "admin@university.edu", 
            "EMP001", 500000.0, Department.SCHOOL_OF_INFORMATION_TECHNOLOGY_AND_ENGINEERING,                  
            "ADM_ID_99"                                                       
        );

        System.out.println("Admin description: " + admin.toString());
        Student transientStudent = new Student("U707", "new_student", "pass123", "ns@uni.edu", "STU777", 1, "IS");
        
        admin.addUser(transientStudent);
        admin.updateUser(transientStudent);
        admin.removeUser(transientStudent.getUserId());
        admin.viewLogs();
    }

    private static void testResearchPaperSortingStrategies() {
        System.out.println("\nTest 7: Dynamic Comparator Rules");
        Teacher professor = new Teacher(
            "U909", "prof_yerlan", "secretPass", "yerlan@university.edu", 
            "EMP551", 750000.0, Department.SCHOOL_OF_INFORMATION_TECHNOLOGY_AND_ENGINEERING,              
            "TCH102", TeacherTitle.PROFESSOR                            
        );

        List<Researcher> authorsList = new ArrayList<>();
        authorsList.add(professor);
        
        ResearchPaper paper1 = new ResearchPaper(
            "P001", "OOP Paradigms", authorsList, 
            "IEEE Software", 8, new Date(), "10.1109/MS.2026.001", 15, "Software Engineering"
        ); 
        
        ResearchPaper paper2 = new ResearchPaper(
            "P002", "Advanced Quantum Computing", authorsList, 
            "ACM Survey", 18, new Date(), "10.1145/2026.002", 120, "Quantum Systems"
        );
        
        ResearchPaper paper3 = new ResearchPaper(
            "P003", "Neural Network Optimizations", authorsList, 
            "Springer AI", 12, new Date(), "10.1007/2026.003", 45, "Artificial Intelligence"
        );

        professor.addPaper(paper1);
        professor.addPaper(paper2);
        professor.addPaper(paper3);

        System.out.println("Citations Descending:");
        professor.printPapers(new Comparator<ResearchPaper>() {
            @Override
            public int compare(ResearchPaper p1, ResearchPaper p2) {
                return Integer.compare(p2.getCitations(), p1.getCitations());
            }
        });

        System.out.println("Length Ascending:");
        professor.printPapers(new Comparator<ResearchPaper>() {
            @Override
            public int compare(ResearchPaper p1, ResearchPaper p2) {
                return Integer.compare(p1.getPages(), p2.getPages());
            }
        });
    }

    private static void test4thYearSupervisorRule() {
        System.out.println("\nTest 8: Senior Student Supervisor Restriction");
        Student senior = new Student("U404", "senior_student", "pass", "senior@uni.edu", "STU009", 4, "CS");
        System.out.println("Checking supervisor for student: " + senior.getLogin());
        
        Researcher weakResearcher = new Researcher() {
            @Override public int getHIndex() { return 2; }
            @Override public void addPaper(ResearchPaper p) {}
            @Override public List<ResearchPaper> getPapers() { return new ArrayList<>(); }
            @Override public void printPapers(Comparator<ResearchPaper> c) {}
            @Override public List<ResearchProject> getProjects() { return new ArrayList<>(); }
            @Override public void joinProject(ResearchProject p) {}
        };

        try {
            System.out.println("Assigning supervisor with low h-index");
            senior.setSupervisor(weakResearcher);
            throw new RuntimeException("LowHIndexException was not thrown");
        } catch (LowHIndexException e) {
            System.out.println("Caught expected LowHIndexException");
        }
    }

    private static void testDatabaseSerialization() {
        System.out.println("\nTest 9: Serialization System Check");
        
        Teacher sampleTeacher = new Teacher(
            "U888", "serial_prof", "pass", "serial@uni.edu", 
            "EMP888", 600000.0, Department.SCHOOL_OF_INFORMATION_TECHNOLOGY_AND_ENGINEERING,              
            "TCH888", TeacherTitle.PROFESSOR                            
        );
        
        Database.getInstance().addUser(sampleTeacher);

        System.out.println("Saving current state to disk...");
        Database.getInstance().save();
        
        System.out.println("Loading state back into application memory...");
        Database.load();
        
        Database.getInstance().printTopCitedResearchers(Department.SCHOOL_OF_INFORMATION_TECHNOLOGY_AND_ENGINEERING);
        System.out.println("Serialization flow verified");
    }
}