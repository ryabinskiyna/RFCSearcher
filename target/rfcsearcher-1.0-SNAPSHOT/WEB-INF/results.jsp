<%@ page import="ru.itpark.enumeration.QueryStatus" %>
<%@ page import="ru.itpark.model.QueryModel" %>
<%@ page import="java.util.Collection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>RFC Searcher upload files</title>
</head>
<body>
<br>
<a href="<%= request.getContextPath() %>/search">Перейти к поиску</a>
<br>
<p>Список задач:</p>
<ul>
    <% for (QueryModel item : (Collection<QueryModel>) request.getAttribute("items")) { %>
    <li>Запрос: <%= item.getQuery() %>, статус: <%= String.valueOf(item.getStatus()) %>
        <c:if test="${item.getStatus() == QueryStatus.DONE}">
            <a href="<%= request.getContextPath() %>/results?download=<%= item.getId() %>">Скачать</a>
        </c:if>
    </li>
    <% } %>
</ul>

</body>
</html>
