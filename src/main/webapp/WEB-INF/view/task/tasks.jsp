<%@ include file="../include/header.jsp"%>
<div class="row">
    <div class="col-lg-offset-3 col-lg-6">
        <h2 align="center"><fmt:message key="header.tasks"/></h2>
    </div>
</div>
<div class="row">
    <div class="col-lg-offset-1 col-lg-10">
        <c:choose>
            <c:when test="${requestScope[AttributeConstants.TASKS].size() != 0}">
                <table class="table table-striped" align="center">
                    <thead>
                    <tr>
                        <th><fmt:message key="type_of_work"/></th>
                        <th><fmt:message key="problem_scale"/></th>
                        <th><fmt:message key="scheduled_time"/></th>
                        <th><fmt:message key="brigade"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="task" items="${requestScope[AttributeConstants.TASKS]}">
                        <tr>
                            <td>
                                <fmt:message>
                                    ${task.application.typeOfWork.description}
                                </fmt:message>
                            </td>
                            <td>
                                <fmt:message>
                                    <custom:enumLocale enumClass="${task.application.problemScale.getClass().getSimpleName()}">
                                        ${task.application.problemScale}
                                    </custom:enumLocale>
                                </fmt:message>
                            </td>
                            <td>
                                <custom:dateTime>${task.scheduledTime}</custom:dateTime>
                            </td>
                            <td>
                                ${task.brigade.id} <a href="/web/brigade/${task.brigade.id}">
                                    <fmt:message key="show"/>
                                </a>
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

