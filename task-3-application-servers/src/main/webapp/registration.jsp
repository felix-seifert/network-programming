<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
<%--    <title>Insert title here</title>--%>
</head>
<body>
<div align="center">
    <h1> Register </h1>
    <form action="<%=request.getContextPath()%>/register" method="post">
        <table style="with: 100%">
            <tr>
                <td>UserName</td>
                <td><input type="text" name="username" /></td>
            </tr>
            <tr>
                <td>Password</td>
                <td><input type="password" name="password" /></td>
            </tr>
            <tr>
                <td>Email</td>
                <td><input type="email" name="email" /></td>
            </tr>

        </table>
        <input type="submit" value="Submit" />
    </form>
</div>
</body>
</html>