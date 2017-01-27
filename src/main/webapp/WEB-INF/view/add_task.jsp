<%@ include file="header.jsp"%>
<div class="row">
    <div class="col-lg-offset-3 col-lg-6">
        <h2 align="center">Workers</h2>
    </div>
</div>
<div class="row">
    <div class="col-lg-offset-1 col-lg-10">
        <c:choose>
            <c:when test="${requestScope.workers.size() != 0}">
                <form class="form-horizontal" name="brigadeForm" onsubmit="return validateBrigade()" method="post" action="/rest/task">
                    <table class="table table-striped" align="center">
                        <thead>
                        <tr>
                            <th>№</th>
                            <th>Name</th>
                            <th>Specializations</th>
                            <th>Add to brigade</th>
                            <th>Manager</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="worker" items="${requestScope.workers}">
                                <tr>
                                    <td>${worker.id}</td>
                                    <td>${worker.name}</td>
                                    <td>
                                        <c:forEach var="typeOfWork" items="${worker.typesOfWork}">
                                            <c:out value="${typeOfWork.description}; "/>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <input type="checkbox" name="worker" id="worker" value="${worker.id}"/>
                                    </td>
                                    <td>
                                        <input type="radio" name="manager" id="manager" value="${worker.id}"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <input type="hidden" name="application" value="${requestScope.application}"/>
                    <div class="row">
                        <div class="col-lg-offset-4 col-lg-4">
                            <div class="form-group" align="center">
                                <label for="dateTime" class="control-label col-md-4">Time</label>
                                <div class="col-md-8">
                                    <input type="datetime-local" id="dateTime" name="dateTime" class="form-control"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="message"></div>
                    <div class="form-group" align="center">
                        <button class="btn btn-default" type="submit">Form</button>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                There no any workers!
            </c:otherwise>
        </c:choose>
    </div>
</div>
<%@ include file="footer.jsp"%>
