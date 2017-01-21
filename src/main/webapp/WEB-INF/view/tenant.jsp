<%@ include file="header.jsp"%>
    Tenant <c:out value="${sessionScope.user.id}"/><br/>
    <a class="btn btn-default"
       style="text-align: center"
       href="/rest/tenant/${sessionScope.user.id}/application"
       role="button">
        Show applications
    </a>
<%@ include file="footer.jsp"%>
