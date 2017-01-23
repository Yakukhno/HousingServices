<%@ include file="header.jsp"%>
<div class="row">
    <div class="col-lg-offset-3 col-lg-6">
        <h2 align="center">Applications</h2>
    </div>
</div>
<c:choose>
    <c:when test="${requestScope.applications.size() != 0}">
        <table class="table table-striped" style="width: 80%" align="center">
            <thead>
            <tr>
                <th>№</th>
                <th>Type of task</th>
                <th>Scale of problem</th>
                <th>Desired date</th>
                <th>Brigade</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="task" items="${requestScope.tasks}">
                <tr>
                    <td>${task.application.id}</td>
                    <td>${task.application.typeOfWork.description}</td>
                    <td>${task.application.scaleOfProblem}</td>
                    <td>${task.application.desiredTime}</td>
                    <td>
                        ${task.brigade.id} <a href="/rest/brigade/${task.brigade.id}">Show</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        There no any tasks!
    </c:otherwise>
</c:choose>
<%@ include file="footer.jsp"%>

