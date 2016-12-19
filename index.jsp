<%-- 
    Document   : index
    Created on : Nov 18, 2016, 3:07:26 PM
    Author     : RebeccaD
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>French Translator Log</title>
    </head>
    <body>
        <h1>French Translator Logs</h1>
        <% Integer logRows = (Integer)request.getAttribute("ROWS");%>
        <TABLE>
            
            
                
            
        </TABLE>
        <%=request.getAttribute("LOGS")%>
    </body>
    
</html>
