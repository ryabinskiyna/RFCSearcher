<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>RFC Searcher</title>
</head>
<body>
<form action="<%= request.getContextPath() %>/search" method="post">
    <input name="query" placeholder="Поиск" value="" style="width: 400px;">
    <input type="hidden" name="action" value="search">
    <button>Искать</button>
</form>

<% String id = (String) request.getAttribute("searchid");
    if (id != null) { %>
<label>Ваш запрос добавлен в очередь: <%= id %>
</label>
<%} %>

<br>
<a href="<%= request.getContextPath() %>/upload">Перейти к загрузке</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
        href="<%= request.getContextPath() %>/results">Перейти к результатам</a>
<br>

</body>
</html>
