package com.vocab_view.controller;
import com.vocab_view.dto.VocabWordRequest;
import com.vocab_view.dto.VocabWordResponse;
import com.vocab_view.service.VocabWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"https://vocab-view.vercel.app/", "http://localhost:5173/"})
@RequestMapping("/api/vocab")
@RequiredArgsConstructor
public class VocabWordController {

    private final VocabWordService service;

    @PostMapping
    public VocabWordResponse add(@RequestBody VocabWordRequest request) {
        return service.addWord(request);
    }

    @GetMapping("/{word}")
    public VocabWordResponse get(@PathVariable String word) {
        return service.getWord(word);
    }

    @GetMapping
    public List<VocabWordResponse> getAll() {
        return service.getAllWords();
    }

    @PutMapping("/{word}")
    public VocabWordResponse update(@PathVariable String word,
                                    @RequestBody VocabWordRequest request) {
        return service.updateWord(word, request);
    }

    @DeleteMapping("/{word}")
    public String delete(@PathVariable String word) {
        service.deleteWord(word);
        return "Deleted Successfully";
    }
}
