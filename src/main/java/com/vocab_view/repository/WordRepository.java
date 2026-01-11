package com.vocab_view.repository;

import com.vocab_view.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByTextIgnoreCase(String text);

    List<Word> findBySynonymKey(String synonymKey);

    List<Word> findByAntonymKey(String antonymKey);

    boolean existsBySynonymKey(String synonymKey);

    boolean existsByAntonymKey(String antonymKey);

    List<Word> findAll();

    @Query("SELECT w.text FROM Word w")
    List<String> findAllWordsOnly();
}