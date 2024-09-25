/**
 * An exception that represents that something went wrong with creating an account
 */
public class AccountNotFoundException extends Exception {

    public AccountNotFoundException() {
        super("AccountNotFoundException occurred!");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
