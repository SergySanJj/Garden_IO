<%@ page import="com.garden.StoredSID" %>
<%@ page import="com.garden.endpoints.API" %>
<%@ page import="com.garden.beans.Job" %>
<%@ page import="com.garden.Receiver" %>
<%@ page import="java.util.List" %>
<%@ page import="com.garden.Represent" %>
<%@ page import="com.garden.beans.FinishedJob" %>
<%@ page import="com.garden.beans.OpenOrder" %>
<%@ page import="com.garden.beans.UnconfirmedOrder" %>
<%@ page import="java.lang.reflect.Method" %>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Owner Content</title>
</head>
<body>

<%
    String contentType = request.getParameter("content");
%>

<%if (contentType == null) {%>
Welcome, <%=StoredSID.getUser((String) session.getAttribute("SID")).getName()%>!
<%} else if (contentType.equals(API.Content.Params.ViewOpenOrders)) {%>
<%
    Receiver<OpenOrder> openOrderReceiver = new Receiver<>(OpenOrder.class);
    List<OpenOrder> openOrders = openOrderReceiver.arrayContent("openOrders", API.Content.Params.ViewOpenOrders, request, session.getId());

    Represent<OpenOrder> represent = new Represent<>(OpenOrder.class);
    out.println(represent.asTable(openOrders));
%>
<%} else if (contentType.equals(API.Content.Params.ViewUnconfirmedOrders)) {%>
<%
    Receiver<UnconfirmedOrder> unconfirmedOrderReceiver = new Receiver<>(UnconfirmedOrder.class);
    List<UnconfirmedOrder> unconfirmedOrders = unconfirmedOrderReceiver.arrayContent("unconfirmedOrders", API.Content.Params.ViewUnconfirmedOrders, request, session.getId());

    Represent<UnconfirmedOrder> represent = new Represent<>(UnconfirmedOrder.class);
    out.println(represent.asOrderOperatable(unconfirmedOrders, request, API.ApproveOrder.ENDPOINT));
%>
<%} else if (contentType.equals(API.AddOrder.ENDPOINT)) {%>
<form class="login" autocomplete="off" action="<%=request.getContextPath()%>/api/<%=API.AddOrder.ENDPOINT%>"
      method="post">
    <p>Add new Order</p>
    <%=Receiver.getOrderTypesRepresentation()%>
    <input type="number" placeholder="<%=API.AddOrder.Params.QUANTITY%>" name="<%=API.AddOrder.Params.QUANTITY%>">

    <input type="submit">
</form>
<%}%>
</body>
</html>
