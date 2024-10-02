package JavaDataBase.Exceptions;

/**
 * An exception that represents that something went wrong with querying a guide
 */
public class GuideNotFoundException extends Exception {

    /**
     * Constructs a new GuideNotFoundException with a default message.
     */
    public GuideNotFoundException() {
        super("GuideNotFoundException occurred!");
    }

    /**
     * Constructs a new GuideNotFoundException with a specified detail message.
     *
     * @param message The detail message.
     */
    public GuideNotFoundException(String message) {
        super(message);
    }

     /**
     * Constructs a new GuideNotFoundException with a specified detail message
     * and cause.
     *
     * @param message The detail message.
     * @param cause   The cause of the exception.
     */
    public GuideNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
