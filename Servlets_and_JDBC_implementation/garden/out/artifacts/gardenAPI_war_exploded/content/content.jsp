<%@ page import="com.garden.StoredSID" %>
<%@ page import="com.garden.endpoints.API" %>
<%@ page import="static jdk.internal.net.http.HttpRequestImpl.USER_AGENT" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.net.http.HttpRequest" %>
<%@ page import="java.net.http.HttpClient" %>
<%@ page import="java.net.http.HttpResponse" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.net.*" %>
<%@ page import="com.garden.Receiver" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="org.json.simple.parser.JSONParser" %>
<%@ page import="org.json.simple.JSONArray" %>
<%@ page import="com.garden.beans.User" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.List" %>
<%@ page import="com.garden.beans.Order" %>
<%@ page import="com.garden.beans.Role" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Content</title>
</head>
<body>
<%
    String role = StoredSID.getUser((String) session.getAttribute("SID")).getRole();
    String action = (String) session.getAttribute("last-action");
%>

<% switch (role) {
    case "admin": %>
<jsp:include page='/content/adminContent.jsp'/>
<%
        break;
    case "gardener":
%>
<jsp:include page='/content/gardenerContent.jsp'/>
<%
        break;
    case "owner":
%>
<jsp:include page='/content/ownerContent.jsp'/>
<%
            break;
    }
%>

<%if (action != null) {%>
<p><%=action%></p>
<%}%>
</body>
</html>
