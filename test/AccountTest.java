import JavaDataBase.Account;
import junit.framework.TestCase;
import org.junit.Test;
public class AccountTest extends TestCase {
    private Account account = new Account("User", "Test");
    public void testCheckPw() {
        Boolean test = account.checkPw("fail");
        Boolean check = false;
        assertEquals(check, test);

        Boolean correct = account.checkPw("Test");
        Boolean check2 = true;
        assertEquals(check2, correct);
    }

}
