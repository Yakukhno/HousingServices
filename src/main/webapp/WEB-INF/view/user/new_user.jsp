<%@ include file="../include/header.jsp"%>
<div class="row" style="height: 15%"></div>
<div class="row">
    <h3 align="center"><fmt:message key="create_user"/></h3>
</div>
<form class="form-horizontal col-md-offset-3 col-md-6" name="registerForm" method="post" action="/web/user">
    <div class="form-group" id="nameForm">
        <label for="name" class="control-label col-md-4"><fmt:message key="name"/></label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="text" id="name" name="name"/><br/>
            <span id="helpName" class="help-block"></span>
        </div>
    </div>
    <div class="form-group" id="emailForm">
        <label for="email" class="control-label col-md-4"><fmt:message key="email"/></label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="text" id="email" name="email"/><br/>
            <span id="helpEmail" class="help-block"></span>
        </div>
    </div>
    <div class="form-group" id="passwordForm">
        <label for="password" class="control-label col-md-4"><fmt:message key="password"/></label>
        <div class="col-md-8">
            <input class="form-control col-md-8" type="password" id="password" name="password"/><br/>
            <span id="helpPass" class="help-block"></span>
        </div>
    </div>
    <div class="form-group">
        <label for="role" class="control-label col-md-4"><fmt:message key="role"/></label>
        <div class="col-md-8">
            <select id="role" name="role" class="form-control">
                <option>${applicationScope[Attributes.TENANT]}</option>
                <option>${applicationScope[Attributes.DISPATCHER]}</option>
            </select>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-offset-4 col-md-8">
            <button class="btn btn-default" type="submit"><fmt:message key="send"/></button>
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
<%@ include file="../include/footer.jsp"%>