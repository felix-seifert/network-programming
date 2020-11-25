package com.felixseifert.kth.networkprogramming.task3.repository;

import com.felixseifert.kth.networkprogramming.task3.databaseconnection.DatabaseUtils;
import com.felixseifert.kth.networkprogramming.task3.model.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestionRepository {

    private static QuestionRepository questionRepositorySingleton;

    private QuestionRepository() {}

    public static QuestionRepository getInstance() {
        if(questionRepositorySingleton == null) {
            synchronized (QuestionRepository.class) {
                if(questionRepositorySingleton == null) {
                    questionRepositorySingleton = new QuestionRepository();
                }
            }
        }
        return questionRepositorySingleton;
    }

    public List<Question> findAllQuestions() {

        List<Question> questions = new ArrayList<>();

        try (Connection connection = DatabaseUtils.getConnection()) {

            PreparedStatement preparedStatement = RepositoryUtils.createFindAllPreparedStatement(
                    connection, Question.SQL_TABLE, Question.SQL_COLUMNS);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                questions.add(Question.createOutOfResultSet(resultSet));
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }

        return questions;
    }

    public Optional<Question> findById(int id) {

        try(Connection connection = DatabaseUtils.getConnection()) {

            PreparedStatement preparedStatement = RepositoryUtils.createFindByIdPreparedStatement(
                    connection, Question.SQL_TABLE, Question.SQL_COLUMNS, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(Question.createOutOfResultSet(resultSet));
            }
        }
        catch(SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
        return Optional.empty();
    }

    public Question create(Question question) {

        try(Connection connection = DatabaseUtils.getConnection()) {

            PreparedStatement preparedStatement = RepositoryUtils.createCreatePreparedStatement(
                    connection, Question.SQL_TABLE, Question.SQL_COLUMNS, question);
            preparedStatement.executeUpdate();

            return question;
        }
        catch(SQLException e) {
            DatabaseUtils.printSQLException(e);
        }

        return null;
    }

    public Question update(Question question) {

        try(Connection connection = DatabaseUtils.getConnection()) {

            PreparedStatement preparedStatement = RepositoryUtils.createUpdatePreparedStatement(
                    connection, Question.SQL_TABLE, Question.SQL_COLUMNS, question, question.getId());

            preparedStatement.executeUpdate();

            return question;
        }
        catch(SQLException e) {
            DatabaseUtils.printSQLException(e);
        }

        return null;
    }

    public void delete(Question question) {
        deleteById(question.getId());
    }

    public void deleteById(int id) {

        try(Connection connection = DatabaseUtils.getConnection()) {

            PreparedStatement preparedStatement = RepositoryUtils.createDeletePreparedStatement(
                    connection, Question.SQL_TABLE, id);

            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }
}
