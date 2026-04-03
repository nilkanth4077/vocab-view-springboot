package com.vocab_view.service.impl;
import com.vocab_view.dto.VocabWordRequest;
import com.vocab_view.dto.VocabWordResponse;
import com.vocab_view.entity.VocabWord;
import com.vocab_view.repository.VocabWordRepository;
import com.vocab_view.service.VocabWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VocabWordServiceImpl implements VocabWordService {

    private final VocabWordRepository repository;

    @Override
    public VocabWordResponse addWord(VocabWordRequest request) {

        VocabWord word = VocabWord.builder()
                .word(request.getWord().toLowerCase())
                .isFrequent(request.getIsFrequent())
                .partOfSpeech(request.getPartOfSpeech())
                .hindiMeaning(request.getHindiMeaning())
                .englishMeaning(request.getEnglishMeaning())
                .examples(request.getExamples())
                .synonyms(request.getSynonyms())
                .hint(request.getHint())
                .build();

        return mapToResponse(repository.save(word));
    }

    @Override
    public VocabWordResponse getWord(String word) {
        return repository.findByWordIgnoreCase(word)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Word not found"));
    }

    @Override
    public List<VocabWordResponse> getAllWords() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public VocabWordResponse updateWord(String word, VocabWordRequest request) {

        VocabWord existing = repository.findByWordIgnoreCase(word)
                .orElseThrow(() -> new RuntimeException("Word not found"));

        existing.setPartOfSpeech(request.getPartOfSpeech());
        existing.setHindiMeaning(request.getHindiMeaning());
        existing.setEnglishMeaning(request.getEnglishMeaning());
        existing.setExamples(request.getExamples());
        existing.setSynonyms(request.getSynonyms());
        existing.setHint(request.getHint());

        return mapToResponse(repository.save(existing));
    }

    @Override
    public void deleteWord(String word) {
        VocabWord vocabWord = repository.findByWordIgnoreCase(word)
                .orElseThrow(() -> new RuntimeException("Word not found"));
        repository.delete(vocabWord);
    }

    private VocabWordResponse mapToResponse(VocabWord w) {
        return VocabWordResponse.builder()
                .id(w.getId())
                .word(w.getWord())
                .partOfSpeech(w.getPartOfSpeech())
                .hindiMeaning(w.getHindiMeaning())
                .englishMeaning(w.getEnglishMeaning())
                .examples(w.getExamples())
                .synonyms(w.getSynonyms())
                .hint(w.getHint())
                .build();
    }
}
