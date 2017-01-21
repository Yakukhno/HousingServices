<%@ include file="header.jsp"%>
<div class="row" style="height: 15%"></div>
<div class="row">
    <h3 align="center">Create a new application</h3>
</div>
<form class="form-horizontal col-md-offset-3 col-md-6" method="post" action="/rest/application">
    <input type="hidden" id="tenantId" value="${sessionScope.user.id}"/>
    <div class="form-group">
        <label for="typeOfWork" class="control-label col-md-4">Type of work</label>
        <div class="col-md-8">
            <select id="typeOfWork" name="typeOfWork" class="form-control">
                <c:forEach var="typeOfWork" items="${requestScope.typesOfWork}">
                    <option>${typeOfWork.description}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label for="problemScale" class="control-label col-md-4">Problem scale</label>
        <div class="col-md-8">
            <select id="problemScale" name="problemScale" class="form-control">
                <c:forEach var="problemScale" items="${requestScope.problemScales}">
                    <option>${problemScale}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label for="dateTime" class="control-label col-md-4">Desired time</label>
        <div class="col-md-8">
            <input type="datetime-local" id="dateTime" name="dateTime" class="form-control"/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-offset-4 col-md-8">
            <button class="btn btn-default" type="submit">Send</button>
        </div>
    </div>
</form>
<%@ include file="footer.jsp"%>