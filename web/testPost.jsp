<%-- 
    Document   : testPost
    Created on : 19/06/2018, 11:18:47
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="aluno" method="post">
            <input type="hidden" name="nome" value="test">
            <input type="hidden" name="sobrenome" value="da silva">
            <input type="hidden" name="email" value="test@test.com">
            <input type="hidden" name="login" value="testlogin">
            <input type="hidden" name="senha" value="testsenha">
            <input type="hidden" name="tipo" value="1">
            <input type="submit">
        </form>
    </body>
</html>
