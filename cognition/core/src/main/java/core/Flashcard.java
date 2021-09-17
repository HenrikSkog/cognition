package core;

public class Flashcard {
    private final long id;
    private final String content;

    public Flashcard(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
