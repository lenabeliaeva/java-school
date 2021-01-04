<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Редактирование тарифа</title>
    <%@include file="parts/header.jsp" %>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
</head>
<body>
<h2>Введите новые данные</h2>
<form:form method="post" modelAttribute="editedTariff" action="/saveEditedTariff">
    <form:input type="hidden" path="id"/>
    <div class="form-group">
        <form:label path="name">Введите название</form:label>
        <form:input type="text" required="name" class="form-control" path="name"/>
    </div>
    <div class="form-group">
        <form:label path="price">Введите цену</form:label>
        <form:input type="number" required="price" min="1" class="form-control" path="price"/>
    </div>
    <table class="table table-hover">
        <tr>
            <td>Название</td>
            <td>Цена</td>
            <td>Стоимость подключения</td>
        </tr>
        <c:forEach var="option" items="${options}">
            <tr>
                <td>${option.name}</td>
                <td>${option.price}</td>
                <td>${option.connectionCost}</td>
                <td>
                    <button class="btn btn-outline-danger" formaction="/deleteOption/${editedTariff.id}/${option.id}"
                            type="submit">Удалить
                    </button>
                </td>
            </tr>
        </c:forEach>
    </table>
    <button type="submit" formaction="/addOption/${editedTariff.id}" class="btn btn-outline-primary">Добавить опции
    </button>
    <button type="submit" class="btn btn-outline-primary">Сохранить изменения</button>
</form:form>
</body>
</html>