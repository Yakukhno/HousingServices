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
                <h1 align="center">Housing services</h1>
            </div>
            <div class="col-lg-3" style="padding: 20px 30px 10px 0px; text-align: right">
                <c:choose>
                    <c:when test="${sessionScope.user == null}">
                        <div class="btn-group">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                Sign up <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a href="/rest/register_tenant">as tenant</a></li>
                                <li><a href="/rest/register_dispatcher">as dispatcher</a></li>
                            </ul>
                        </div>
                        <a class="btn btn-default" href="/rest/login" role="button">Login</a>
                    </c:when>
                    <c:otherwise>
                        <form method="post" action="/rest/logout">
                            <button class="btn btn-default">Logout</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
