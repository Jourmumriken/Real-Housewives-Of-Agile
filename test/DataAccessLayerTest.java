import JavaDataBase.DataAccessLayer;
import JavaDataBase.VoteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DataAccessLayerTest {

    @Mock
    private Connection mockCon;
    @Mock
    private Statement mockStmt;
    private DataAccessLayer dataAccessLayer;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        when(mockCon.createStatement()).thenReturn(mockStmt);  // Return mock Statement
        when(mockStmt.executeUpdate(anyString())).thenReturn(1);  // Mock executeUpdate behavior
        dataAccessLayer = new DataAccessLayer(mockCon);
    }
    @Test
    public void testQueryVoteBalance() throws SQLException {
        int guideId = 1;
        PreparedStatement mockPreStmt = mock(PreparedStatement.class);
        ResultSet mockRS = mock(ResultSet.class);
        // Mock the PreparedStatement and ResultSet behavior
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPreStmt);
        when(mockPreStmt.executeQuery()).thenReturn(mockRS);

        // Mock the ResultSet to simulate upvotes and downvotes
        when(mockRS.next()).thenReturn(true, true, true, false); // Simulate 3 votes
        when(mockRS.getString(1))
                .thenReturn("upvote", "downvote", "upvote"); // 2 upvotes and 1 downvote

        // Call the method under test
        int voteBalance = dataAccessLayer.queryVoteBalance(mockCon, guideId);

        // Verify the PreparedStatement and ResultSet interactions
        verify(mockCon, times(1)).prepareStatement(anyString());
        verify(mockPreStmt, times(1)).setInt(1, guideId);
        verify(mockRS, times(3)).getString(1); // Should check for vote type 3 times

        // Assert the expected balance
        assertEquals(1, voteBalance); // 2 upvotes - 1 downvote = 1
    }
    @Test
    public void testQueryVoteTypeUPVOTE() throws SQLException {
        //Testing variables
        int guideID = 1;
        String userName = "Username";
        String voteType = "upvote";

        PreparedStatement mockPreStmt = mock(PreparedStatement.class);
        ResultSet mockRS = mock(ResultSet.class);

        when(mockPreStmt.executeQuery()).thenReturn(mockRS);
        when(mockRS.next()).thenReturn(true);
        when(mockRS.getString("vote_type")).thenReturn(voteType);

        when(mockCon.prepareStatement(anyString())).thenReturn(mockPreStmt);

        VoteType result = dataAccessLayer.queryVoteType(mockCon, userName, guideID);

        assertEquals(VoteType.UPVOTE, result);
        // Verify that prepared statement and result set methods were called
        verify(mockPreStmt).setString(1, userName);
        verify(mockPreStmt).setInt(2, guideID);
        verify(mockPreStmt).executeQuery();
        verify(mockRS).getString("vote_type");
    }
    @Test
    public void testQueryVoteTypeDOWNVOTE() throws SQLException {
        //Testing variables
        int guideID = 1;
        String userName = "Username";
        String voteType = "downvote";

        PreparedStatement mockPreStmt = mock(PreparedStatement.class);
        ResultSet mockRS = mock(ResultSet.class);

        when(mockPreStmt.executeQuery()).thenReturn(mockRS);
        when(mockRS.next()).thenReturn(true);
        when(mockRS.getString("vote_type")).thenReturn(voteType);

        when(mockCon.prepareStatement(anyString())).thenReturn(mockPreStmt);

        VoteType result = dataAccessLayer.queryVoteType(mockCon, userName, guideID);

        assertEquals(VoteType.DOWNVOTE, result);
        // Verify that prepared statement and result set methods were called
        verify(mockPreStmt).setString(1, userName);
        verify(mockPreStmt).setInt(2, guideID);
        verify(mockPreStmt).executeQuery();
        verify(mockRS).getString("vote_type");
    }
    @Test
    public void testQueryVoteTypeNoResult() throws SQLException {
        int guideID = 1;
        String userName = "Username";

        PreparedStatement mockPreStmt = mock(PreparedStatement.class);
        ResultSet mockRS = mock(ResultSet.class);

        when(mockPreStmt.executeQuery()).thenReturn(mockRS);
        when(mockRS.next()).thenReturn(false);  // Simulate no result found

        when(mockCon.prepareStatement(anyString())).thenReturn(mockPreStmt);

        // Expect an exception since no result is found
        assertThrows(SQLException.class, () -> dataAccessLayer.queryVoteType(mockCon, userName, guideID));

        // Verify the correct interactions
        verify(mockPreStmt).setString(1, userName);
        verify(mockPreStmt).setInt(2, guideID);
        verify(mockPreStmt).executeQuery();
        verify(mockRS, never()).getString("vote_type");  // Should never get the string since no result
    }
}
