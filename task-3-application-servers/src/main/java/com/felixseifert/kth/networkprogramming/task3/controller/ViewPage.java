package com.felixseifert.kth.networkprogramming.task3.controller;

public enum ViewPage {
    LOGIN("login.jsp", "/login"),
    QUIZ("quiz.jsp", "/quiz"),
    REGISTER("registration.jsp", "/register"),
    ADD_QUESTIONS("add-questions.jsp", "/add");

    String fileName;

    String relativeUrl;

    ViewPage(String fileName, String relativeUrl) {
        this.fileName = fileName;
        this.relativeUrl = relativeUrl;
    }
}
