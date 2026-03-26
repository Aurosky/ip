package Willa.Exception;

/**
 * Self-defined exception, used to handle business exceptions within the Willa task management program.
 * Inherits from Exception and carries specific error message information.
 */
public class WillaException extends Exception {
    /**
     * Constructs an instance of the WillaException exception.
     * @param message Detailed exception message informing the user of the specific error cause
     */
    public WillaException(String message) {
        super(message);
    }
}
