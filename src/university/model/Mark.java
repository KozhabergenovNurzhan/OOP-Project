package university.model;

import java.io.Serializable;

public class Mark implements Serializable {
    private static final long serialVersionUID = 1L;

    private Student student;
    private Course course;
    private double attestation1;
    private double attestation2;
    private double finalExam;

    public Mark(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public double computeTotal() {
        // ATT1 (30%) + ATT2 (30%) + Final (40%)
        return attestation1 * 0.3 + attestation2 * 0.3 + finalExam * 0.4;
    }

    public String getLetterGrade() {
        double total = computeTotal();
        if (total >= 95) return "A";
        if (total >= 90) return "A-";
        if (total >= 85) return "B+";
        if (total >= 80) return "B";
        if (total >= 75) return "B-";
        if (total >= 70) return "C+";
        if (total >= 65) return "C";
        if (total >= 60) return "C-";
        if (total >= 55) return "D+";
        if (total >= 50) return "D";
        return "F";
    }

    public boolean isPassed() {
        return computeTotal() >= 50;
    }

    // Getters & Setters
    public double getAttestation1()          { return attestation1; }
    public void setAttestation1(double v) {
        if (v < 0 || v > 60)
            throw new IllegalArgumentException("Attestation 1 must be between 0 and 60");
        if (v + this.attestation2 > 60)
            throw new IllegalArgumentException("Sum of ATT1 and ATT2 cannot exceed 60");
        this.attestation1 = v;
    }
    public double getAttestation2()          { return attestation2; }
    public void setAttestation2(double v) {
        if (v < 0 || v > 60)
            throw new IllegalArgumentException("Attestation 2 must be between 0 and 60");
        if (this.attestation1 + v > 60)
            throw new IllegalArgumentException("Sum of ATT1 and ATT2 cannot exceed 60");
        this.attestation2 = v;
    }
    public double getFinalExam()             { return finalExam; }
    public void setFinalExam(double v) {
        if (v < 0 || v > 40)
            throw new IllegalArgumentException("Final exam must be between 0 and 40");
        this.finalExam = v;
    }
    public Student getStudent()              { return student; }
    public Course getCourse()                { return course; }

    @Override
    public String toString() {
        return course.getName() + " | ATT1: " + attestation1 + " ATT2: " + attestation2
                + " Final: " + finalExam + " | Total: " + String.format("%.1f", computeTotal())
                + " (" + getLetterGrade() + ")";
    }
}
