<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isErrorPage="true" %>
<html>
<head>
    <title>Housing services</title>
    <meta name="viewport" charset="UTF-8" content="width=device-width, initial-scale=1"/>
    <link href="/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <h2 align="center">An error occurred!</h2>
    </div>
    <div class="col-lg-offset-1 col-lg-10">
        <table class="table table-striped" align="center">
            <tbody>
                <tr>
                    <td>URI</td>
                    <td>${pageContext.errorData.requestURI}</td>
                </tr>
                <tr>
                    <td>Message</td>
                    <td>${pageContext.errorData.throwable.message}</td>
                </tr>
                <tr>
                    <td>Stack trace</td>
                    <td>
                        <c:forEach var="trace" items="${pageContext.exception.stackTrace}">
                            ${trace}<br/>
                        </c:forEach>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>
<script src="/js/validator.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
</body>
</html>