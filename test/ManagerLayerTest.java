import JavaDataBase.*;
import JavaDataBase.Exceptions.GuideNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The MangerLayerTest class handles test cases for the MangerLayer class
 */
public class ManagerLayerTest {

    private ManagerLayer managerLayer;
    private DataAccessLayer mockDataAccessLayer;
    private Connection mockConnection;

    //TODO testVoteOnGuideNotFound
    //TODO testgetAccount
    //TODO getAllGuides
    //TODO getGuideByTitle
    //TODO getAllGuidesFromUser
    //TODO getGuidesWithDifficulty

    /**
     * called BeforeEach test to initialize mocked MangerLayer
     * for running unit testing
     */
    @BeforeEach
    public void setUp() {
        // Mock the connection and DataAccessLayer
        mockConnection = mock(Connection.class);
        mockDataAccessLayer = mock(DataAccessLayer.class);

        // Initialize ManagerLayer with the mocked DataAccessLayer and connection
        managerLayer = new ManagerLayer(mockDataAccessLayer, mockConnection);
    }

    /**
     * Test getVoteBalance method correctly returns expected balance
     * @throws Exception GuideNotFoundException if the guide does not exist.
     */
    @Test
    public void testGetVoteBalance() throws Exception {
        //mockGuide object
        Guide mockGuide = mock(Guide.class);
        //mockGuide getID then return 1
        when(mockGuide.getId()).thenReturn(1);
        int expected = 5;
        //Mock DataAccessLayer queryVoteBalance method to return expected.
        when(mockDataAccessLayer.queryVoteBalance(mockConnection, 1)).thenReturn(expected);
        int actual = managerLayer.getVoteBalance(mockGuide);
        //AssertEqual expected and actual result is the same.
        assertEquals(expected, actual);
        //Verify that mockDataAccessLayer was called with correct parameters.
        verify(mockDataAccessLayer).queryVoteBalance(mockConnection, 1);
    }
    /**
     * Test that getVoteBalance successfully returns a GuideNotFoundException
     * when called with a invalid Guide.
     * @throws GuideNotFoundException if the database query fails
     */
    @Test
    public void testGetVoteBalanceNotFound() throws Exception {
        //mockGuide object
        Guide mockGuide = mock(Guide.class);
        int guideID = mockGuide.getId();

        //Mock DataAccessLayer queryGuide method to throw a SQLException
        when(mockDataAccessLayer.queryVoteBalance(mockConnection, mockGuide.getId())).thenThrow(new SQLException());
        //Assert: Expected a GuideNotFoundException when called getVoteBalance
        GuideNotFoundException exception = assertThrows(GuideNotFoundException.class, () -> {
            managerLayer.getVoteBalance(mockGuide);
        });
        //AssertTrue if exception message contains the guideID and expected string message
        assertTrue(exception.getMessage().contains("The guide with id"+guideID+"does not exist"));

        //Verify that queryGuide was called with correct parameters
        verify(mockDataAccessLayer).queryVoteBalance(mockConnection, guideID);
    }

    /**
     * Test voteOnGuide method correctly accepts a vote
     * @throws Exception if a vote could not be passed to the DB
     */
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

    /**
     * Test that getGuideByID method successfully returns a guide
     * when a valid guideID is provided and the DB contains the guide.
     * @throws Exception if the database query fails
     */
    @Test
    public void testGuideByIDSuccess() throws Exception {
        //Testing variable
        int guideID = 1;
        //mockGuide object
        Guide mockGuide = mock(Guide.class);
        // Mock the DataAccessLayer queryGuide method to return mockGuide
        when(mockDataAccessLayer.queryGuide(mockConnection, guideID)).thenReturn(mockGuide);
        //Fetch result
        Guide result = managerLayer.getGuideById(guideID);
        //Assert that the objects are equal
        assertEquals(mockGuide, result);
        //Verify that queryGuide was called with the correct parameters
        verify(mockDataAccessLayer).queryGuide(mockConnection, guideID);
    }

    /**
     * Test that getGuideByID successfully returns a GuideNotFoundException
     * when called with a invalid guideID.
     * @throws Exception if the database query fails
     */
    @Test
    public void testGuideByIDNotFound() throws Exception {
        //Testing variable
        int guideID = 1;

        //Mock DataAccessLayer queryGuide method to throw a SQLException
        when(mockDataAccessLayer.queryGuide(mockConnection, guideID)).thenThrow(new SQLException());
        //Assert: Expected a GuideNotFoundException when called getGuideByID
        GuideNotFoundException exception = assertThrows(GuideNotFoundException.class, () -> {
            managerLayer.getGuideById(guideID);
        });
        //AssertTrue if exception message contains the guideID
        assertTrue(exception.getMessage().contains("Guide " + guideID + " does not exist"));

        //Verify that queryGuide was called with correct parameters
        verify(mockDataAccessLayer).queryGuide(mockConnection, guideID);
    }
    //TODO Add javadoc
    @Test
    public void testCreateAccount() throws Exception {
        // Arrange: No exception thrown when inserting an account
        doNothing().when(mockDataAccessLayer).insertAccount(mockConnection, "testUser", "testPassword");

        // Act: Call createAccount method
        managerLayer.createAccount("testUser", "testPassword");

        // Assert: Verify that the DataAccessLayer's insertAccount method was called
        verify(mockDataAccessLayer).insertAccount(mockConnection, "testUser", "testPassword");
    }
    //TODO Add javadoc
    @Test
    public void testCreateGuide() throws Exception {
        Account mockAccount = mock(Account.class);
        doNothing().when(mockDataAccessLayer).insertGuide(mockConnection, "title", "content", mockAccount, 5);
        managerLayer.createGuide("title", "content", mockAccount, 5);
        verify(mockDataAccessLayer).insertGuide(mockConnection, "title", "content", mockAccount, 5);
    }
    //TODO Add javadoc
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
    /**
     * Test that getAllAccounts method successfully returns a list of guides
     * @throws Exception if the database query fails
     */
    @Test
    public void testGetAllAccountSuccess() throws Exception {
        //mockAccounts added to accounts
        ArrayList<Account> accounts = new ArrayList<Account>();
        Account mockAccount = mock(Account.class);
        Account mockAccount2 = mock(Account.class);
        Account mockAccount3 = mock(Account.class);
        accounts.add(mockAccount);
        accounts.add(mockAccount2);
        accounts.add(mockAccount3);

        //mockDataAccessLayer quaryAllAccounts method to return the accounts list
        when(mockDataAccessLayer.queryAllAccounts(mockConnection)).thenReturn(accounts);

        //Store result from method getAllAccounts
        ArrayList<Account> result = managerLayer.getAllAccounts();

        //Assert accounts and result list are equal
        assertEquals(accounts, result);
        //Verify that queryAllAccounts was called.
        verify(mockDataAccessLayer).queryAllAccounts(mockConnection);
    }
    @Test
    public void testGetAccount() throws Exception {
        Account mockAccount = mock(Account.class);
        String username = "TestUser";
        when(mockAccount.getUsername()).thenReturn(username);
        when(mockDataAccessLayer.queryAccount(mockConnection, username)).thenReturn(mockAccount);

        Account result = managerLayer.getAccount(username);
        assertEquals(mockAccount,result);

        verify(mockDataAccessLayer).queryAccount(mockConnection, username);
    }
}