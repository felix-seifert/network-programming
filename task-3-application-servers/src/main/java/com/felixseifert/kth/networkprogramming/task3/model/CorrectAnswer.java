package com.felixseifert.kth.networkprogramming.task3.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum CorrectAnswer {
    A("A"), B("B"), C("C"), D("D");

    private String value;

    private static final Map<String, CorrectAnswer> valueMap = new HashMap<>(values().length, 1);

    static {
        Arrays.stream(values()).forEach(a -> valueMap.put(a.value.toLowerCase(), a));
    }

    CorrectAnswer(String value) {
        this.value = value;
    }

    public static CorrectAnswer of(String value) {
        CorrectAnswer correctAnswer = valueMap.get(value.toLowerCase());
        if(correctAnswer == null) {
            throw new IllegalArgumentException("Invalid value for CorrectAnswer: " + value);
        }
        return correctAnswer;
    }

    public String getValue() {
        return value;
    }
}
