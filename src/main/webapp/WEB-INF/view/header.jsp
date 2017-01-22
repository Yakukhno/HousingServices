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
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="/">Housing services</a>
            </div>
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav">
                    <c:choose>
                        <c:when test="${sessionScope.user != null and sessionScope.user.role.equals(applicationScope.tenant)}">
                            <li><a href="/rest/tenant/${sessionScope.user.id}">Profile</a></li>
                            <li><a href="/rest/tenant/${sessionScope.user.id}/application">Applications</a></li>
                        </c:when>
                        <c:when test="${sessionScope.user != null and sessionScope.user.role.equals(applicationScope.dispatcher)}">
                            <li><a href="/rest/dispatcher/${sessionScope.user.id}">Profile</a></li>
                            <li><a href="/rest/application">Applications</a></li>
                        </c:when>
                    </c:choose>
                    <li><a href="/rest/task">Scheduled tasks</a></li>
                </ul>
                <div class="nav navbar-nav navbar-right">
                    <c:choose>
                        <c:when test="${sessionScope.user == null}">
                            <div class="btn-group">
                                <button type="button" class="btn btn-default navbar-btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    Sign up <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a href="/rest/register_tenant">as tenant</a></li>
                                    <li><a href="/rest/register_dispatcher">as dispatcher</a></li>
                                </ul>
                            </div>
                            <a class="btn btn-default navbar-btn" href="/rest/login" role="button">Login</a>
                        </c:when>
                        <c:otherwise>
                            <form method="post" action="/rest/logout" style="margin-bottom: 0px">
                                <button class="btn btn-default navbar-btn">Logout</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </nav>
    <div class="container-fluid">
