package com.vocab_view.service.impl;
import com.vocab_view.entity.GrammarRule;
import com.vocab_view.repository.GrammarRuleRepository;
import com.vocab_view.service.GrammarRuleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrammarRuleServiceImpl implements GrammarRuleService {

    private final GrammarRuleRepository repository;

    public GrammarRuleServiceImpl(GrammarRuleRepository repository) {
        this.repository = repository;
    }

    @Override
    public GrammarRule create(GrammarRule rule) {
        return repository.save(rule);
    }

    @Override
    public List<GrammarRule> getAll() {
        return repository.findAll();
    }

    @Override
    public GrammarRule getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grammar rule not found"));
    }

    @Override
    public List<GrammarRule> getByCategory(String category) {
        return repository.findByCategoryIgnoreCase(category);
    }
}
