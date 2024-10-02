package JavaDataBase.Exceptions;

/**
 * An exception that represents that something went wrong with creating an account
 */
public class AccountCreationException extends Exception {

    /**
     * Constructs a new AccountCreationException with a default message.
     */
    public AccountCreationException() {
        super("AccountCreationException occurred!");
    }

    /**
     * Constructs a new AccountCreationException with the specified detail message.
     *
     * @param message the detail message
     */
    public AccountCreationException(String message) {
        super(message);
    }

    /**
     * Constructs a new AccountCreationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public AccountCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
