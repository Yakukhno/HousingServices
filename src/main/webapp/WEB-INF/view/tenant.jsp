<%@ include file="header.jsp"%>
<div class="row">
    <h2 align="center">Tenant profile</h2>
</div>
<div class="col-md-offset-3 col-md-6">
    <div class="row">
        <div class="col-md-offset-3 col-md-3">
            <p>Account id</p>
        </div>
        <div class="col-md-6">
            <p>${requestScope.tenant.account}</p>
        </div>
    </div>
    <div class="row">
        <div class="col-md-offset-3 col-md-3">
            <p>Name</p>
        </div>
        <div class="col-md-6">
            <p>${requestScope.tenant.name}</p>
        </div>
    </div>
    <div class="row">
        <div class="col-md-offset-3 col-md-3">
            <p>Email</p>
        </div>
        <div class="col-md-6">
            <p>${requestScope.tenant.email}</p>
        </div>
    </div>
    <div class="row">
        <h3 align="center">Edit profile</h3>
    </div>
    <form class="form-horizontal" method="post" action="/rest/tenant/${requestScope.tenant.id}">
        <div class="form-group">
            <label for="email" class="control-label col-md-5">New email</label>
            <div class="col-md-7">
                <input class="form-control" type="text" id="email" name="newEmail"/>
            </div>
        </div>
        <div class="form-group">
            <label for="oldPassword" class="control-label col-md-5">Old password</label>
            <div class="col-md-7">
                <input class="form-control" type="password" id="oldPassword"/>
            </div>
        </div>
        <div class="form-group">
            <label for="newPassword" class="control-label col-md-5">New password</label>
            <div class="col-md-7">
                <input class="form-control" type="password" id="newPassword" name="newPassword"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-md-offset-5 col-md-7">
                <button class="btn btn-default" type="submit">Change</button>
            </div>
        </div>
    </form>
</div>
<%@ include file="footer.jsp"%>
