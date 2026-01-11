package com.vocab_view.dto;

import lombok.Data;

@Data
public class AddWordRequest {

    private String text;
    private String partOfSpeech;

    private String synonymReference;
    private String antonymReference;

}