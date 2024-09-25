/**
 * An exception that represents that something went wrong with creating an account
 */
public class DataBaseConnectionException extends Exception {

    public DataBaseConnectionException() {
        super("DataBaseConnectionException occurred!");
    }

    public DataBaseConnectionException(String message) {
        super(message);
    }

    public DataBaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
