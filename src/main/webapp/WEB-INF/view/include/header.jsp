<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="custom" uri="/WEB-INF/custom.tld"%>
<%@ page import="ua.training.controller.util.AttributeConstants" %>
<c:set var="language"
       value="${not empty sessionScope[AttributeConstants.LOCALE]
                            ? sessionScope[AttributeConstants.LOCALE].getDisplayLanguage()
                            : 'en_US'}"
       scope="session" />
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
            <a class="navbar-brand" href="/web/home">
                <fmt:message key="header.housing_services"/>
            </a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <sec:authorize access="isAuthenticated()">
                    <sec:authentication property="principal.user.id" var="userId"/>
                    <li>
                        <a href="/web/user/${userId}">
                            <fmt:message key="header.profile"/>
                        </a>
                    </li>
                    <sec:authorize access="hasRole('ROLE_TENANT')">
                        <li>
                            <a href="/web/user/application">
                                <fmt:message key="header.applications"/>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ROLE_DISPATCHER')">
                        <li>
                            <a href="/web/application">
                                <fmt:message key="header.applications"/>
                            </a>
                        </li>
                        <li>
                            <a href="/web/worker">
                                <fmt:message key="workers"/>
                            </a>
                        </li>
                    </sec:authorize>
                </sec:authorize>
                <li>
                    <a href="/web/task">
                        <fmt:message key="header.tasks"/>
                    </a>
                </li>
            </ul>
            <div class="nav navbar-nav navbar-right">
                <div class="row">
                    <form method="post" action="/web/locale" style="margin-bottom: 0; display: inline-block; margin-right: 40px">
                        <input type="hidden"
                               name="uri"
                               value="${requestScope['javax.servlet.forward.request_uri']}"/>
                        <sec:csrfInput/>
                        <select id="locale" name="locale" class="form-control" onchange="this.form.submit()">
                            <option value="en_us" ${language eq "en_us" ? "selected" : ""}>
                                English
                            </option>
                            <option value="ru_ru" ${language eq "ru_ru" ? "selected" : ""}>
                                Русский
                            </option>
                            <option value="uk_ua" ${language eq "uk_ua" ? "selected" : ""}>
                                Українська
                            </option>
                        </select>
                    </form>
                    <sec:authorize access="isAnonymous()">
                        <a class="btn btn-default navbar-btn" href="/web/new_user" role="button">
                            <fmt:message key="header.sign_up"/>
                        </a>
                        <a class="btn btn-default navbar-btn" href="/web/login" role="button">
                            <fmt:message key="header.login"/>
                        </a>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <form method="post" action="/web/logout" style="margin-bottom: 0; display: inline-flex;">
                            <sec:csrfInput/>
                            <button class="btn btn-default navbar-btn">
                                <fmt:message key="header.logout"/>
                            </button>
                        </form>
                    </sec:authorize>
                </div>
            </div>
        </div>
    </div>
</nav>
<div class="container-fluid">
