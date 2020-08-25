<%@ page import="com.garden.StoredSID" %>
<%@ page import="com.garden.endpoints.API" %>
<%@ page import="com.garden.beans.Job" %>
<%@ page import="com.garden.Receiver" %>
<%@ page import="java.util.List" %>
<%@ page import="com.garden.Represent" %>
<%@ page import="com.garden.beans.FinishedJob" %>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gardener Content</title>
</head>
<body>
<%
    String contentType = request.getParameter("content");
%>

<%if (contentType == null) {%>
Welcome, <%=StoredSID.getUser((String) session.getAttribute("SID")).getName()%>!
<%} else if (contentType.equals(API.Content.Params.ViewOpenJobs)) {%>
<%
    Receiver<Job> jobReceiver = new Receiver<>(Job.class);
    List<Job> jobs = jobReceiver.arrayContent("jobs", API.Content.Params.ViewOpenJobs, request, session.getId());

    Represent<Job> represent = new Represent<>(Job.class);
    out.println(represent.asOrderOperatable(jobs, request, API.FinishJob.ENDPOINT));
%>
<%} else if (contentType.equals(API.Content.Params.ViewFinishedJobs)) {%>
<%
    Receiver<FinishedJob> finishedJobReceiver = new Receiver<>(FinishedJob.class);
    List<FinishedJob> finishedJobs = finishedJobReceiver.arrayContent("finishedJobs", API.Content.Params.ViewFinishedJobs, request, session.getId());

    Represent<FinishedJob> represent = new Represent<>(FinishedJob.class);
    out.println(represent.asTable(finishedJobs));
%>
<%}%>
</body>
</html>
