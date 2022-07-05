<%--
  Created by IntelliJ IDEA.
  User: data03
  Date: 2021-05-27
  Time: 오후 3:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String msg = (String) request.getAttribute("msg");
    String url = (String) request.getAttribute("url");
%>
<html>
<head>
    <script type="text/javascript">
        alert("<%=msg%>")
        location.href = "<%=url%>"
    </script>
    <meta charset="UTF-8">
    <title>redirect</title>
</head>
<body>

</body>
</html>
