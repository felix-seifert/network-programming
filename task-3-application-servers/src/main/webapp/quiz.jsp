<%@page import="java.util.ArrayList" %>
<%@ page import="com.felixseifert.kth.networkprogramming.task3.model.Question" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Student List</title>
</head>
<body>
<h1>Quiz</h1>
<table border="1" width="500" align="center">
    <tr bgcolor="00FF7F">
        <th><b>Question Id</b></th>
        <th><b>Question</b></th>
        <th><b>Options</b></th>
    </tr>
    <%
        ArrayList<Question> std =
                (ArrayList<Question>) request.getAttribute("data");
        for (Question s : std) {
    %>
    <tr>
        <td><%=s.getId()%>

        </td>
        <td><%=s.getQuestion()%>
        </td>
        <td>
            <form action="<%=request.getContextPath()%>/quiz" method="post">
                <input type="hidden" name="id" value="<%=s.getId()%>"/>
                <%
                    String[] answers = s.getOption().split(",");
                    for (String s1 : answers) {
                %>
                <input type="checkbox" name="option" value="<%=s1%>"><%=s1%><BR>
                <%}%>
                <input type="submit" value="Submit">
            </form>


        </td>
    </tr>
    <%}%>
</table>
<hr/>
</body>
</html>