package com.felixseifert.kth.networkprogramming.task3.repository;

import com.felixseifert.kth.networkprogramming.task3.model.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestionRepository {

    public List<Question> findAllQuestions(Connection connection) throws SQLException {

        PreparedStatement preparedStatement = RepositoryUtils
                .createFindAllPreparedStatement(connection, Question.SQL_TABLE, Question.SQL_COLUMNS);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<Question> questions = new ArrayList<>();

        while(resultSet.next()) {
            questions.add(Question.createOutOfResultSet(resultSet));
        }

        return questions;
    }

    public Optional<Question> findById(Connection connection, Integer id) throws SQLException {

        PreparedStatement preparedStatement = RepositoryUtils
                .createFindByIdPreparedStatement(connection, Question.SQL_TABLE, Question.SQL_COLUMNS, id);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) {
            return Optional.of(Question.createOutOfResultSet(resultSet));
        }
        return Optional.empty();
    }
}
