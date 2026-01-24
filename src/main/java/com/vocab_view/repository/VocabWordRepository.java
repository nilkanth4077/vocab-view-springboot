package com.vocab_view.repository;

import com.vocab_view.entity.VocabWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VocabWordRepository extends JpaRepository<VocabWord, Long> {

    Optional<VocabWord> findByWordIgnoreCase(String word);
}