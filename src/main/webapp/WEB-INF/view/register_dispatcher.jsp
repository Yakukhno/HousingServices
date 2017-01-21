<%@ include file="header.jsp"%>
<div class="row" style="height: 15%"></div>
<div class="row">
    <h3 align="center">Create a dispatcher account</h3>
</div>
<form class="form-horizontal col-md-offset-3 col-md-6" method="post" action="/rest/dispatcher">
    <div class="form-group">
        <label for="name" class="control-label col-md-4">Name</label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="text" id="name" name="name"/><br/>
        </div>
    </div>
    <div class="form-group">
        <label for="email" class="control-label col-md-4">Email</label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="text" id="email" name="email"/><br/>
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
            <button class="btn btn-default" type="submit">Send</button>
        </div>
    </div>
</form>
<%@ include file="footer.jsp"%>