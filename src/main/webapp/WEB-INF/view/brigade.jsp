<%@ include file="header.jsp"%>
<div class="row">
    <div class="col-lg-offset-3 col-lg-6">
        <h2 align="center">Brigade ${requestScope.brigade.id}</h2>
    </div>
</div>
<c:choose>
    <c:when test="${requestScope.brigade.workers.size() != 0}">
        <table class="table table-striped" style="width: 80%" align="center">
            <thead>
            <tr>
                <th>Name</th>
                <th>Specializations</th>
                <th>Manager</th>
            </tr>
            </thead>
            <tbody>
                <tr>
                    <td>${requestScope.brigade.manager.name}</td>
                    <td>
                        <c:forEach var="typeOfWork" items="${requestScope.brigade.manager.typesOfWork}">
                            <c:out value="${typeOfWork.description}; "/>
                        </c:forEach>
                    </td>
                    <td>
                        Manager
                    </td>
                </tr>
                <c:forEach var="worker" items="${requestScope.brigade.workers}">
                    <tr>
                        <td>${worker.name}</td>
                        <td>
                            <c:forEach var="typeOfWork" items="${worker.typesOfWork}">
                                <c:out value="${typeOfWork.description}; "/>
                            </c:forEach>
                        </td>
                        <td></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        There no any workers!
    </c:otherwise>
</c:choose>
<%@ include file="footer.jsp"%>