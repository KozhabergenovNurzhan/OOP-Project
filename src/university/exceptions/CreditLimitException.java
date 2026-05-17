package university.exceptions;

public class CreditLimitException extends Exception {
    private static final long serialVersionUID = 1L;

    public CreditLimitException(int currentCredits, int newCredits) {
        super("Credit limit exceeded. Current: " + currentCredits + ", Adding: " + newCredits);
    }
}
