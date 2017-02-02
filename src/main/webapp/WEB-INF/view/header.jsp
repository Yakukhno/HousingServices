<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="ua.training.controller.Attributes" %>
<c:set var="language" value="${not empty sessionScope[Attributes.LOCALE] ? sessionScope[Attributes.LOCALE].getDisplayLanguage() : 'en_US'}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="strings" />
<html lang="${language}">
<head>
    <title>Housing services</title>
    <meta name="viewport" charset="UTF-8" content="width=device-width, initial-scale=1"/>
    <link href="/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/"><fmt:message key="header.housing_services"/></a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <c:choose>
                    <c:when test="${sessionScope.user != null and sessionScope.user.role.equals(applicationScope.tenant)}">
                        <li><a href="/rest/user/${sessionScope[Attributes.USER].id}"><fmt:message key="header.profile"/></a></li>
                        <li><a href="/rest/user/application"><fmt:message key="header.applications"/></a></li>
                    </c:when>
                    <c:when test="${sessionScope.user != null and sessionScope.user.role.equals(applicationScope.dispatcher)}">
                        <li><a href="/rest/user/${sessionScope[Attributes.USER].id}"><fmt:message key="header.profile"/></a></li>
                        <li><a href="/rest/application"><fmt:message key="header.applications"/></a></li>
                    </c:when>
                </c:choose>
                <li><a href="/rest/task"><fmt:message key="header.tasks"/></a></li>
            </ul>
            <div class="nav navbar-nav navbar-right">
                <div class="row">
                    <form method="post" action="/rest/locale" style="margin-bottom: 0; display: inline-block; margin-right: 40px">
                        <input type="hidden" name="uri" value="${requestScope['javax.servlet.forward.request_uri']}"/>
                        <select id="locale" name="locale" class="form-control" onchange="this.form.submit()">
                            <option value="en_us" ${language eq "en_us" ? "selected" : ""}>English</option>
                            <option value="ru_ru" ${language eq "ru_ru" ? "selected" : ""}>Русский</option>
                            <option value="uk_ua" ${language eq "uk_ua" ? "selected" : ""}>Українська</option>
                        </select>
                    </form>
                    <c:choose>
                        <c:when test="${empty sessionScope.user}">
                            <a class="btn btn-default navbar-btn" href="/rest/register_user" role="button"><fmt:message key="header.sign_up"/></a>
                            <a class="btn btn-default navbar-btn" href="/rest/login" role="button"><fmt:message key="header.login"/></a>
                        </c:when>
                        <c:otherwise>
                            <form method="post" action="/rest/logout" style="margin-bottom: 0; display: inline-flex;">
                                <button class="btn btn-default navbar-btn"><fmt:message key="header.logout"/></button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</nav>
<div class="container-fluid">
