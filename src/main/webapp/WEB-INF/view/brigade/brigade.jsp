<%@ include file="../include/header.jsp"%>
<div class="row">
    <div class="col-lg-offset-3 col-lg-6">
        <h2 align="center">
            <fmt:message key="brigade"/> ${requestScope[AttributeConstants.BRIGADE].id}
        </h2>
    </div>
</div>
    <table class="table table-striped" style="width: 80%" align="center">
        <thead>
        <tr>
            <th><fmt:message key="name"/></th>
            <th><fmt:message key="specializations"/></th>
            <th><fmt:message key="manager"/></th>
        </tr>
        </thead>
        <tbody>
            <tr>
                <td>${requestScope[AttributeConstants.BRIGADE].manager.name}</td>
                <td>
                    <c:forEach var="typeOfWork"
                               items="${requestScope[AttributeConstants.BRIGADE].manager.typesOfWork}">
                        <fmt:message>
                            ${typeOfWork.description}
                        </fmt:message>;
                    </c:forEach>
                </td>
                <td>
                    <fmt:message key="manager"/>
                </td>
            </tr>
            <c:forEach var="worker" items="${requestScope[AttributeConstants.BRIGADE].workers}">
                <tr>
                    <td>${worker.name}</td>
                    <td>
                        <c:forEach var="typeOfWork" items="${worker.typesOfWork}">
                            <fmt:message>
                                ${typeOfWork.description}
                            </fmt:message>;
                        </c:forEach>
                    </td>
                    <td></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
<%@ include file="../include/footer.jsp"%>