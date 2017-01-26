<%@ include file="header.jsp"%>
<div class="row">
    <h2 align="center">Dispatcher profile</h2>
</div>
<div class="col-md-offset-3 col-md-6">
    <div class="row">
        <div class="col-md-offset-3 col-md-3">
            <p>Name</p>
        </div>
        <div class="col-md-6">
            <p>${requestScope.dispatcher.name}</p>
        </div>
    </div>
    <div class="row">
        <div class="col-md-offset-3 col-md-3">
            <p>Email</p>
        </div>
        <div class="col-md-6">
            <p>${requestScope.dispatcher.email}</p>
        </div>
    </div>
    <div class="row">
        <h3 align="center">Edit profile</h3>
    </div>
    <form class="form-horizontal" name="registerForm" onsubmit="return validateUserUpdate()" method="post" action="/rest/dispatcher/${requestScope.dispatcher.id}">
        <div class="form-group">
            <label for="name" class="control-label col-md-5">New name</label>
            <div class="col-md-7">
                <input class="form-control" type="text" id="name" name="newName"/>
            </div>
        </div>
        <div class="form-group" id="emailForm">
            <label for="email" class="control-label col-md-5">New email</label>
            <div class="col-md-7">
                <input class="form-control" type="text" id="email" name="newEmail"/>
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
        <c:if test="${requestScope.message != null}">
            <div class="row">
                <div class="alert alert-danger" align="center" role="alert">
                        ${requestScope.message}
                </div>
            </div>
        </c:if>
    </form>
</div>
<%@ include file="footer.jsp"%>