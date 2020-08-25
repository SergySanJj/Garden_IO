<%@ page import="com.garden.StoredSID" %>
<%@ page import="com.garden.endpoints.API" %>
<%@ page import="com.garden.beans.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.garden.Receiver" %>
<%@ page import="com.garden.beans.Role" %>
<%@ page import="com.garden.beans.Order" %>
<%@ page import="com.garden.Represent" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin content</title>
</head>
<body>
<%
    String contentType = request.getParameter("content");
%>

<%if (contentType == null) {%>
Welcome, <%=StoredSID.getUser((String) session.getAttribute("SID")).getName()%>!
<%} else if (contentType.equals(API.Content.Params.ViewUsers)) {%>
<%
    Receiver<User> userReceiver = new Receiver<>(User.class);
    List<User> users = userReceiver.arrayContent("users", API.Content.Params.ViewUsers, request, session.getId());

    Represent<User> represent = new Represent<>(User.class);
    out.println(represent.asTable(users));
%>
<%} else if (contentType.equals(API.Content.Params.ViewRoles)) {%>
<%
    Receiver<Role> roleReceiver = new Receiver<>(Role.class);
    List<Role> roles = roleReceiver.arrayContent("roles", API.Content.Params.ViewRoles, request, session.getId());

    Represent<Role> represent = new Represent<>(Role.class);
    out.println(represent.asTable(roles));
%>
<%} else if (contentType.equals(API.Content.Params.ViewOrders)) {%>
<%
    Receiver<Order> orderReceiver = new Receiver<>(Order.class);
    List<Order> orders = orderReceiver.arrayContent("orders", API.Content.Params.ViewOrders, request, session.getId());

    Represent<Order> represent = new Represent<>(Order.class);
    out.println(represent.asTable(orders));
%>
<%} else if (contentType.equals(API.AddGardener.ENDPOINT)) {%>
<form class="login" autocomplete="off" action="<%=request.getContextPath()%>/api/<%=API.AddGardener.ENDPOINT%>"
      method="post">
    <p>Add new Gardener</p>
    <input type="text" placeholder="login" name="login">
    <input type="password" placeholder="password" name="password">
    <input type="text" placeholder="name" name="name">

    <input type="submit">
</form>
<%} else if (contentType.equals(API.AddOwner.ENDPOINT)) {%>
<form class="login" autocomplete="off" action="<%=request.getContextPath()%>/api/<%=API.AddOwner.ENDPOINT%>"
      method="post">
    <p>Add new Owner</p>
    <input type="text" placeholder="login" name="login">
    <input type="password" placeholder="password" name="password">
    <input type="text" placeholder="name" name="name">

    <input type="submit">
</form>
<%}%>


</body>
</html>
