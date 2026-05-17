package university.exceptions;

public class LowHIndexException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LowHIndexException(int hIndex) {
        super("Supervisor assignment rejected. H-Index is too low: " + hIndex);
    }
}