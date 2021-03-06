package com.felixseifert.kth.networkprogramming.task3.databaseconnection;

import com.felixseifert.kth.networkprogramming.task3.model.CorrectAnswer;
import com.felixseifert.kth.networkprogramming.task3.model.Question;
import com.felixseifert.kth.networkprogramming.task3.model.User;
import com.felixseifert.kth.networkprogramming.task3.repository.QuestionRepository;
import com.felixseifert.kth.networkprogramming.task3.repository.UserRepository;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

@WebListener
public class DatabaseSetup implements ServletContextListener {

    private final UserRepository userRepository = UserRepository.getInstance();

    private final QuestionRepository questionRepository = QuestionRepository.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        this.createTable(Question.SQL_TABLE, Question.SQL_COLUMNS);
        this.createTable(User.SQL_TABLE, User.SQL_COLUMNS);

        User user = new User(null, "user", "password");
        userRepository.create(user);

        Question question = new Question("How many feet does a centipede have?",
                "100", "102", "120", "122",
                CorrectAnswer.A, CorrectAnswer.B, CorrectAnswer.C, CorrectAnswer.D);
        questionRepository.create(question);

        question = new Question("How many chambers does the heart have?",
                "1", "2", "4", "6", CorrectAnswer.C);
        questionRepository.create(question);

        question = new Question("Which of the following bases is not found in DNA?",
                "Adenine", "Cytosine", "Uracil", "Guanine", CorrectAnswer.C);
        questionRepository.create(question);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        this.destroyTable(Question.SQL_TABLE);
        this.destroyTable(User.SQL_TABLE);
    }

    private void createTable(String sqlTable, Map<String, String> sqlColumns){
        StringBuilder tableCreateQuery = new StringBuilder();
        tableCreateQuery.append("CREATE TABLE ");
        tableCreateQuery.append(sqlTable);
        tableCreateQuery.append("(");
        tableCreateQuery.append(sqlColumns.entrySet().stream()
                .map(c -> c.getKey() + " " + c.getValue())
                .collect(Collectors.joining(", ")));
        tableCreateQuery.append(");");

        try(Connection connection = DatabaseUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(tableCreateQuery.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }

    private void destroyTable(String tableName){
        StringBuilder tableDropQuery = new StringBuilder();
        tableDropQuery.append("DROP TABLE ");
        tableDropQuery.append(tableName);

        try(Connection connection = DatabaseUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(tableDropQuery.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }
}
