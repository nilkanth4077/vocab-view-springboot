package com.vocab_view.dto;

import java.util.List;

public class GrammarRuleRequest {

    public String title;
    public String category;
    public String description;
    public String appliesTo;
    public String correctForm;
    public String wrongForm;
    public List<String> examples;
    public List<String> exceptions;
    public String hint;
}
