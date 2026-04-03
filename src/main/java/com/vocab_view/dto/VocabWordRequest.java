package com.vocab_view.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VocabWordRequest {

    private String word;
    private Boolean isFrequent;
    private String partOfSpeech;
    private String hindiMeaning;
    private String englishMeaning;
    private List<String> examples;
    private List<String> synonyms;
    private String hint;
}
