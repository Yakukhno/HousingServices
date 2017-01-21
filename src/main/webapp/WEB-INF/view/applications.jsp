<%@ include file="header.jsp"%>
    Application<br/>
    <c:forEach var="application" items="${applications}">
        <c:out value="${application}"/><br/>
    </c:forEach>
<%@ include file="footer.jsp"%>
