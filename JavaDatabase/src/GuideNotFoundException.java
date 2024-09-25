/**
 * An exception that represents that something went wrong with creating an account
 */
public class GuideNotFoundException extends Exception {

    public GuideNotFoundException() {
        super("GuideNotFoundException occurred!");
    }

    public GuideNotFoundException(String message) {
        super(message);
    }

    public GuideNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
