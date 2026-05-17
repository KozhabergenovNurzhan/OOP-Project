package university.model;

import java.io.Serializable;

/**
 * Represents an academic mark for a student in a specific course.
 * <p>
 * A mark consists of two attestation scores and a final exam score.
 * The total grade is computed as:
 * <pre>
 *   Total = ATT1 * 0.3 + ATT2 * 0.3 + FinalExam * 0.4
 * </pre>
 * Validation ensures ATT1 + ATT2 &le; 60 and FinalExam &le; 40.
 * A student passes if the total is at least 50.
 * </p>
 *
 * @author Kozhabergenov Nurzhan, Yelmuratova Madina, Kainazar Nurdaulet
 * @version 1.0
 * @see Student
 * @see Course
 * @see Transcript
 */
public class Mark implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The student this mark belongs to. */
    private Student student;

    /** The course this mark is for. */
    private Course course;

    /** First attestation score (0–60, combined with ATT2 must not exceed 60). */
    private double attestation1;

    /** Second attestation score (0–60, combined with ATT1 must not exceed 60). */
    private double attestation2;

    /** Final exam score (0–40). */
    private double finalExam;

    /**
     * Constructs a new Mark for the given student and course.
     * All scores are initialized to 0.
     *
     * @param student the student receiving this mark
     * @param course  the course this mark is for
     */
    public Mark(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    /**
     * Computes the total grade using the weighted formula:
     * ATT1 (30%) + ATT2 (30%) + Final (40%).
     *
     * @return the total grade out of 100
     */
    public double computeTotal() {
        return attestation1 * 0.3 + attestation2 * 0.3 + finalExam * 0.4;
    }

    /**
     * Returns the letter grade based on the total score.
     * <ul>
     *   <li>95–100: A</li>
     *   <li>90–94: A-</li>
     *   <li>85–89: B+</li>
     *   <li>80–84: B</li>
     *   <li>75–79: B-</li>
     *   <li>70–74: C+</li>
     *   <li>65–69: C</li>
     *   <li>60–64: C-</li>
     *   <li>55–59: D+</li>
     *   <li>50–54: D</li>
     *   <li>below 50: F</li>
     * </ul>
     *
     * @return the letter grade as a String
     */
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

    /**
     * Determines whether the student passed this course.
     *
     * @return {@code true} if total score is at least 50, {@code false} otherwise
     */
    public boolean isPassed() {
        return computeTotal() >= 50;
    }

    /**
     * Returns the first attestation score.
     *
     * @return attestation 1 score
     */
    public double getAttestation1() { return attestation1; }

    /**
     * Sets the first attestation score.
     * Validates that the score is between 0 and 60, and that
     * the sum of ATT1 and ATT2 does not exceed 60.
     *
     * @param v the attestation 1 score
     * @throws IllegalArgumentException if v is out of range or sum exceeds 60
     */
    public void setAttestation1(double v) {
        if (v < 0 || v > 60)
            throw new IllegalArgumentException("Attestation 1 must be between 0 and 60");
        if (v + this.attestation2 > 60)
            throw new IllegalArgumentException("Sum of ATT1 and ATT2 cannot exceed 60");
        this.attestation1 = v;
    }

    /**
     * Returns the second attestation score.
     *
     * @return attestation 2 score
     */
    public double getAttestation2() { return attestation2; }

    /**
     * Sets the second attestation score.
     * Validates that the score is between 0 and 60, and that
     * the sum of ATT1 and ATT2 does not exceed 60.
     *
     * @param v the attestation 2 score
     * @throws IllegalArgumentException if v is out of range or sum exceeds 60
     */
    public void setAttestation2(double v) {
        if (v < 0 || v > 60)
            throw new IllegalArgumentException("Attestation 2 must be between 0 and 60");
        if (this.attestation1 + v > 60)
            throw new IllegalArgumentException("Sum of ATT1 and ATT2 cannot exceed 60");
        this.attestation2 = v;
    }

    /**
     * Returns the final exam score.
     *
     * @return final exam score
     */
    public double getFinalExam() { return finalExam; }

    /**
     * Sets the final exam score.
     * Must be between 0 and 40.
     *
     * @param v the final exam score
     * @throws IllegalArgumentException if v is not between 0 and 40
     */
    public void setFinalExam(double v) {
        if (v < 0 || v > 40)
            throw new IllegalArgumentException("Final exam must be between 0 and 40");
        this.finalExam = v;
    }

    /**
     * Returns the student this mark belongs to.
     *
     * @return the student
     */
    public Student getStudent() { return student; }

    /**
     * Returns the course this mark is for.
     *
     * @return the course
     */
    public Course getCourse() { return course; }

    /**
     * Returns a formatted string representation of this mark.
     *
     * @return string with course name, scores, total, and letter grade
     */
    @Override
    public String toString() {
        return course.getName() + " | ATT1: " + attestation1 + " ATT2: " + attestation2
                + " Final: " + finalExam + " | Total: " + String.format("%.1f", computeTotal())
                + " (" + getLetterGrade() + ")";
    }
}
