<%@ include file="header.jsp"%>
    <h2>Tenants</h2>
    <c:forEach var="tenant" items="${tenants}">
        <c:out value="${tenant}"/>
    </c:forEach>
    <br/>
    <c:out value="${sessionScope.user.name}"/>
<%@ include file="footer.jsp"%>