<%@ include file="../include/header.jsp"%>
<div class="row">
    <h2 align="center">
        <sec:authorize access="hasRole('ROLE_TENANT')">
            <fmt:message key="tenant_profile"/>
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_DISPATCHER')">
            <fmt:message key="dispatcher_profile"/>
        </sec:authorize>
    </h2>
</div>
<div class="col-md-offset-3 col-md-6">
    <div class="row">
        <div class="col-md-offset-3 col-md-3">
            <p><fmt:message key="name"/></p>
        </div>
        <div class="col-md-6">
            <p>${requestScope[Attributes.USER].name}</p>
        </div>
    </div>
    <div class="row">
        <div class="col-md-offset-3 col-md-3">
            <p><fmt:message key="email"/></p>
        </div>
        <div class="col-md-6">
            <p>${requestScope[Attributes.USER].email}</p>
        </div>
    </div>
    <div class="row">
        <h3 align="center"><fmt:message key="edit_profile"/></h3>
    </div>
    <form class="form-horizontal" name="registerForm" method="post" action="/web/user/${requestScope.user.id}">
        <div class="form-group" id="emailForm">
            <label for="email" class="control-label col-md-5"><fmt:message key="new_email"/></label>
            <div class="col-md-7">
                <input class="form-control" type="text" id="email" name="email"/>
                <span id="helpEmail" class="help-block"></span>
            </div>
        </div>
        <div class="form-group" id="oldPasswordForm">
            <label for="oldPassword" class="control-label col-md-5"><fmt:message key="old_password"/></label>
            <div class="col-md-7">
                <input class="form-control" type="password" id="oldPassword" name="oldPassword"/>
                <span id="helpOldPass" class="help-block"></span>
            </div>
        </div>
        <div class="form-group" id="newPasswordForm">
            <label for="newPassword" class="control-label col-md-5"><fmt:message key="new_password"/></label>
            <div class="col-md-7">
                <input class="form-control" type="password" id="newPassword" name="newPassword"/>
                <span id="helpNewPass" class="help-block"></span>
            </div>
        </div>
        <div class="form-group">
            <div class="col-md-offset-5 col-md-7">
                <button class="btn btn-default" type="submit"><fmt:message key="change"/></button>
            </div>
        </div>
        <c:if test="${not empty requestScope[Attributes.MESSAGE]}">
            <div class="row">
                <div class="alert alert-danger" align="center" role="alert">
                    <fmt:message key="${requestScope[Attributes.MESSAGE]}">
                        <c:if test="${not empty requestScope[Attributes.PARAMS]}">
                            <c:forEach var="parameter" items="${requestScope[Attributes.PARAMS]}">
                                <fmt:param value="${parameter}"/>
                            </c:forEach>
                        </c:if>
                    </fmt:message>
                </div>
            </div>
        </c:if>
    </form>
</div>
<%@ include file="../include/footer.jsp"%>
