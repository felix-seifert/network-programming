package com.felixseifert.kth.networkprogramming.task3.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum CorrectAnswer {
    A("A", 1), B("B", 2), C("C", 3), D("D", 4);

    private String value;

    private Integer databaseCode;

    private static final Map<Integer, CorrectAnswer> databaseKeyMap = new HashMap<>(values().length, 1);
    private static final Map<String, CorrectAnswer> valueMap = new HashMap<>(values().length, 1);

    static {
        Arrays.stream(values()).forEach(a -> databaseKeyMap.put(a.databaseCode, a));
        Arrays.stream(values()).forEach(a -> valueMap.put(a.value.toLowerCase(), a));
    }

    CorrectAnswer(String value, Integer databaseCode) {
        this.value = value;
        this.databaseCode = databaseCode;
    }

    public static CorrectAnswer of(int databaseCode) {
        CorrectAnswer correctAnswer = databaseKeyMap.get(databaseCode);
        if(correctAnswer == null) {
            throw new IllegalArgumentException("Invalid databaseCode for CorrectAnswer: " + databaseCode);
        }
        return correctAnswer;
    }

    public static CorrectAnswer of(String value) {
        CorrectAnswer correctAnswer = valueMap.get(value.toLowerCase());
        if(correctAnswer == null) {
            throw new IllegalArgumentException("Invalid value for CorrectAnswer: " + value);
        }
        return correctAnswer;
    }

    public Integer getDatabaseCode() {
        return databaseCode;
    }

    public String getValue() {
        return value;
    }
}
