/**
 * An exception that represents that something went wrong with creating an account
 */
public class AccountCreationException extends Exception {

    public AccountCreationException() {
        super("AccountCreationException occurred!");
    }

    public AccountCreationException(String message) {
        super(message);
    }

    public AccountCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
