package com.vocab_view.service;

import com.vocab_view.entity.GrammarRule;

import java.util.List;

public interface GrammarRuleService {

    GrammarRule create(GrammarRule rule);

    List<GrammarRule> getAll();

    GrammarRule getById(Long id);

    List<GrammarRule> getByCategory(String category);
}
