<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Анкета Prog Kiev</title>
    <link rel="stylesheet" href="css/formStyle.css">
</head>
<body>

    <div class="content">
        <% String fistName = (String) session.getAttribute("user_firstName");
            String lastName = (String) session.getAttribute("user_lastName"); %>

        <% if(fistName == null || fistName.equals("")){ %>
        <form action="/submit" method="POST">
            <p>Имя: <input required type="text" name="firstName"></p>
            <p>Прозвище: <input required type="text" name="lastName"></p>
            <p>Возраст: <input required type="number" name="age"></p>
            <p><b>JavaScript - это скрипт на Java?</b></p>
            <p><input required name="question1" type="radio" value="yes" checked>Да</p>
            <p><input required name="question1" type="radio" value="no">Нет</p>
            <p><b>HTML - это полноценный язык програмирования?</b></p>
            <p><input required name="question2" type="radio" value="yes" checked>Да</p>
            <p><input required name="question2" type="radio" value="no">Нет</p>
            <p><input type="submit" value="Подтвердить"></p>
        </form>
        <% } else { %>
        <h1><%= fistName %> <%= lastName %>, спасибо за участие!</h1>
        <a href="/submit?action=retry">Пройти анкету еще раз</a><br>
        <a href="/statistic">Посмотреть статистику ответов</a>
        <% } %>
    </div>

</body>
</html>