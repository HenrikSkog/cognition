package core;

public class Flashcard {
    private long id;
    private String front;
    private String answer;

    /**
     * Initializes a Flashcard object with the provided parameters.
     *
     * @param id     is the identifier for the Flashcard.
     * @param front  is the front statement or question on the Flashcard.
     * @param answer is the flipped side of the Flashcard, and the answer to the front.
     */
    public Flashcard(long id, String front, String answer) {
        this.id = id;

        if (isValidFront(front)) {
            this.front = front;
        }

        this.answer = answer;
    }

    /**
     * Initializes a Flashcard object with no values.
     * This is used when deserializing the object.
     */
    public Flashcard() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private boolean isValidFront(String front) {
        if (front == null) {
            throw new NullPointerException("Front cannot be null.");
        }

        return true;
    }

    @Override
    public String toString() {
        return "Flashcard{" +
                "id=" + id +
                ", front='" + front + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
