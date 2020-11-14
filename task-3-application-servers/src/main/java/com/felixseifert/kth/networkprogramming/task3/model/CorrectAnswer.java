package com.felixseifert.kth.networkprogramming.task3.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum CorrectAnswer {
    A(1), B(2), C(3), D(4);

    Integer databaseCode;

    private static final Map<Integer, CorrectAnswer> databaseKeyMap = new HashMap<>(values().length, 1);

    static {
        Arrays.stream(values()).forEach(a -> databaseKeyMap.put(a.databaseCode, a));
    }

    CorrectAnswer(Integer databaseCode) {
        this.databaseCode = databaseCode;
    }

    public static CorrectAnswer of(int databaseCode) {
        CorrectAnswer correctAnswer = databaseKeyMap.get(databaseCode);
        if(correctAnswer == null) {
            throw new IllegalArgumentException("Invalid databaseCode for CorrectAnswer: " + databaseCode);
        }
        return correctAnswer;
    }
}
