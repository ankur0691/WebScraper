<%-- 
    Document   : welcome
    Created on : Sep 17, 2017, 5:24:09 PM
    Author     : ankur
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>XKCD Comic Archive Search</h1>
        <form action="getXKCDSearch" method="GET">
            <label for="letter">Search XKCD titles for: </label>
            <input type="text" name="searchedWord" value="" />
            <input type="submit" value="Search" />
        </form>
        <br>
        <a href="https://xkcd.com/">XKCD</a> is a webcomic by Randall Munroe
    </body>
</html>
