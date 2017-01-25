<%@ include file="header.jsp"%>
<div class="row" style="height: 15%"></div>
<div class="row">
    <h3 align="center">Create a tenant account</h3>
</div>
<form class="form-horizontal col-md-offset-3 col-md-6" name="registerForm" onsubmit="return validateTenant()" method="post" action="/rest/tenant">
    <div class="form-group" id="accountForm">
        <label for="account" class="control-label col-md-4">Account id</label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="text" id="account" name="account"/><br/>
            <span id="helpAccount" class="help-block"></span>
        </div>
    </div>
    <div class="form-group">
        <label for="name" class="control-label col-md-4">Name</label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="text" id="name" name="name"/><br/>
        </div>
    </div>
    <div class="form-group" id="emailForm">
        <label for="email" class="control-label col-md-4">Email</label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="text" id="email" name="email"/><br/>
            <span id="helpEmail" class="help-block"></span>
        </div>
    </div>
    <div class="form-group" id="passwordForm">
        <label for="password" class="control-label col-md-4">Password</label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="password" id="password" name="password"/><br/>
            <span id="helpPass" class="help-block"></span>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-offset-4 col-md-8">
            <button class="btn btn-default" type="submit">Send</button>
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
<%@ include file="footer.jsp"%>