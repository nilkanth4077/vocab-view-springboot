package com.vocab_view.components;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KeyGenerator {

    public String generateSynonymKey() {
        return "SYN_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public String generateAntonymKey() {
        return "ANT_" + UUID.randomUUID().toString().substring(0, 8);
    }
}