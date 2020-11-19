package com.felixseifert.kth.networkprogramming.task3.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Question {

    public static final String SQL_TABLE = "QUESTIONS";

    public static final Map<String, String> SQL_COLUMNS = new LinkedHashMap<>();

    static {
        SQL_COLUMNS.put("ID", "INT PRIMARY KEY AUTO_INCREMENT");
        SQL_COLUMNS.put("QUESTION", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("ANSWER_A", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("ANSWER_B", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("ANSWER_C", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("ANSWER_D", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("CORRECT_ANSWER", "INT NOT NULL");
    }

    Integer id;

    String question;

    String answerA;

    String answerB;

    String answerC;

    String answerD;

    CorrectAnswer correctAnswer;

    public Question() {
    }

    public Question(String question, String answerA, String answerB, String answerC, String answerD,
                    CorrectAnswer correctAnswer) {
        this.question = question;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correctAnswer = correctAnswer;
    }

    public Question(Integer id, String question, String answerA, String answerB, String answerC, String answerD,
                    CorrectAnswer correctAnswer) {
        this.id = id;
        this.question = question;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correctAnswer = correctAnswer;
    }

    public static Question createOutOfResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("ID");
        String question = resultSet.getString("QUESTION");
        String answerA = resultSet.getString("ANSWER_A");
        String answerB = resultSet.getString("ANSWER_B");
        String answerC = resultSet.getString("ANSWER_C");
        String answerD = resultSet.getString("ANSWER_D");
        CorrectAnswer correctAnswer = CorrectAnswer.of(resultSet.getInt("CORRECT_ANSWER"));
        return new Question(id, question, answerA, answerB, answerC, answerD, correctAnswer);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public CorrectAnswer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(CorrectAnswer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return Objects.equals(id, question1.id);
    }

    @Override
    public int hashCode() {
        return 11;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answerA='" + answerA + '\'' +
                ", answerB='" + answerB + '\'' +
                ", answerC='" + answerC + '\'' +
                ", answerD='" + answerD + '\'' +
                ", correctAnswer=" + correctAnswer +
                '}';
    }
}
