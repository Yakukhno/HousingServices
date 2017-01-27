<%@ include file="header.jsp"%>
    <div class="row">
        <div class="col-lg-offset-3 col-lg-6">
            <h2 align="center">Your applications</h2>
        </div>
        <div class="col-lg-3" style="padding: 20px 30px 10px 0px; text-align: right">
            <a class="btn btn-default" href="/rest/add_application" role="button">Add new</a>
        </div>
    </div>
    <c:choose>
        <c:when test="${requestScope[Attributes.APPLICATIONS].size() != 0}">
            <table class="table table-striped" align="center">
                <thead>
                    <tr>
                        <th>№</th>
                        <th>Type of task</th>
                        <th>Scale of problem</th>
                        <th>Desired date</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="application" items="${requestScope[Attributes.APPLICATIONS]}">
                        <tr>
                            <td>${application.id}</td>
                            <td>${application.typeOfWork.description}</td>
                            <td>${application.scaleOfProblem}</td>
                            <td>${application.desiredTime}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            You don't have any applications!
        </c:otherwise>
    </c:choose>
<%@ include file="footer.jsp"%>
