<%@ page import="com.felixseifert.kth.networkprogramming.task3.model.Question" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Questions</title>
    <style>
        .spacing-div {
            margin: 5px;
        }
    </style>
</head>
<body>
<div style="text-align: center">
    <h1>Add Questions</h1>
    <div>
        <form action="<%=request.getContextPath()%>/add" method="post">
            <input type="hidden" name="form" value="add"/>
            <div class="spacing-div">
                <label>Question
                    <input type="text" name="question"/>
                </label>
            </div>
            <div class="spacing-div">
                <label>Answer A
                    <input type="text" name="answer-a"/><br/>
                    Correct answer: <input type="checkbox" name="correct-answers" value="A"/>
                </label>
            </div>
            <div class="spacing-div">
                <label>Answer B
                    <input type="text" name="answer-b"/><br/>
                    Correct answer: <input type="checkbox" name="correct-answers" value="B"/>
                </label>
            </div>
            <div class="spacing-div">
                <label>Answer C
                    <input type="text" name="answer-c"/><br/>
                    Correct answer: <input type="checkbox" name="correct-answers" value="C"/>
                </label>
            </div>
            <div class="spacing-div">
                <label>Answer D
                    <input type="text" name="answer-d"/><br/>
                    Correct answer: <input type="checkbox" name="correct-answers" value="D"/>
                </label>
            </div>
            <div>
                <input type="submit" value="Add Question"/>
            </div>
        </form>
    </div>
    <h1>Existing Quiz Questions</h1>
    <div>
        <form action="<%=request.getContextPath()%>/add" method="post">
            <input type="hidden" name="form" value="delete"/>
            <%
                ArrayList<Question> questions = new ArrayList<>();
                Object data = request.getAttribute("questions");
                if (data != null) {
                    questions = (ArrayList<Question>) data;
                }

                for (Question question : questions) {
            %>
            <div class="spacing-div">
                <%=question.getQuestion()%>&nbsp;
                <button type="submit" name="delete" value="<%=question.getId()%>">Delete</button>
            </div>
            <%}%>
        </form>
    </div>
</div>
</body>
</html>
