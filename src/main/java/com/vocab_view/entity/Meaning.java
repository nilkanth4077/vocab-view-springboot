package com.vocab_view.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meanings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meaning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String synonymKey;

    @Column(nullable = false, length = 2000)
    private String meaning;
}