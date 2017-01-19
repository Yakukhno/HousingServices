<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Housing services</title>
    <meta name="viewport" charset="UTF-8" content="width=device-width, initial-scale=1"/>
    <link href="/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-offset-3 col-lg-6">
                <a><h1 align="center">Housing services</h1></a>
            </div>
            <div class="col-lg-2">
                <c:choose>
                    <c:when test="${sessionScope.user == null}">
                        <form method="get" action="/rest/login">
                            <button class="btn btn-default">Login</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <form method="post" action="/rest/logout">
                            <button class="btn btn-default">Logout</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
