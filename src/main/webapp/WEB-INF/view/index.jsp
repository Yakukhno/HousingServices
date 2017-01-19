<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Housing services</title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link href="/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<form method="get" action="/rest/login">
    <input type="text" name="email"/><br/>
    <input type="password" name="password"/><br/>
    <input type="submit">
</form>
<body>
    <h1 align="center">Housing services</h1>
    <h2 align="center">Planned tasks</h2>
    <table class="table table-striped" align="center">
        <thead>
            <tr>
                <th>№</th>
                <th>Type of task</th>
                <th>Scale of problem</th>
                <th>Planned date</th>
                <th>Brigade of workers</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="task" items="${tasks}">
                <tr>
                    <td>${task.id}</td>
                    <td>${task.application.typeOfWork.description}</td>
                    <td>${task.application.scaleOfProblem}</td>
                    <td>${task.application.desiredTime}</td>
                    <td>${task.brigade.id}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
</body>
</html>
