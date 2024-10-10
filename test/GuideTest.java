import JavaDataBase.Account;
import JavaDataBase.Guide;
import junit.framework.TestCase;
public class GuideTest extends TestCase {
    private int id = 1;
    private String title = "title";
    private String content = "content";
    private Account account = new Account("User", "Test");
    private int difficulty = 1;
    public Guide guide = new Guide(id, title, content, account, difficulty);
    public void testgetID() {
        assertEquals(id, guide.getId());
    }
    public void testgetTitle() {
        assertEquals(title, guide.getTitle());
    }
    public void testsetTitle() {
        String testStr = "newString";
        guide.setTitle(testStr);
        assertEquals(testStr, guide.getTitle());
    }
    public void testgetContent() {
        assertEquals(content, guide.getContent());
    }
    public void testsetContent() {
        String test = "newContent";
        guide.setContent(test);
        assertEquals(test, guide.getContent());
    }
    public void testgetAccount() {
        assertEquals(account, guide.getAccount());
    }
    public void testgetDifficulty() {
        assertEquals(difficulty, guide.getDifficulty());
    }
    public void testsetDifficulty() {
        guide.setDifficulty(5);
        int changedDifficulty= 5;
        assertEquals(changedDifficulty, guide.getDifficulty());
    }
    public void testtoString() {
        String testStr = guide.toString();
        String expected = "ID: 1, Title: title, Content: content, Poster: User, Difficulty 1";

    }
}
