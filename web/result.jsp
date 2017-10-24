<%-- 
    Document   : result
    Created on : Sep 17, 2017, 5:23:07 PM
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
        <h3>There are a total of <%= request.getAttribute("totalCount")%> XKCD comics in the archive.</h3> 
        <h3> 
        <% Integer searchCount=(Integer) request.getAttribute("searchCount"); 
            out.print(searchCount);
            if(searchCount == 1){
                out.print(" comic");
            }else out.print(" comics");
        %>
        had "<%= request.getAttribute("searchWord")%>" in the title.</h3>
        <% if(((Integer) request.getAttribute("searchCount")) != 0) {%>
        <h3> Here is one of them: </h3>
        <h2> <%= request.getAttribute("comicName")%> </h2>
        <img src="<%= request.getAttribute("resultUrl")%>"><br><br>
        <% } %>
        <form action="getXKCDSearch" method="GET">
            <label for="letter">Search XKCD titles for: </label>
            <input type="text" name="searchedWord" value="" />
            <input type="submit" value="Search" /><br>
        </form>
        <br>
        <a href="https://xkcd.com/">XKCD</a> is a webcomic by Randall Munroe
    </body>
</html>
