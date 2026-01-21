package com.vocab_view.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordDto {
    private String word;
    private String partOfSpeech;
    private List<String> synonyms;
    private List<String> antonyms;
    private String meaning;
}