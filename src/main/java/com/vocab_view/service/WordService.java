package com.vocab_view.service;

import com.vocab_view.components.KeyGenerator;
import com.vocab_view.dto.WordResponse;
import com.vocab_view.entity.Word;
import com.vocab_view.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WordService {

    private final WordRepository wordRepository;
    private final KeyGenerator keyGenerator;
    private final WordCacheService cache;

    public WordService(WordRepository wordRepository, KeyGenerator keyGenerator, WordCacheService cache) {
        this.wordRepository = wordRepository;
        this.keyGenerator = keyGenerator;
        this.cache = cache;
    }

    // 🔍 Fetch synonyms & antonyms
    public Map<String, List<String>> getSynonymsAndAntonyms(String wordText) {

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

        return Map.of(
                "synonyms", synonyms,
                "antonyms", antonyms
        );
    }

//    public Word addWord(String text,
//                        String partOfSpeech,
//                        String synonymReference,
//                        String antonymReference) {
//
//        if (wordRepository.findByTextIgnoreCase(text).isPresent()) {
//            throw new RuntimeException("Word already exists");
//        }
//
//        Word word = new Word();
//        word.setText(text.toLowerCase());
//        word.setPartOfSpeech(partOfSpeech);
//
//        // 1️⃣ Synonym reference
//        if (synonymReference != null) {
//
//            Word ref = wordRepository.findByTextIgnoreCase(synonymReference)
//                    .orElseThrow(() -> new RuntimeException("Synonym reference not found"));
//
//            word.setSynonymKey(ref.getSynonymKey());
//            word.setAntonymKey(ref.getAntonymKey());
//        }
//
//        // 2️⃣ Antonym reference (CRITICAL FIX)
//        else if (antonymReference != null) {
//
//            Word ref = wordRepository.findByTextIgnoreCase(antonymReference)
//                    .orElseThrow(() -> new RuntimeException("Antonym reference not found"));
//
//            word.setSynonymKey(ref.getAntonymKey());
//            word.setAntonymKey(ref.getSynonymKey());
//        }
//
//        // 3️⃣ Brand new word
//        else {
//            String synKey = keyGenerator.generateSynonymKey();
//            String antKey = keyGenerator.generateAntonymKey();
//
//            word.setSynonymKey(synKey);
//            word.setAntonymKey(antKey);
//        }
//
//        Word saved = wordRepository.save(word);
//        cache.refreshCache();
//        return saved;
//    }

    public List<WordResponse> getAllWords() {
        return wordRepository.findAll()
                .stream()
                .map(word -> new WordResponse(
                        word.getText(),
                        word.getPartOfSpeech(),
                        word.getSynonymKey(),
                        word.getAntonymKey()
                ))
                .toList();
    }

    public Word addWord(String text,
                        String partOfSpeech,
                        String synonymReference,
                        String antonymReference) {

        List<Word> saved = addWordsInternal(
                List.of(text),
                partOfSpeech,
                synonymReference,
                antonymReference
        );

        return saved.get(0);
    }

    // ✅ Bulk-word API
    public List<Word> addWordsBulk(List<String> words,
                                   String partOfSpeech,
                                   String synonymReference,
                                   String antonymReference) {

        return addWordsInternal(
                words,
                partOfSpeech,
                synonymReference,
                antonymReference
        );
    }

    // 🔥 SINGLE SOURCE OF TRUTH
    private List<Word> addWordsInternal(List<String> words,
                                        String partOfSpeech,
                                        String synonymReference,
                                        String antonymReference) {

        // 🔍 Duplicate check
        for (String text : words) {
            if (wordRepository.findByTextIgnoreCase(text).isPresent()) {
                throw new RuntimeException("Word already exists: " + text);
            }
        }

        // 🔑 Resolve keys ONCE
        KeyPair keys = resolveKeys(synonymReference, antonymReference);

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