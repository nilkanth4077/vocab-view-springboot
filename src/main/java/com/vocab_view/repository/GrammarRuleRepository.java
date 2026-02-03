package com.vocab_view.repository;
import com.vocab_view.entity.GrammarRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrammarRuleRepository extends JpaRepository<GrammarRule, Long> {

    List<GrammarRule> findByCategoryIgnoreCase(String category);

    List<GrammarRule> findByTitleContainingIgnoreCase(String keyword);
}
