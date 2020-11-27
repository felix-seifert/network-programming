<%@page import="java.util.ArrayList" %>
<%@ page import="com.felixseifert.kth.networkprogramming.task3.model.Question" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Questions</title>
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
        ArrayList<Question> questions = new ArrayList<>();
        Object data = request.getAttribute("data");
        if(data != null) {
            questions = (ArrayList<Question>) data;
        }

        for (Question question : questions) {
    %>
    <tr>
        <td><%=question.getId()%></td>
        <td><%=question.getQuestion()%></td>
        <td>
            <form action="<%=request.getContextPath()%>/quiz" method="post">
                <input type="hidden" name="id" value="<%=question.getId()%>" />
                <%for (String answer : question.getAnswers()) {%>
                <input type="checkbox" name="answers" value="<%=answer%>" /><%=answer%><br />
                <%}%>
                <input type="submit" value="Submit" />
            </form>
        </td>
    </tr>
    <%}%>
</table>
<hr/>
</body>
</html>