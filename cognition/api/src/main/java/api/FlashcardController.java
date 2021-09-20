package api;

import java.util.concurrent.atomic.AtomicLong;

import core.Flashcard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlashcardController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/flashcard")
    public Flashcard flashcard(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Flashcard(counter.incrementAndGet(), String.format(template, name));
    }
}
