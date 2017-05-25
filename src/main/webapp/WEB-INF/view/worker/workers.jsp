<%@ include file="../include/header.jsp"%>
<div class="row">
    <div class="col-lg-offset-3 col-lg-6">
        <h2 align="center"><fmt:message key="workers"/></h2>
    </div>
    <div class="col-lg-3" style="padding: 20px 30px 10px 0px; text-align: right">
        <a class="btn btn-primary" href="/web/new_worker" role="button">
            <fmt:message key="add"/>
        </a>
    </div>
</div>
<div class="row">
    <div class="col-lg-offset-1 col-lg-10">
        <c:choose>
            <c:when test="${requestScope[Attributes.WORKERS].size() != 0}">
                <table class="table table-striped" align="center">
                    <thead>
                    <tr>
                        <th>N</th>
                        <th><fmt:message key="name"/></th>
                        <th><fmt:message key="specializations"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="worker"
                               items="${requestScope[Attributes.WORKERS]}">
                        <tr>
                            <td>${worker.id}</td>
                            <td>${worker.name}</td>
                            <td>
                                <c:forEach var="typeOfWork"
                                           items="${worker.typesOfWork}">
                                    <fmt:message>
                                        ${typeOfWork.description}
                                    </fmt:message>;
                                </c:forEach>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <fmt:message key="no_workers"/>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<%@ include file="../include/footer.jsp"%>

