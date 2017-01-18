<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>Tenants</h1>
    <c:forEach var="tenant" items="${tenants}">
        <c:out value="${tenant}"/>
    </c:forEach>
</body>
</html>
