package com.vocab_view.service;

import com.vocab_view.dto.VocabWordRequest;
import com.vocab_view.dto.VocabWordResponse;

import java.util.List;

public interface VocabWordService {

    VocabWordResponse addWord(VocabWordRequest request);

    VocabWordResponse getWord(String word);

    List<VocabWordResponse> getAllWords();

    VocabWordResponse updateWord(String word, VocabWordRequest request);

    void deleteWord(String word);
}
