package com.vocab_view.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordResponse {

    private String word;
    private String partOfSpeech;
    private String synonymKey;
    private String antonymKey;

}