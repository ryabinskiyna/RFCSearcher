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
<form action="<%= request.getContextPath() %>/upload" method="post" enctype="multipart/form-data">
    <label for="sendbtn">Выберите файл для загрузки</label>
    <input type="file" id="sendbtn" name="sendbtn" accept=".txt" multiple value="Выбрать..."/>
    <input type="hidden" name="action" value="upload">
    <input type="submit" value="Отправить"/>
</form>

<br>
<a href="<%= request.getContextPath() %>/search">Перейти к поиску</a>
<br>
<p>Список загруженных файлов:</p>
<ul>
    <% for (String item : (Collection<String>) request.getAttribute("items")) { %>
    <li><%= item %>
    </li>
    <% } %>
</ul>

</body>
</html>
