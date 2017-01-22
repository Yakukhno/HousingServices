<%@ include file="header.jsp"%>
    Dispatcher <c:out value="${sessionScope.user.id}"/>
    <a class="btn btn-default"
       style="text-align: center"
       href="/rest/application"
       role="button">
        Show applications
    </a>
<%@ include file="footer.jsp"%>
