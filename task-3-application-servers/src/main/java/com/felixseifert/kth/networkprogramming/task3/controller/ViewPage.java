package com.felixseifert.kth.networkprogramming.task3.controller;

public enum ViewPage {
    LOGIN("login.jsp"), QUIZ("quiz.jsp"), REGISTER("registration.jsp");

    String fileName;

    ViewPage(String fileName) {
        this.fileName = fileName;
    }
}
