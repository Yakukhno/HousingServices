<%@ include file="header.jsp"%>
<div class="row">
    <div class="col-lg-offset-3 col-lg-6">
        <h2 align="center"><fmt:message key="workers"/></h2>
    </div>
</div>
<div class="row">
    <div class="col-lg-offset-1 col-lg-10">
        <c:choose>
            <c:when test="${requestScope.workers.size() != 0}">
                <form class="form-horizontal" name="brigadeForm" onsubmit="return validateBrigade()" method="post" action="/rest/task">
                    <table class="table table-striped" align="center">
                        <thead>
                        <tr>
                            <th>N</th>
                            <th><fmt:message key="name"/></th>
                            <th><fmt:message key="specializations"/></th>
                            <th><fmt:message key="add_to_brigade"/></th>
                            <th><fmt:message key="manager"/></th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="worker" items="${requestScope.workers}">
                                <tr>
                                    <td>${worker.id}</td>
                                    <td>${worker.name}</td>
                                    <td>
                                        <c:forEach var="typeOfWork" items="${worker.typesOfWork}">
                                            <c:out value="${typeOfWork.description}; "/>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <input type="checkbox" name="worker" id="worker" value="${worker.id}"/>
                                    </td>
                                    <td>
                                        <input type="radio" name="manager" id="manager" value="${worker.id}"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <input type="hidden" name="application" value="${requestScope.application}"/>
                    <div class="row">
                        <div class="col-lg-offset-4 col-lg-4">
                            <div class="form-group" align="center">
                                <label for="dateTime" class="control-label col-md-4"><fmt:message key="time"/></label>
                                <div class="col-md-8">
                                    <input type="datetime-local" required id="dateTime" name="dateTime" class="form-control"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="message"></div>
                    <c:if test="${not empty requestScope[Attributes.MESSAGE]}">
                        <div class="row">
                            <div class="alert alert-danger" align="center" role="alert">
                                <fmt:message key="${requestScope[Attributes.MESSAGE]}"/>
                            </div>
                        </div>
                    </c:if>
                    <div class="form-group" align="center">
                        <button class="btn btn-default" type="submit">Form</button>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <fmt:message key="no_workers"/>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<%@ include file="footer.jsp"%>
