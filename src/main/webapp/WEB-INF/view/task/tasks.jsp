<%@ include file="../include/header.jsp"%>
<div class="row">
    <div class="col-lg-offset-3 col-lg-6">
        <h2 align="center"><fmt:message key="header.tasks"/></h2>
    </div>
</div>
<div class="row">
    <div class="col-lg-offset-1 col-lg-10">
        <c:choose>
            <c:when test="${requestScope[Attributes.APPLICATIONS].size() != 0}">
                <table class="table table-striped" align="center">
                    <thead>
                    <tr>
                        <th><fmt:message key="type_of_task"/></th>
                        <th><fmt:message key="problem_scale"/></th>
                        <th><fmt:message key="scheduled_time"/></th>
                        <th><fmt:message key="brigade"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="task" items="${requestScope[Attributes.TASKS]}">
                        <tr>
                            <td>${task.application.typeOfWork.description}</td>
                            <td>${task.application.scaleOfProblem}</td>
                            <td>${task.scheduledTime}</td>
                            <td>
                                ${task.brigade.id} <a href="/rest/brigade/${task.brigade.id}"><fmt:message key="show"/></a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <fmt:message key="no_tasks"/>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<%@ include file="../include/footer.jsp"%>

