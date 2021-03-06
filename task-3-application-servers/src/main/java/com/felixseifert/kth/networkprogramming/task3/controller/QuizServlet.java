package com.felixseifert.kth.networkprogramming.task3.controller;

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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@WebServlet(name = "QuizServlet", value="/quiz")
public class QuizServlet extends HttpServlet {

    private final QuestionRepository questionRepository = QuestionRepository.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        if(Objects.isNull(session.getAttribute("username"))){
            RequestDispatcher dispatcher =
                    request.getRequestDispatcher(ViewPage.LOGIN.relativeUrl);
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
            return;
        }

        List<Question> questions = questionRepository.findAll();
        request.setAttribute("data", questions);
        RequestDispatcher dispatcher =
                request.getRequestDispatcher(ViewPage.QUIZ.fileName);
        if(dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] chosenAnswersString = request.getParameterValues("answers");
        List<String> chosenAnswers = Arrays.asList(Optional.ofNullable(chosenAnswersString).orElse(new String[0]));
        int id = Integer.parseInt(request.getParameter("id"));

        if(areAnswersCorrect(id, chosenAnswers)){
            response.getOutputStream().print("<h1 style=\"color:green;text-align:center;\">Correct</h1>" +
                    "<button type=\"button\" name=\"Back to Quiz\" onclick=\"history.back()\">back</button>");
            return;
        }
        response.getOutputStream().print("<h1 style=\"color:red;text-align:center;\">Incorrect</h1>" +
                "<button type=\"button\" name=\"Back to Quiz\" onclick=\"history.back()\">back</button>");
    }

    private boolean areAnswersCorrect(int id, List<String> chosenAnswers) {
        Question question = questionRepository.findById(id).orElseThrow(RuntimeException::new);
        return question.getCorrectAnswersString().containsAll(chosenAnswers)
                && (long) question.getCorrectAnswersString().size() == chosenAnswers.size();
    }
}
