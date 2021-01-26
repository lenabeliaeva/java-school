<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Подключение опций</title>
    <%@include file="../parts/header.jsp" %>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
</head>
<body>
<h3>Подключённые опции</h3>
<table class="table table-hover">
    <tr>
        <td>Название</td>
        <td>Цена</td>
        <td>Стоимость подключения</td>
    </tr>
    <c:forEach var="option" items="${connectedOptions}">
        <tr>
            <td>${option.name}</td>
            <td>${option.price}</td>
            <td>${option.connectionCost}</td>
        </tr>
    </c:forEach>
</table>
<br>
<h3>Доступные для подключения опции</h3>
<form:form>
    <table class="table table-hover">
        <tr>
            <td>Название</td>
            <td>Цена</td>
            <td>Стоимость подключения</td>
        </tr>
        <c:forEach var="option" items="${availableOptions}">
            <tr>
                <td>${option.name}</td>
                <td>${option.price}</td>
                <td>${option.connectionCost}</td>
                <td>
                    <button class="btn btn-outline-primary" formaction="/admin/connectOption/${contract.id}/${option.id}"
                            type="submit"
                    >Подключить
                    </button>
                </td>
            </tr>
        </c:forEach>
    </table>
    <button class="btn btn-outline-primary" type="submit" formmethod="get" formaction="/admin/clients">Оформить контракт</button>
</form:form>
</body>
</html>
