<%@ page import="com.garden.StoredSID" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Little garden</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/icon" type="image/x-icon"/>

    <link href="https://fonts.googleapis.com/css?family=Roboto&display=swap" rel="stylesheet"/>
    <style>
        <%@include file="/css/style.css" %>
    </style>
</head>
<body>

<%
    response.setHeader("Cache-Control", "no-cache, no store, must-revalidate");
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0);
%>

<%
    if (session != null && StoredSID.exists((String) session.getAttribute("SID"))) {
        response.sendRedirect(request.getContextPath() + "/");
    }
%>

<div class="container">
    <div class="header">
        <p class="logo">
            Little Garden
        </p>
    </div>

    <div class="content">
        <form class="login" autocomplete="on" action="<%=request.getContextPath()%>/api/auth" method="post">
            <input type="text" placeholder="login" name="login">
            <input type="password" placeholder="password" name="password">

            <input type="submit">
        </form>
    </div>
    <div class="footer">
        <p>
            Little garden control service developed by <a href="https://github.com/SergySanJj">Sergei Yarema</a>
        </p>
    </div>
</div>


<script type="text/javascript">
    <%@include file="/js/login.js" %>
</script>
</body>
</html>
