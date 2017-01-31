<%@ include file="header.jsp"%>
<div class="row" style="height: 15%"></div>
<div class="row">
    <h3 align="center"><fmt:message key="add_application"/></h3>
</div>
<form class="form-horizontal col-md-offset-3 col-md-6" method="post" action="/rest/application">
    <input type="hidden" id="tenantId" value="${sessionScope[Attributes.USER].id}"/>
    <div class="form-group">
        <label for="typeOfWork" class="control-label col-md-4"><fmt:message key="type_of_task"/></label>
        <div class="col-md-8">
            <select id="typeOfWork" name="typeOfWork" class="form-control">
                <c:forEach var="typeOfWork" items="${requestScope[Attributes.TYPE_OF_WORK]}">
                    <option value="${typeOfWork.id}">${typeOfWork.description}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label for="problemScale" class="control-label col-md-4"><fmt:message key="problem_scale"/></label>
        <div class="col-md-8">
            <select id="problemScale" name="problemScale" class="form-control">
                <c:forEach var="problemScale" items="${requestScope[Attributes.PROBLEM_SCALE]}">
                    <option>${problemScale}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label for="dateTime" class="control-label col-md-4"><fmt:message key="desired_time"/></label>
        <div class="col-md-8">
            <input type="datetime-local" id="dateTime" name="dateTime" class="form-control"/>
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
                    ${requestScope[Attributes.MESSAGE]}
            </div>
        </div>
    </c:if>
</form>
<%@ include file="footer.jsp"%>