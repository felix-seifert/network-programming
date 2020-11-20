package com.felixseifert.kth.networkprogramming.task3.controller;

import com.felixseifert.kth.networkprogramming.task3.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {

    private final UserRepository userRepository = new UserRepository();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            if(userRepository.validateCredentials(username, password, email)){
                HttpSession session= request.getSession();
                session.setAttribute("username", username);
                response.sendRedirect("quiz.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    private void listUser(HttpServletRequest request, HttpServletResponse response)
//            throws SQLException, IOException, ServletException {
//        LoginDao loginDao = new LoginDao();
//        List<User> listUser = loginDao.getAllUser();
//        request.setAttribute("listUser", listUser);
//        RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
//        dispatcher.forward(request, response);
//    }

//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String action = request.getServletPath();
//
//        try {
//            listUser(request, response);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

}
