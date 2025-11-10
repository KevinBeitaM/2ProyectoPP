package cr.ac.una.prologapi.exception;

public class PrologException extends RuntimeException {
    public PrologException(String message) {
        super(message);
    }

    public PrologException(String message, Throwable cause) {
        super(message, cause);
    }
}