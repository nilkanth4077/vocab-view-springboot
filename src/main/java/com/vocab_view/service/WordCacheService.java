package com.vocab_view.service;

import com.vocab_view.repository.WordRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordCacheService {

    private final WordRepository repository;
    private volatile List<String> cachedWords = List.of();

    public WordCacheService(WordRepository repository) {
        this.repository = repository;
    }

    // Load at startup
    @PostConstruct
    public void loadCache() {
        refreshCache();
    }

    public void refreshCache() {
        cachedWords = repository.findAllWordsOnly();
    }

    public List<String> suggest(String prefix) {
        if (prefix.length() < 2) return List.of();

        String p = prefix.toLowerCase();

        return cachedWords.stream()
                .filter(w -> w.startsWith(p))
                .limit(10)
                .toList();
    }
}