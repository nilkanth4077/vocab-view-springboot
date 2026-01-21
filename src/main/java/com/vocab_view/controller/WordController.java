package com.vocab_view.controller;

import com.vocab_view.dto.AddWordRequest;
import com.vocab_view.dto.BulkWordRequest;
import com.vocab_view.dto.WordDto;
import com.vocab_view.dto.WordResponse;
import com.vocab_view.entity.Word;
import com.vocab_view.repository.WordRepository;
import com.vocab_view.service.WordCacheService;
import com.vocab_view.service.WordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@CrossOrigin(origins = {"https://vocab-view.vercel.app/", "http://localhost:5173/"})
@RestController
@RequestMapping("/api/words")
public class WordController {

    private final WordService wordService;
    private final WordCacheService cacheService;
    private final WordRepository wordRepository;
    private final Random random = new Random();

    public WordController(WordService wordService, WordCacheService cacheService, WordRepository wordRepository) {
        this.wordService = wordService;
        this.cacheService = cacheService;
        this.wordRepository = wordRepository;
    }

    // Fetch synonyms & antonyms
    @GetMapping("/{word}")
    public ResponseEntity<?> getWordRelations(@PathVariable String word) {
        return ResponseEntity.ok(wordService.getSynonymsAndAntonyms(word));
    }

    @PostMapping
    public ResponseEntity<List<Word>> addWords(
            @RequestBody BulkWordRequest request) {

        if (request.getWords() == null || request.getWords().isEmpty()) {
            throw new RuntimeException("Words list cannot be empty");
        }

        List<Word> saved = wordService.addWordsBulk(
                request.getWords(),
                request.getPartOfSpeech(),
                request.getSynonymReference(),
                request.getAntonymReference(),
                request.getMeaning()
        );

        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<WordResponse>> getAllWords() {
        return ResponseEntity.ok(wordService.getAllWords());
    }

    @GetMapping("/suggest")
    public List<String> suggest(@RequestParam String q) {
        return cacheService.suggest(q);
    }

    @GetMapping("/revise")
    public ResponseEntity<?> getRandomWord() {
        List<Word> allWords = wordRepository.findAll();

        if (allWords.isEmpty()) {
            throw new RuntimeException("No words available");
        }

        // pick random
        Word chosen = allWords.get(random.nextInt(allWords.size()));
        return ResponseEntity.ok(wordService.getSynonymsAndAntonyms(chosen.getText()));
    }
}