import JavaDataBase.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.SQLException;

public class ManagerLayerTest {

    private ManagerLayer managerLayer;
    private DataAccessLayer mockDataAccessLayer;
    private Connection mockConnection;

    @BeforeEach
    public void setUp() {
        // Mock the connection and DataAccessLayer
        mockConnection = mock(Connection.class);
        mockDataAccessLayer = mock(DataAccessLayer.class);

        // Initialize ManagerLayer with the mocked DataAccessLayer and connection
        managerLayer = new ManagerLayer(mockDataAccessLayer, mockConnection);
    }

    @Test
    public void testCreateAccount() throws Exception {
        // Arrange: No exception thrown when inserting an account
        doNothing().when(mockDataAccessLayer).insertAccount(mockConnection, "testUser", "testPassword");

        // Act: Call createAccount method
        managerLayer.createAccount("testUser", "testPassword");

        // Assert: Verify that the DataAccessLayer's insertAccount method was called
        verify(mockDataAccessLayer).insertAccount(mockConnection, "testUser", "testPassword");
    }
    @Test
    public void testCreateGuide() throws Exception {
        Account mockAccount = mock(Account.class);
        doNothing().when(mockDataAccessLayer).insertGuide(mockConnection, "title", "content", mockAccount, 5);
        managerLayer.createGuide("title", "content", mockAccount, 5);
        verify(mockDataAccessLayer).insertGuide(mockConnection, "title", "content", mockAccount, 5);
    }

    @Test
    public void testCorrectPw() throws Exception {
        // Arrange: Mock an account
        Account mockAccount = mock(Account.class);
        when(mockDataAccessLayer.queryAccount(mockConnection, "testUser")).thenReturn(mockAccount);
        when(mockAccount.checkPw("testPassword")).thenReturn(true);

        // Act: Call correctPw method
        boolean result = managerLayer.correctPw("testUser", "testPassword");

        // Assert: Verify the result is true
        assertTrue(result);
    }

    @Test
    public void testVoteOnGuide() throws Exception {
        // Arrange: Mock account, guide, and voting behavior
        Account mockAccount = mock(Account.class);
        Guide mockGuide = mock(Guide.class);
        when(mockAccount.getUsername()).thenReturn("testUser");
        when(mockGuide.getId()).thenReturn(1);

        // Simulate that inserting a vote will fail with SQLException (indicating vote exists)
        doThrow(new SQLException()).when(mockDataAccessLayer).insertVote(mockConnection, VoteType.UPVOTE, "testUser", 1);

        // Simulate that the user already voted with UPVOTE
        when(mockDataAccessLayer.queryVoteType(mockConnection, "testUser", 1)).thenReturn(VoteType.UPVOTE);

        // Act: Call voteOnGuide method
        managerLayer.voteOnGuide(mockAccount, mockGuide, VoteType.UPVOTE);

        // Assert: Verify that insertVote was called once but failed
        verify(mockDataAccessLayer).insertVote(mockConnection, VoteType.UPVOTE, "testUser", 1);

        // Assert: Since the user already voted UPVOTE, the vote should be deleted
        verify(mockDataAccessLayer).deleteVote(mockConnection, "testUser", 1);
    }
    @Test
    public void testGetVoteBalance() throws Exception {
        Guide mockGuide = mock(Guide.class);
        when(mockGuide.getId()).thenReturn(1);

        int expected = 5;

        when(mockDataAccessLayer.queryVoteBalance(mockConnection, 1)).thenReturn(expected);

        int actual = managerLayer.getVoteBalance(mockGuide);
        assertEquals(expected, actual);
        verify(mockDataAccessLayer).queryVoteBalance(mockConnection, 1);
    }
}