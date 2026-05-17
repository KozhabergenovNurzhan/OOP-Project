package university.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Transcript implements Serializable {
    private static final long serialVersionUID = 1L;

    private Student student;
    private Map<Course, Mark> records; // course → mark

    public Transcript(Student student) {
        this.student = student;
        this.records = new HashMap<>();
    }

    public void addRecord(Course course, Mark mark) {
        records.put(course, mark);
    }

    public Mark getMark(Course course) {
        return records.get(course);
    }

    public double getGpa() {
        if (records.isEmpty()) return 0.0;

        double totalPoints = 0;
        int totalCredits = 0;

        for (Map.Entry<Course, Mark> entry : records.entrySet()) {
            int credits = entry.getKey().getCredits();
            double total = entry.getValue().computeTotal();
            double gradePoint = totalToGradePoint(total);
            totalPoints += gradePoint * credits;
            totalCredits += credits;
        }

        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }

    private double totalToGradePoint(double total) {
        if (total >= 95) return 4.0;
        if (total >= 90) return 3.67;
        if (total >= 85) return 3.33;
        if (total >= 80) return 3.0;
        if (total >= 75) return 2.67;
        if (total >= 70) return 2.33;
        if (total >= 65) return 2.0;
        if (total >= 60) return 1.67;
        if (total >= 55) return 1.33;
        if (total >= 50) return 1.0;
        return 0.0;
    }

    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TRANSCRIPT: ").append(student.getLogin()).append(" ===\n");
        for (Map.Entry<Course, Mark> entry : records.entrySet()) {
            sb.append(entry.getValue().toString()).append("\n");
        }
        sb.append(String.format("GPA: %.2f%n", getGpa()));
        return sb.toString();
    }

    public Map<Course, Mark> getRecords() { return records; }
    public Student getStudent()           { return student; }

    @Override
    public String toString() { return generateReport(); }
}
