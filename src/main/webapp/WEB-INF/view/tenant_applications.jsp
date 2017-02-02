<%@ include file="header.jsp"%>
    <div class="row">
        <div class="col-lg-offset-3 col-lg-6">
            <h2 align="center"><fmt:message key="your_applications"/></h2>
        </div>
        <div class="col-lg-3" style="padding: 20px 30px 10px 0px; text-align: right">
            <a class="btn btn-primary" href="/rest/add_application" role="button"><fmt:message key="add_new"/></a>
        </div>
    </div>
    <c:choose>
        <c:when test="${requestScope[Attributes.APPLICATIONS].size() != 0}">
            <table class="table table-striped" align="center">
                <thead>
                    <tr>
                        <th><fmt:message key="type_of_task"/></th>
                        <th><fmt:message key="problem_scale"/></th>
                        <th><fmt:message key="desired_time"/></th>
                        <th><fmt:message key="status"/></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="application" items="${requestScope[Attributes.APPLICATIONS]}">
                        <tr>
                            <td>${application.typeOfWork.description}</td>
                            <td>${application.scaleOfProblem}</td>
                            <td>${application.desiredTime}</td>
                            <td>${application.status}</td>
                            <td>
                                <c:if test="${application.status eq requestScope[Attributes.STATUS_NEW]}">
                                    <form method="post" action="/rest/application/${application.id}/delete" style="margin-bottom: 0">
                                        <button class="btn btn-danger" type="submit"><fmt:message key="delete"/></button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <fmt:message key="no_user_applications"/>
        </c:otherwise>
    </c:choose>
<%@ include file="footer.jsp"%>
