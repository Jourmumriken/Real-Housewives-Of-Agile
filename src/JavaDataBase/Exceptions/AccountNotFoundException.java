package JavaDataBase.Exceptions;

/**
 * An exception that represents that something went wrong with querying an account
 */
public class AccountNotFoundException extends Exception {

     /**
     * Constructs a new AccountNotFoundException with a default message.
     */
    public AccountNotFoundException() {
        super("AccountNotFoundException occurred!");
    }

      /**
     * Constructs a new AccountNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public AccountNotFoundException(String message) {
        super(message);
    }

     /**
     * Constructs a new AccountNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
