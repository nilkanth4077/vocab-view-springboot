package com.vocab_view.repository;

import com.vocab_view.entity.Meaning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeaningRepository extends JpaRepository<Meaning, Long> {

    Optional<Meaning> findBySynonymKey(String synonymKey);
}