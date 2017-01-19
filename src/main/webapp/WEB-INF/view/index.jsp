<%@ include file="header.jsp"%>
    <h2 align="center">Planned tasks</h2>
    <table class="table table-striped" align="center">
        <thead>
            <tr>
                <th>№</th>
                <th>Type of task</th>
                <th>Scale of problem</th>
                <th>Planned date</th>
                <th>Brigade of workers</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="task" items="${tasks}">
                <tr>
                    <td>${task.id}</td>
                    <td>${task.application.typeOfWork.description}</td>
                    <td>${task.application.scaleOfProblem}</td>
                    <td>${task.application.desiredTime}</td>
                    <td>${task.brigade.id}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
<%@ include file="footer.jsp"%>
