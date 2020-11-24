package com.felixseifert.kth.networkprogramming.task3.controller;

import com.felixseifert.kth.networkprogramming.task3.model.Question;
import com.felixseifert.kth.networkprogramming.task3.repository.QuestionRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "QuizServlet", value="/quiz")
public class QuizServlet extends HttpServlet {

    private final QuestionRepository questionRepository=QuestionRepository.getInstance();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Question> questions = questionRepository.findAllQuestions();
        request.setAttribute("data", questions);
        String quizViewPage = "quiz.jsp";
        RequestDispatcher dispatcher =
                request.getRequestDispatcher(quizViewPage);
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> chosenOptions = Arrays.asList(request.getParameterValues("option"));
        Question question = questionRepository.findById(Integer.parseInt(request.getParameter("id"))).get();
        List<String> answer = Arrays.asList(question.getAnswer().split(","));
        Boolean isCorrect = answer.equals(chosenOptions);

        if(isCorrect){
            response.getOutputStream().print("<h1 style=\"color:green;text-align:center;\">Correct</h1>");
        }else {
            response.getOutputStream().print("<h1 style=\"color:red;text-align:center;\">Incorrect</h1>");
        }


    }
}
