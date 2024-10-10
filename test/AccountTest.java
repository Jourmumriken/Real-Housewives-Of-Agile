import JavaDataBase.Account;
import junit.framework.TestCase;
public class AccountTest extends TestCase {
    private Account account = new Account("User", "Test");
    
    public void testCheckPwCorrectPW() {
        Boolean correct = account.checkPw("Test");
        Boolean correctPW = true;
        assertEquals(correctPW, correct);
    }
    public void testCheckPwIncorrectPW() {
        Boolean test = account.checkPw("fail");
        Boolean incorrectPW = false;
        assertEquals(incorrectPW, test);
    }
    public void testgetUsername() {
        assertEquals("User",account.getUsername());
    }

    
    public void testequalsAccountEqualAccount() {
        assertTrue(account.equals(account));
    }
    
    public void testequalsAccountEqualNull() {
        assertFalse(account.equals(null));
    }
    
    public void testequalsAccountEqualUserName() {
        assertEquals(true, account.getUsername().equals(account.getUsername()));
    }
    
    public void testequalsAccountEqualDifferentAccount() {
        Account account2 = new Account("User2", "Test2");
        assertFalse(account.equals(account2));
    }
}
