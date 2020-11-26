package com.felixseifert.kth.networkprogramming.task3.controller;

import com.felixseifert.kth.networkprogramming.task3.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {

    private final UserRepository userRepository = UserRepository.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        if(!Objects.isNull(session.getAttribute("username"))) {
            response.sendRedirect(request.getContextPath() + ViewPage.QUIZ.relativeUrl);
            return;
        }

        response.sendRedirect(ViewPage.LOGIN.fileName);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(userRepository.areCredentialsValid(username, password)){
            HttpSession session= request.getSession();
            session.setAttribute("username", username);
            response.sendRedirect(request.getContextPath() + ViewPage.QUIZ.relativeUrl);
            return;
        }
        response.sendRedirect(ViewPage.LOGIN.fileName);
    }
}
