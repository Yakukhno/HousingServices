<%@ include file="../include/header.jsp"%>
<div class="row">
    <div class="col-lg-offset-3 col-lg-6">
        <h2 align="center"><fmt:message key="applications"/></h2>
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
                        <th><fmt:message key="desired_time"/></th>
                        <th><fmt:message key="status"/></th>
                        <th><fmt:message key="consideration"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="application"
                               items="${requestScope[Attributes.APPLICATIONS]}">
                       <tr>
                            <td>${application.typeOfWork.description}</td>
                            <td>${application.scaleOfProblem}</td>
                            <td>
                                <c:if test="${not empty application.desiredTime}">
                                    <custom:dateTime>${application.desiredTime}</custom:dateTime>
                                </c:if>
                            </td>
                            <td>${application.status}</td>
                            <td>
                                <c:if test="${application.status eq requestScope[Attributes.STATUS_NEW]}">
                                    <form style="margin-bottom: 0"
                                          method="post"
                                          action="/rest/new_task">
                                        <input type="hidden" name="application" value="${application.id}"/>
                                        <button class="btn btn-primary" type="submit">
                                            <fmt:message key="form_brigade"/>
                                        </button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <fmt:message key="no_applications"/>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<%@ include file="../include/footer.jsp"%>

