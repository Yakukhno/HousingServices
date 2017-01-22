<%@ include file="header.jsp"%>
<div class="row">
    <div class="col-lg-offset-3 col-lg-6">
        <h2 align="center">Workers</h2>
    </div>
</div>
<c:choose>
    <c:when test="${requestScope.workers.size() != 0}">
        <form class="form-horizontal" method="post" action="/rest/brigade">
            <table class="table table-striped" style="width: 80%" align="center">
                <thead>
                <tr>
                    <th>№</th>
                    <th>Name</th>
                    <th>Specializations</th>
                    <th>Add to brigade</th>
                    <th>Manager</th>
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
                                <input type="checkbox" name="worker" value="${worker.id}"/>
                            </td>
                            <td>
                                <input type="radio" name="manager" value="${worker.id}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <input type="hidden" name="application" value="${requestScope.application}"/>
            <div class="form-group" align="center">
                <button class="btn btn-default" type="submit">Form</button>
            </div>
        </form>
    </c:when>
    <c:otherwise>
        There no any workers!
    </c:otherwise>
</c:choose>
<%@ include file="footer.jsp"%>
