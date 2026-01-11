package com.vocab_view.controller;

import com.vocab_view.dto.AddWordRequest;
import com.vocab_view.dto.WordResponse;
import com.vocab_view.service.WordCacheService;
import com.vocab_view.service.WordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"https://vocab-view.vercel.app/", "http://localhost:5173/"})
@RestController
@RequestMapping("/api/words")
public class WordController {

    private final WordService wordService;
    private final WordCacheService cacheService;

    public WordController(WordService wordService, WordCacheService cacheService) {
        this.wordService = wordService;
        this.cacheService = cacheService;
    }

    // Fetch synonyms & antonyms
    @GetMapping("/{word}")
    public ResponseEntity<?> getWordRelations(@PathVariable String word) {
        return ResponseEntity.ok(wordService.getSynonymsAndAntonyms(word));
    }

    @PostMapping
    public ResponseEntity<?> addWord(@RequestBody AddWordRequest request) {
        return ResponseEntity.ok(
                wordService.addWord(
                        request.getText(),
                        request.getPartOfSpeech(),
                        request.getSynonymReference(),
                        request.getAntonymReference()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<WordResponse>> getAllWords() {
        return ResponseEntity.ok(wordService.getAllWords());
    }

    @GetMapping("/suggest")
    public List<String> suggest(@RequestParam String q) {
        return cacheService.suggest(q);
    }

}