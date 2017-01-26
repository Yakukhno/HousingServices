<%@ include file="header.jsp"%>
<div class="row">
    <div class="col-lg-offset-3 col-lg-6">
        <h2 align="center">Scheduled tasks</h2>
    </div>
</div>
<div class="row">
    <div class="col-lg-offset-1 col-lg-10">
        <c:choose>
            <c:when test="${requestScope.applications.size() != 0}">
                <table class="table table-striped" align="center">
                    <thead>
                    <tr>
                        <th>N</th>
                        <th>Type of task</th>
                        <th>Scale of problem</th>
                        <th>Scheduled time</th>
                        <th>Brigade</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="task" items="${requestScope.tasks}">
                        <tr>
                            <td>${task.application.id}</td>
                            <td>${task.application.typeOfWork.description}</td>
                            <td>${task.application.scaleOfProblem}</td>
                            <td>${task.scheduledTime}</td>
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
    </div>
</div>
<%@ include file="footer.jsp"%>

