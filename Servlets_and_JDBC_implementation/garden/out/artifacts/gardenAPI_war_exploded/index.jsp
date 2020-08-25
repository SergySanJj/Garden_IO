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
    response.setIntHeader("Refresh", 5*60);
%>

<div class="container">
    <div class="header">
        <p class="logo">
            Little Garden
        </p>

        <jsp:include page='/headerMenu.jsp'/>

        <form class="logout" action="<%=request.getContextPath()%>/api/logout" method="post">
            <div>
                <p><%=StoredSID.getUser((String) session.getAttribute("SID")).getName()%>
                </p>
                <p style="font-size: 0.8em"><%=StoredSID.getUser((String) session.getAttribute("SID")).getRole()%>
                </p>
            </div>
            <input type="submit" value="Logout">
        </form>
    </div>


    <div class="content">
        <jsp:include page='/content/content.jsp'/>
    </div>

    <jsp:include page='/footer.jsp'/>
</div>


<script type="text/javascript">
    <%@include file="/js/login.js" %>
</script>
</body>
</html>
