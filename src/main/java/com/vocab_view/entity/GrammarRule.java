package com.vocab_view.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "grammar_rules")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GrammarRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String category;

    @Column(length = 3000)
    private String description;

    private String appliesTo;
    private String correctForm;
    private String wrongForm;

    @ElementCollection
    @CollectionTable(name = "grammar_examples", joinColumns = @JoinColumn(name = "rule_id"))
    @Column(length = 500)
    private List<String> examples;

    @ElementCollection
    @CollectionTable(name = "grammar_exceptions", joinColumns = @JoinColumn(name = "rule_id"))
    @Column(length = 500)
    private List<String> exceptions;

    private String hint;

}
