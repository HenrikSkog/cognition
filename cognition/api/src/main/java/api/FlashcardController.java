package api;

import java.util.concurrent.atomic.AtomicLong;

import core.Flashcard;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlashcardController {
    private final AtomicLong counter = new AtomicLong();

    /**
     * @param front  is the parameter of the front of the Flashcard
     * @param answer is the parameter of the back of the Flashcard
     * @return a Flashcard object in JSON format.
     */
    @GetMapping("/flashcard")
    public Flashcard flashcard(
            @RequestParam(value = "front", defaultValue = "No front specified.") String front,
            @RequestParam(value = "answer", defaultValue = "No answer specified.") String answer
    ) {
        return new Flashcard(
                String.valueOf(counter.incrementAndGet()),
                "Front: " + front,
                "Answer: " + answer
        );
    }
}
