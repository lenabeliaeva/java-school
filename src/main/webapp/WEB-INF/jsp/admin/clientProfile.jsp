<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${client.name} ${client.lastName}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <%@include file="../parts/header.jsp" %>
</head>
<body>
<c:if test="${clientContracts.size() > 0}">
    <h3>Contracts</h3>
    <form:form>
        <table class="table table-hover">
            <tr>
                <td>Number</td>
                <td>Tariff</td>
                <td>Tariff Price</td>
            </tr>
            <c:forEach var="contract" items="${clientContracts}">
                <tr>
                    <td>${contract.number}</td>
                    <td>${contract.tariff.name}</td>
                    <td>${contract.price}</td>
                    <td>
                        <button class="btn btn-outline-primary"
                                formmethod="get"
                                formaction="/contract/options/${contract.id}"
                                type="submit">Connected options
                        </button>
                    </td>
                    <td>
                        <c:if test="${contract.blockedByClient == false && contract.blockedByAdmin == false}">
                            <button class="btn btn-outline-danger"
                                    onclick="return confirm('Are you sure you want to block the contract?')"
                                    formaction="/admin/blockContract/${client.id}/${contract.id}"
                                    type="submit">Block contract
                            </button>
                        </c:if>
                        <c:if test="${contract.blockedByClient == true || contract.blockedByAdmin == true}">
                            <button class="btn btn-outline-primary"
                                    formaction="/admin/unblockContract/${client.id}/${contract.id}"
                                    type="submit">Unblock contract
                            </button>
                        </c:if>
                    </td>
                    <td>
                        <button class="btn btn-outline-danger"
                                onclick="return confirm('Are you sure you want to terminate the contract?')"
                                formaction="/admin/clientProfile/${client.id}/delete/${contract.id}"
                                type="submit">
                            Terminate contract
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </form:form>
</c:if>
<c:if test="${clientContracts.size() == 0}">
    <p>The client hasn't got any contracts</p>
</c:if>
<form action="/admin/signContract/${client.id}">
    <button type="submit" class="btn btn-outline-primary">Sign new contract</button>
</form>
<form action="/admin/clients">
    <button type="submit" class="btn btn-outline-primary">Back to client list</button>
</form>
</body>
</html>
