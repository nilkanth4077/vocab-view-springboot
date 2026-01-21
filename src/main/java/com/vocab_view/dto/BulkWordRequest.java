package com.vocab_view.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkWordRequest {

    private List<String> words;
    private String synonymReference;
    private String antonymReference;
    private String partOfSpeech;
    private String meaning;
}