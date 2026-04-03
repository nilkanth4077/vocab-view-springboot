package com.vocab_view.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VocabWordResponse {

    private Long id;
    private String word;
    private Boolean isFrequent;
    private String partOfSpeech;
    private String hindiMeaning;
    private String englishMeaning;
    private List<String> examples;
    private List<String> synonyms;
    private String hint;
}
