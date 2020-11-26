package com.felixseifert.kth.networkprogramming.task3.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Question {

    public static final String SQL_TABLE = "QUESTIONS";

    public static final Map<String, String> SQL_COLUMNS = new LinkedHashMap<>();

    static {
        SQL_COLUMNS.put("ID", "IDENTITY NOT NULL PRIMARY KEY");
        SQL_COLUMNS.put("QUESTION", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("ANSWER_A", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("ANSWER_B", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("ANSWER_C", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("ANSWER_D", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("CORRECT_ANSWERS", "VARCHAR(255) NOT NULL");
    }

    Integer id;

    String question;

    String answerA;

    String answerB;

    String answerC;

    String answerD;

    Set<CorrectAnswer> correctAnswers;

    public Question() {
    }

    public Question(Integer id, String question, String answerA, String answerB, String answerC, String answerD,
                    Set<CorrectAnswer> correctAnswers) {
        this.id = id;
        this.question = question;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correctAnswers = correctAnswers;
    }

    public Question(String question, String answerA, String answerB, String answerC, String answerD,
                    Set<CorrectAnswer> correctAnswers) {
        this.question = question;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correctAnswers = correctAnswers;
    }

    public Question(String question, String answerA, String answerB, String answerC, String answerD,
                    CorrectAnswer... correctAnswers) {
        this.id = id;
        this.question = question;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correctAnswers = new HashSet<>(Arrays.asList(correctAnswers));
    }

    public static Question createOutOfResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("ID");
        String question = resultSet.getString("QUESTION");
        String answerA = resultSet.getString("ANSWER_A");
        String answerB = resultSet.getString("ANSWER_B");
        String answerC = resultSet.getString("ANSWER_C");
        String answerD = resultSet.getString("ANSWER_D");
        Set<CorrectAnswer> correctAnswers = Arrays.stream(resultSet.getString("CORRECT_ANSWERS")
                .split("<>")).map(CorrectAnswer::of).collect(Collectors.toSet());
        return new Question(id, question, answerA, answerB, answerC, answerD, correctAnswers);
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

    public Set<String> getAnswers() {
        Set<String> answers = new HashSet<>();
        answers.add(answerA);
        answers.add(answerB);
        answers.add(answerC);
        answers.add(answerD);
        return answers;
    }

    public Set<CorrectAnswer> getCorrectAnswers() {
        return correctAnswers;
    }

    public Set<String> getCorrectAnswersString() {
        Set<String> answerSet = new HashSet<>();
        if(correctAnswers.contains(CorrectAnswer.A)) answerSet.add(answerA);
        if(correctAnswers.contains(CorrectAnswer.B)) answerSet.add(answerB);
        if(correctAnswers.contains(CorrectAnswer.C)) answerSet.add(answerC);
        if(correctAnswers.contains(CorrectAnswer.D)) answerSet.add(answerD);
        return answerSet;
    }

    public void setCorrectAnswers(Set<CorrectAnswer> correctAnswers) {
        this.correctAnswers = correctAnswers;
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
}
