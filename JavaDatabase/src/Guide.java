public class Guide {
    private final int id;
    private String title;
    private String content;
    private final Account account;
    Guide(int id, String title, String content,Account account) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.account=account;
    }
    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Account getAccount() {
        return account;
    }

    public String toString() {
        return "id: " + id + " title: " + title + " content: " + content;
    }
}
