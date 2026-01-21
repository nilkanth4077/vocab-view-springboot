package com.vocab_view.service;

import com.vocab_view.components.KeyGenerator;
import com.vocab_view.dto.WordDto;
import com.vocab_view.dto.WordResponse;
import com.vocab_view.entity.Meaning;
import com.vocab_view.entity.Word;
import com.vocab_view.repository.MeaningRepository;
import com.vocab_view.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WordService {

    private final WordRepository wordRepository;
    private final MeaningRepository meaningRepository;
    private final KeyGenerator keyGenerator;
    private final WordCacheService cache;

    public WordService(WordRepository wordRepository, MeaningRepository meaningRepository, KeyGenerator keyGenerator, WordCacheService cache) {
        this.wordRepository = wordRepository;
        this.meaningRepository = meaningRepository;
        this.keyGenerator = keyGenerator;
        this.cache = cache;
    }

    // 🔍 Fetch synonyms & antonyms
    public WordDto getSynonymsAndAntonyms(String wordText) {

        Word word = wordRepository.findByTextIgnoreCase(wordText)
                .orElseThrow(() -> new RuntimeException("Word not found"));

        List<String> synonyms = wordRepository
                .findBySynonymKey(word.getSynonymKey())
                .stream()
                .map(Word::getText)
                .toList();

        // 🔥 FIX: antonyms fetched by SYNONYM KEY
        List<String> antonyms = wordRepository
                .findBySynonymKey(word.getAntonymKey())
                .stream()
                .map(Word::getText)
                .toList();

        String meaning = meaningRepository
                .findBySynonymKey(word.getSynonymKey())
                .map(Meaning::getMeaning)
                .orElse(null);

        WordDto res = new WordDto();
        res.setWord(wordText);
        res.setPartOfSpeech(word.getPartOfSpeech());
        res.setSynonyms(synonyms);
        res.setAntonyms(antonyms);

        return res;
    }

    public List<WordResponse> getAllWords() {

        Map<String, String> meaningMap =
                meaningRepository.findAll()
                        .stream()
                        .collect(Collectors.toMap(
                                Meaning::getSynonymKey,
                                Meaning::getMeaning
                        ));

        return wordRepository.findAll()
                .stream()
                .map(word -> new WordResponse(
                        word.getText(),
                        word.getPartOfSpeech(),
                        word.getSynonymKey(),
                        word.getAntonymKey(),
                        meaningMap.get(word.getSynonymKey())
                ))
                .toList();
    }

    // ✅ Bulk-word API
    public List<Word> addWordsBulk(List<String> words,
                                   String partOfSpeech,
                                   String synonymReference,
                                   String antonymReference,
                                   String meaning) {

        return addWordsInternal(
                words,
                partOfSpeech,
                synonymReference,
                antonymReference,
                meaning
        );
    }

    // 🔥 SINGLE SOURCE OF TRUTH
    private List<Word> addWordsInternal(List<String> words,
                                        String partOfSpeech,
                                        String synonymReference,
                                        String antonymReference,
                                        String meaning) {

        // 🔍 Duplicate check
        for (String text : words) {
            if (wordRepository.findByTextIgnoreCase(text).isPresent()) {
                throw new RuntimeException("Word already exists: " + text);
            }
        }

        // 🔑 Resolve keys ONCE
        KeyPair keys = resolveKeys(synonymReference, antonymReference);

        if (meaning != null && !meaning.isBlank()) {
            meaningRepository.findBySynonymKey(keys.synonymKey())
                    .orElseGet(() -> {
                        Meaning m = new Meaning();
                        m.setSynonymKey(keys.synonymKey());
                        m.setMeaning(meaning.trim());
                        return meaningRepository.save(m);
                    });
        }

        List<Word> entities = words.stream().map(text -> {
            Word word = new Word();
            word.setText(text.toLowerCase());
            word.setPartOfSpeech(partOfSpeech);
            word.setSynonymKey(keys.synonymKey());
            word.setAntonymKey(keys.antonymKey());
            return word;
        }).toList();

        List<Word> saved = wordRepository.saveAll(entities);
        cache.refreshCache();
        return saved;
    }

    // 🧠 EXACT SAME LOGIC YOU WROTE (JUST EXTRACTED)
    private KeyPair resolveKeys(String synonymReference, String antonymReference) {

        // 1️⃣ Synonym reference
        if (synonymReference != null) {

            Word ref = wordRepository.findByTextIgnoreCase(synonymReference)
                    .orElseThrow(() -> new RuntimeException("Synonym reference not found"));

            return new KeyPair(
                    ref.getSynonymKey(),
                    ref.getAntonymKey()
            );
        }

        // 2️⃣ Antonym reference
        if (antonymReference != null) {

            Word ref = wordRepository.findByTextIgnoreCase(antonymReference)
                    .orElseThrow(() -> new RuntimeException("Antonym reference not found"));

            // 🔥 CRITICAL inversion preserved
            return new KeyPair(
                    ref.getAntonymKey(),
                    ref.getSynonymKey()
            );
        }

        // 3️⃣ Brand new word group
        return new KeyPair(
                keyGenerator.generateSynonymKey(),
                keyGenerator.generateAntonymKey()
        );
    }

    // 🔹 Tiny immutable helper
    private record KeyPair(String synonymKey, String antonymKey) {}

}