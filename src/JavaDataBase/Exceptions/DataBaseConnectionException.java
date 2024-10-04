package JavaDataBase.Exceptions;

/**
 * An exception that represents that something went wrong with connecting to the database
 */
public class DataBaseConnectionException extends Exception {

     /**
     * Default constructor for DatabaseConnectionException.
     * Initializes the exception with a default error message.
     */
    public DataBaseConnectionException() {
        super("DataBaseConnectionException occurred!");
    }

     /**
     * Constructor for DatabaseConnectionException that allows a custom message.
     *
     * @param message the detail message that provides specific information about the error
     */
    public DataBaseConnectionException(String message) {
        super(message);
    }

    /**
     * Constructor for DatabaseConnectionException that allows a custom message and a cause.
     *
     * @param message the detail message that provides specific information about the error
     * @param cause   the cause of the exception (a Throwable that triggered this exception)
     */
    public DataBaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
