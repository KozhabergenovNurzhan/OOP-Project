package university.exceptions;

public class FailLimitException extends Exception {
    private static final long serialVersionUID = 1L;

    public FailLimitException() {
        super("Student has exceeded the maximum number of allowed course failures.");
    }
}