<%@ include file="header.jsp"%>
<div class="col-lg-offset-3 col-lg-6" style="text-align: center">
    <h2 align=>An error occurred!</h2>
    <div class="row">
        <h4>
            Status code : ${requestScope['javax.servlet.error.status_code']}
        </h4>
        <c:if test="${requestScope['javax.servlet.error.status_code'] == 500}">
            <h4>
                Exception : ${requestScope['javax.servlet.error.exception_type']}
            </h4>
        </c:if>
        <h4>
            Message : ${requestScope['javax.servlet.error.message']}<br/>
        </h4>
    </div>
</div>
<%@ include file="footer.jsp"%>