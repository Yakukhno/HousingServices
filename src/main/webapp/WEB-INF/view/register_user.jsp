<%@ include file="header.jsp"%>
<div class="row" style="height: 15%"></div>
<div class="row">
    <h3 align="center">Create a user account</h3>
</div>
<form class="form-horizontal col-md-offset-3 col-md-6" name="registerForm" onsubmit="return validateUser()" method="post" action="/rest/user">
    <div class="form-group" id="nameForm">
        <label for="name" class="control-label col-md-4">Name</label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="text" id="name" name="name"/><br/>
            <span id="helpName" class="help-block"></span>
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
        <label for="role" class="control-label col-md-4">Role</label>
        <div class="col-md-8">
            <select id="role" name="role" class="form-control">
                <option>${applicationScope[Attributes.TENANT]}</option>
                <option>${applicationScope[Attributes.DISPATCHER]}</option>
            </select>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-offset-4 col-md-8">
            <button class="btn btn-default" type="submit">Send</button>
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
<%@ include file="footer.jsp"%>