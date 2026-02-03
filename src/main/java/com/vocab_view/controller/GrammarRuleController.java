package com.vocab_view.controller;
import com.vocab_view.dto.GrammarRuleRequest;
import com.vocab_view.entity.GrammarRule;
import com.vocab_view.service.GrammarRuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"https://vocab-view.vercel.app/", "http://localhost:5173/"})
@RequestMapping("/api/grammar")
public class GrammarRuleController {

    private final GrammarRuleService service;

    public GrammarRuleController(GrammarRuleService service) {
        this.service = service;
    }

    @PostMapping
    public GrammarRule addRule(@RequestBody GrammarRuleRequest req) {

        GrammarRule rule = new GrammarRule();
        rule.setTitle(req.title);
        rule.setCategory(req.category);
        rule.setDescription(req.description);
        rule.setAppliesTo(req.appliesTo);
        rule.setCorrectForm(req.correctForm);
        rule.setWrongForm(req.wrongForm);
        rule.setExamples(req.examples);
        rule.setExceptions(req.exceptions);
        rule.setHint(req.hint);

        return service.create(rule);
    }

    @GetMapping
    public List<GrammarRule> getAllRules(
            @RequestParam(required = false) String category
    ) {
        if (category != null) {
            return service.getByCategory(category);
        }
        return service.getAll();
    }

    @GetMapping("/{id}")
    public GrammarRule getRule(@PathVariable Long id) {
        return service.getById(id);
    }
}
