<%@ include file="header.jsp"%>
<div class="row">
    <h2 align="center">
        <c:choose>
            <c:when test="${requestScope[Attributes.USER].role eq applicationScope[Attributes.TENANT]}">
                Tenant profile
            </c:when>
            <c:when test="${requestScope[Attributes.USER].role eq applicationScope[Attributes.DISPATCHER]}">
                Dispatcher profile
            </c:when>
            <c:otherwise>
                User profile
            </c:otherwise>
        </c:choose>
    </h2>
</div>
<div class="col-md-offset-3 col-md-6">
    <div class="row">
        <div class="col-md-offset-3 col-md-3">
            <p>Name</p>
        </div>
        <div class="col-md-6">
            <p>${requestScope[Attributes.USER].name}</p>
        </div>
    </div>
    <div class="row">
        <div class="col-md-offset-3 col-md-3">
            <p>Email</p>
        </div>
        <div class="col-md-6">
            <p>${requestScope[Attributes.USER].email}</p>
        </div>
    </div>
    <div class="row">
        <h3 align="center">Edit profile</h3>
    </div>
    <form class="form-horizontal" name="registerForm" onsubmit="return validateUserUpdate()" method="post" action="/rest/user/${requestScope.user.id}">
        <div class="form-group" id="emailForm">
            <label for="email" class="control-label col-md-5">New email</label>
            <div class="col-md-7">
                <input class="form-control" type="text" id="email" name="email"/>
                <span id="helpEmail" class="help-block"></span>
            </div>
        </div>
        <div class="form-group" id="oldPasswordForm">
            <label for="oldPassword" class="control-label col-md-5">Old password</label>
            <div class="col-md-7">
                <input class="form-control" type="password" id="oldPassword" name="oldPassword"/>
                <span id="helpOldPass" class="help-block"></span>
            </div>
        </div>
        <div class="form-group" id="newPasswordForm">
            <label for="newPassword" class="control-label col-md-5">New password</label>
            <div class="col-md-7">
                <input class="form-control" type="password" id="newPassword" name="newPassword"/>
                <span id="helpNewPass" class="help-block"></span>
            </div>
        </div>
        <div class="form-group">
            <div class="col-md-offset-5 col-md-7">
                <button class="btn btn-default" type="submit">Change</button>
            </div>
        </div>
        <c:if test="${not empty requestScope[Attributes.MESSAGE]}">
            <div class="row">
                <div class="alert alert-danger" align="center" role="alert">
                        ${requestScope[Attributes.MESSAGE]}
                </div>
            </div>
        </c:if>
    </form>
</div>
<%@ include file="footer.jsp"%>
