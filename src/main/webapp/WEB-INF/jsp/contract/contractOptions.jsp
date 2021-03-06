<%--
  Created by IntelliJ IDEA.
  User: lenab
  Date: 25.01.2021
  Time: 19:39
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Connected options</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <%@include file="../parts/header.jsp" %>
</head>
<body>
<h3>Tariff: ${contract.tariff.name}</h3>
<h3>Price: ${contract.price}</h3>
<h3>Connection cost: ${contract.connectionCost}</h3>
<form:form method="post">
    <c:if test="${options.size() > 0}">
        <table class="table table-hover">
            <tr>
                <td>Name</td>
                <td>Price</td>
                <td>Connection cost</td>
            </tr>
            <c:forEach var="option" items="${options}">
                <tr>
                    <td>${option.name}</td>
                    <td>${option.price}</td>
                    <td>${option.connectionCost}</td>
                    <td>
                        <c:if test="${contract.blockedByClient == false && contract.blockedByAdmin == false}">
                            <button class="btn btn-outline-danger"
                                    onclick="return confirm('Are you sure you want to disconnect the option?')"
                                    formaction="/contract/disconnectOption/${contract.id}/${option.id}"
                                    type="submit"
                            >Disconnect
                            </button>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${contract.blockedByAdmin == false && contract.blockedByClient == false}">
        <button class="btn btn-outline-primary" formmethod="get" formaction="/contract/connectOptions/${contract.id}">
            Connect new options
        </button>
        <button class="btn btn-outline-primary" formmethod="get" formaction="/contract/changeTariff/${contract.id}">
            Change tariff
        </button>
    </c:if>
</form:form>
</body>
</html>
