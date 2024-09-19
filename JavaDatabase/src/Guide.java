public class Guide {
    private int id;
    private String title;
    private String content;

    Guide(int id, String title, String content){
        this.id=id;
        this.title=title;
        this.content=content;
    }
    Guide(){

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

    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public String toString() {
        return "id: "+id+" title: "+title+" content: "+content;
    }
}
