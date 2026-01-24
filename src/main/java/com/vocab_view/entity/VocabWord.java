package com.vocab_view.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "vocab_words")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String word;

    private String partOfSpeech;

    @Column(length = 1000)
    private String hindiMeaning;

    @Column(length = 1000)
    private String englishMeaning;

    @ElementCollection
    @CollectionTable(name = "vocab_examples", joinColumns = @JoinColumn(name = "vocab_id"))
    @Column(name = "example", length = 1000)
    private List<String> examples;

    @ElementCollection
    @CollectionTable(name = "vocab_synonyms", joinColumns = @JoinColumn(name = "vocab_id"))
    @Column(name = "synonym")
    private List<String> synonyms;

    private String hint;

    private String extraInfo;
}