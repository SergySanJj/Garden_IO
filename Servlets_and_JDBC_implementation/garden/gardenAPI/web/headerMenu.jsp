<%@ page import="com.garden.StoredSID" %>
<%@ page import="com.garden.beans.User" %>
<%@ page import="com.garden.endpoints.API" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Header</title>
    <style>
        nav {
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
            align-self: center;
            min-height: fit-content;
            min-width: fit-content;
            margin-right: auto;
        }

        ul {
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
            list-style-type: none;
            min-height: fit-content;
            min-width: fit-content;
        }

        li {
            min-height: fit-content;
            float: left;
            margin-right: 0;
            border-right: 1px solid #aaa;
            padding: 0 10px;
        }

        li:last-child {
            border-right: none;
        }

        li a {
            text-decoration: none;
            color: #ccc;
            font: 25px/1 Helvetica, Verdana, sans-serif;
            text-transform: uppercase;

            -webkit-transition: all 0.5s ease;
            -moz-transition: all 0.5s ease;
            -o-transition: all 0.5s ease;
            -ms-transition: all 0.5s ease;
            transition: all 0.5s ease;
        }

        li a:hover {
            color: #666;
        }

        li.active a {
            font-weight: bold;
            color: #333;
        }

        @media screen and (max-width: 700px) {

        }
    </style>
</head>
<body>
<% String role = StoredSID.getUser((String) session.getAttribute("SID")).getRole(); %>
<nav>
    <ul>
        <%if (User.Roles.ADMIN.equals(role)) { %>
        <li>
            <form action="<%=request.getContextPath()%>/" method="post">
                <input type="hidden"
                       name="<%=API.Content.Params.CONTENT%>"
                       value="<%=API.Content.Params.ViewOrders%>">
                <input type="submit" value="View orders">
            </form>
        </li>
        <li>
            <form action="<%=request.getContextPath()%>/" method="post">
                <input type="hidden"
                       name="<%=API.Content.Params.CONTENT%>"
                       value="<%=API.Content.Params.ViewUsers%>">
                <input type="submit" value="View users">
            </form>
        </li>
        <li>
            <form action="<%=request.getContextPath()%>/" method="post">
                <input type="hidden"
                       name="<%=API.Content.Params.CONTENT%>"
                       value="<%=API.Content.Params.ViewRoles%>">
                <input type="submit" value="View roles">
            </form>
        </li>
        <li>
            <form action="<%=request.getContextPath()%>/" method="post">
                <input type="hidden"
                       name="<%=API.Content.Params.CONTENT%>"
                       value="<%=API.AddGardener.ENDPOINT%>">
                <input type="submit" value="Add gardener">
            </form>
        </li>
        <li>
            <form action="<%=request.getContextPath()%>/" method="post">
                <input type="hidden"
                       name="<%=API.Content.Params.CONTENT%>"
                       value="<%=API.AddOwner.ENDPOINT%>">
                <input type="submit" value="Add owner">
            </form>
        </li>

        <% } else if (User.Roles.GARDENER.equals(role)) { %>
        <li>
            <form action="<%=request.getContextPath()%>/" method="post">
                <input type="hidden"
                       name="<%=API.Content.Params.CONTENT%>"
                       value="<%=API.Content.Params.ViewOpenJobs%>">
                <input type="submit" value="Available jobs">
            </form>
        </li>
        <li>
            <form action="<%=request.getContextPath()%>/" method="post">
                <input type="hidden"
                       name="<%=API.Content.Params.CONTENT%>"
                       value="<%=API.Content.Params.ViewFinishedJobs%>">
                <input type="submit" value="Finished jobs">
            </form>
        </li>
        <% } else if (User.Roles.OWNER.equals(role)) { %>
        <li>
            <form action="<%=request.getContextPath()%>/" method="post">
                <input type="hidden"
                       name="<%=API.Content.Params.CONTENT%>"
                       value="<%=API.Content.Params.ViewOpenOrders%>">
                <input type="submit" value="Orders">
            </form>
        </li>
        <li>
            <form action="<%=request.getContextPath()%>/" method="post">
                <input type="hidden"
                       name="<%=API.Content.Params.CONTENT%>"
                       value="<%=API.Content.Params.ViewUnconfirmedOrders%>">
                <input type="submit" value="Needs confirmation">
            </form>
        </li>
        <li>
            <form action="<%=request.getContextPath()%>/" method="post">
                <input type="hidden"
                       name="<%=API.Content.Params.CONTENT%>"
                       value="<%=API.AddOrder.ENDPOINT%>">
                <input type="submit" value="New Order">
            </form>
        </li>
        <% } else { %>
        out.println("Error panel");
        <% } %>
    </ul>
</nav>


</body>
</html>
