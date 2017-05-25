<%@ include file="../include/header.jsp"%>
<div class="row" style="height: 15%"></div>
<div class="row">
    <h3 align="center"><fmt:message key="header.login"/></h3>
</div>
<form class="form-horizontal col-md-offset-3 col-md-6" method="post" action="/web/login">
    <div class="form-group">
        <label for="email" class="control-label col-md-4">
            <fmt:message key="email"/>
        </label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="text" id="email" name="email"/><br/>
        </div>
    </div>
    <div class="form-group">
        <label for="password" class="control-label col-md-4">
            <fmt:message key="password"/>
        </label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="password" id="password" name="password"/><br/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-offset-4 col-md-8">
            <button class="btn btn-default" type="submit">
                <fmt:message key="header.login"/>
            </button>
        </div>
    </div>
    <c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
        <div class="row">
            <div class="alert alert-danger" align="center" role="alert">
                <fmt:message key="exception.email_password"/>
            </div>
        </div>
    </c:if>
</form>
<%@ include file="../include/footer.jsp"%>
