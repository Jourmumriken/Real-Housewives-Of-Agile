/**
 * The Guide class represents a repair guide created by a user.
 * Each guide is associated with an account and contains
 * details such as a title, content, creator, and a difficulty rating.
 */
public class Guide {

    private final int id;
    private String title;
    private String content;
    private final Account account;  // Account associated with the guide (creator)
    private int difficulty;

    /**
     * Constructs a new Guide object.
     *
     * @param id         The unique identifier for the guide.
     * @param title      The title of the guide.
     * @param content    The detailed & formatted content of the guide.
     * @param account    The Account associated with the guide (the
     *                   creator).
     * @param difficulty The difficulty rating of the guide.
     */
    Guide(int id, String title, String content, Account account, int difficulty) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.account = account;
        this.difficulty = difficulty;
    }

    /**
     * Gets the unique identifier for this guide.
     *
     * @return The guide's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the title of this guide.
     *
     * @return The guide's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets a new title for this guide.
     *
     * @param title The new title of the guide.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the content of this guide.
     *
     * @return The guide's formatted content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets a new content for this guide.
     *
     * @param content The new content of the guide.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the Account associated with this guide.
     * This typically represents the creator or poster of the guide.
     *
     * @return The account associated with this guide.
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Gets the difficulty rating of this guide.
     *
     * @return The difficulty rating as an integer.
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Sets a new difficulty rating for this guide.
     *
     * @param difficulty The new difficulty rating.
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Returns a string representation of the guide, including a unique identifier
     * ID, title, content, poster's username, and difficulty rating.
     *
     * @return A string describing the data associated with this guide.
     */
    @Override
    public String toString() {
        return "ID: " + id +
                ", Title: " + title +
                ", Content: " + content +
                ", Poster: " + account.getUsername() +
                ", Difficulty: " + difficulty;
    }
}
