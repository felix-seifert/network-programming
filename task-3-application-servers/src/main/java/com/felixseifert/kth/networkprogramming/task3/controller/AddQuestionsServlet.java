package com.felixseifert.kth.networkprogramming.task3.controller;

import com.felixseifert.kth.networkprogramming.task3.model.CorrectAnswer;
import com.felixseifert.kth.networkprogramming.task3.model.Question;
import com.felixseifert.kth.networkprogramming.task3.repository.QuestionRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(value = "/add")
public class AddQuestionsServlet extends HttpServlet {

    private final QuestionRepository questionRepository = QuestionRepository.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        if(Objects.isNull(session.getAttribute("username"))){
            RequestDispatcher dispatcher = request.getRequestDispatcher(ViewPage.LOGIN.relativeUrl);
            dispatcher.forward(request, response);
            return;
        }

        List<Question> questions = questionRepository.findAll();
        request.setAttribute("questions", questions);

        RequestDispatcher dispatcher = request.getRequestDispatcher(ViewPage.ADD_QUESTIONS.fileName);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if("add".equals(request.getParameter("form"))) {
            String question = request.getParameter("question");
            String answerA = request.getParameter("answer-a");
            String answerB = request.getParameter("answer-b");
            String answerC = request.getParameter("answer-c");
            String answerD = request.getParameter("answer-d");

            String[] correctAnswersString = request.getParameterValues("correct-answers");
            Set<CorrectAnswer> correctAnswers = Arrays
                    .stream(Optional.ofNullable(correctAnswersString).orElse(new String[0]))
                    .map(CorrectAnswer::of)
                    .collect(Collectors.toSet());

            if(question != null && answerA != null && answerB != null && answerC != null && answerD != null
                    && !correctAnswers.isEmpty()) {

                Question questionObject = new Question(question, answerA, answerB, answerC, answerD, correctAnswers);
                questionRepository.create(questionObject);
            }

            response.sendRedirect(request.getContextPath() + ViewPage.ADD_QUESTIONS.relativeUrl);
            return;
        }

        int idToDelete = Integer.parseInt(request.getParameter("delete"));
        questionRepository.deleteById(idToDelete);

        response.sendRedirect(request.getContextPath() + ViewPage.ADD_QUESTIONS.relativeUrl);
    }
}
