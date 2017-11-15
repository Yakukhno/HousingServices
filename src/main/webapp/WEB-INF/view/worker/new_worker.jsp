<%@ include file="../include/header.jsp"%>
<div class="row">
    <h3 align="center"><fmt:message key="add_worker"/></h3>
</div>
<div class="row">
    <form class="form-horizontal col-md-offset-3 col-md-6"
          method="post"
          action="/web/worker">
        <div class="form-group">
            <label for="name" class="control-label col-md-4">
                <fmt:message key="name"/>
            </label>
            <div class="col-md-8">
                <input class="form-control col-md-8" type="text" id="name" name="name"/><br/>
                <span id="helpName" class="help-block"></span>
            </div>
        </div>
        <div class="form-group">
            <div class="col-md-offset-4 col-md-8">
                <c:forEach var="typeOfWork" items="${requestScope[AttributeConstants.TYPE_OF_WORK]}">
                    <label>
                        <input type="checkbox" name="typesOfWork" value="${typeOfWork.id}">
                            <fmt:message>
                                ${typeOfWork.description}
                            </fmt:message><br/>
                        </input>
                    </label>
                </c:forEach>
            </div>
        </div>
        <sec:csrfInput/>
        <div class="form-group">
            <div class="col-md-offset-4 col-md-8">
                <button class="btn btn-default" type="submit"><fmt:message key="send"/></button>
            </div>
        </div>
        <c:if test="${not empty requestScope[AttributeConstants.MESSAGE]}">
            <div class="row">
                <div class="alert alert-danger" align="center" role="alert">
                    <fmt:message key="${requestScope[AttributeConstants.MESSAGE]}">
                        <c:if test="${not empty requestScope[AttributeConstants.PARAMS]}">
                            <c:forEach var="parameter"
                                       items="${requestScope[AttributeConstants.PARAMS]}">
                                <fmt:param>${parameter}</fmt:param>
                            </c:forEach>
                        </c:if>
                    </fmt:message>
                </div>
            </div>
        </c:if>
    </form>
</div>
<%@ include file="../include/footer.jsp"%>
