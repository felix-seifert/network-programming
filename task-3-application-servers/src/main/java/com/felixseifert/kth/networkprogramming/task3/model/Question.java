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
        SQL_COLUMNS.put("ID", "INT PRIMARY KEY ");
        SQL_COLUMNS.put("QUESTION", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("ANSWER", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("OPTION", "VARCHAR(255) NOT NULL");
    }

    Integer id;

    String question;

    String option;

    String answer;

    public Question() {
    }

    public Question(Integer id, String question, String answer, String option) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.option= option;

    }

    public static Question createOutOfResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("ID");
        String question = resultSet.getString("QUESTION");
        String answer = resultSet.getString("ANSWER");
        String option = resultSet.getString("OPTION");
        return new Question(id, question, answer,option);
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

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
