<%@ include file="header.jsp"%>
<div class="row" style="height: 15%"></div>
<div class="row">
    <h3 align="center">Log in</h3>
</div>
<form class="form-horizontal col-md-offset-3 col-md-6" method="post" action="/rest/login">
    <div class="form-group">
        <label for="login" class="control-label col-md-4">Email or account id</label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="text" id="login" name="login"/><br/>
        </div>
    </div>
    <div class="form-group">
        <label for="password" class="control-label col-md-4">Password</label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="password" id="password" name="password"/><br/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-offset-4 col-md-8">
            <button class="btn btn-default" type="submit">Login</button>
        </div>
    </div>
</form>
<%@ include file="footer.jsp"%>
