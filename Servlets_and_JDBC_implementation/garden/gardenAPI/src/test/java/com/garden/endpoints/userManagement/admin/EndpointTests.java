package com.garden.endpoints.userManagement.admin;

import com.db.DatabaseAction;
import com.db.DatabaseConfig;
import com.garden.GardenServlet;
import com.garden.StoredSID;
import com.garden.beans.User;
import com.garden.endpoints.API;
import com.garden.endpoints.contentManagement.gardener.FinishJobEndpoint;
import com.garden.endpoints.contentManagement.owner.AddOrderEndpoint;
import com.garden.endpoints.contentManagement.owner.ApproveOrderEndpoint;
import com.garden.endpoints.contentView.admin.ViewOrdersEndpoint;
import com.garden.endpoints.contentView.admin.ViewRolesEndpoint;
import com.garden.endpoints.contentView.admin.ViewUsersEndpoint;
import com.garden.endpoints.contentView.gardener.ViewFinishedJobsEndpoint;
import com.garden.endpoints.contentView.gardener.ViewOpenJobsEndpoint;
import com.garden.endpoints.contentView.owner.ViewOpenOrdersEndpoint;
import com.garden.endpoints.contentView.owner.ViewUnconfirmedOrdersEndpoint;
import com.garden.endpoints.userAuth.AuthEndpoint;
import com.garden.endpoints.userAuth.LogoutEndpoint;
import org.junit.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EndpointTests {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeClass
    public static void initDB() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        DatabaseConfig.config("gardenTestConfig.json");
        DatabaseAction.dropAndInit();
    }

    @Before
    public void addStoredSID() {
        User admin = new User(1, "admin", "testName1", "admin");
        User gardener = new User(2, "gardener", "testName2", "gardener");
        User owner = new User(3, "owner", "testName3", "owner");

        StoredSID.addSID("s1", admin);
        StoredSID.addSID("s2", gardener);
        StoredSID.addSID("s3", owner);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
    }

    @After
    public void clear() {
        StoredSID.clear();
    }

    @Test
    public void addGardener() throws ServletException, IOException {
        when(session.getAttribute("SID")).thenReturn("s1"); // admin
        when(request.getParameter(API.AddGardener.Params.LOGIN)).thenReturn("testLoginGardener");
        when(request.getParameter(API.AddGardener.Params.PASSWORD)).thenReturn("testPass");
        when(request.getParameter(API.AddGardener.Params.NAME)).thenReturn("testName");
        Assert.assertFalse(DatabaseAction.userLoginExists("testLoginGardener"));
        AddGardenerEndpoint servlet = new AddGardenerEndpoint();
        servlet.doPost(request, response);
        Assert.assertTrue(DatabaseAction.userLoginExists("testLoginGardener"));

        verify(session, atLeast(1)).getAttribute("SID");
    }

    @Test
    public void notAdminCantAddGardener() throws ServletException, IOException {
        when(session.getAttribute("SID")).thenReturn("s2"); // not admin
        when(request.getParameter(API.AddGardener.Params.LOGIN)).thenReturn("testLoginGardenerNotAdmin");
        when(request.getParameter(API.AddGardener.Params.PASSWORD)).thenReturn("testPass");
        when(request.getParameter(API.AddGardener.Params.NAME)).thenReturn("testName");
        Assert.assertFalse(DatabaseAction.userLoginExists("testLoginGardenerNotAdmin"));
        AddGardenerEndpoint servlet = new AddGardenerEndpoint();
        servlet.doPost(request, response);
        Assert.assertFalse(DatabaseAction.userLoginExists("testLoginGardenerNotAdmin"));

        verify(session, atLeast(1)).getAttribute("SID");
        verify(response, atLeast(1)).sendRedirect("/");
    }

    @Test
    public void addOwner() throws ServletException, IOException {
        when(session.getAttribute("SID")).thenReturn("s1"); // admin
        when(request.getParameter(API.AddGardener.Params.LOGIN)).thenReturn("testLoginAdmin");
        when(request.getParameter(API.AddGardener.Params.PASSWORD)).thenReturn("testPass");
        when(request.getParameter(API.AddGardener.Params.NAME)).thenReturn("testName");
        Assert.assertFalse(DatabaseAction.userLoginExists("testLoginAdmin"));
        AddOwnerEndpoint servlet = new AddOwnerEndpoint();
        servlet.doPost(request, response);
        Assert.assertTrue(DatabaseAction.userLoginExists("testLoginAdmin"));

        verify(session, atLeast(1)).getAttribute("SID");
    }

    @Test
    public void notAdminCantAddOwner() throws ServletException, IOException {
        when(session.getAttribute("SID")).thenReturn("s2"); // not admin
        when(request.getParameter(API.AddGardener.Params.LOGIN)).thenReturn("testLoginOwnerNotAdmin");
        when(request.getParameter(API.AddGardener.Params.PASSWORD)).thenReturn("testPass");
        when(request.getParameter(API.AddGardener.Params.NAME)).thenReturn("testName");
        Assert.assertFalse(DatabaseAction.userLoginExists("testLoginOwnerNotAdmin"));
        AddGardenerEndpoint servlet = new AddGardenerEndpoint();
        servlet.doPost(request, response);
        Assert.assertFalse(DatabaseAction.userLoginExists("testLoginOwnerNotAdmin"));

        verify(session, atLeast(1)).getAttribute("SID");
        verify(response, atLeast(1)).sendRedirect("/");
    }

    @Test
    public void authValidCredentials() throws ServletException, IOException {
        when(session.getAttribute("SID")).thenReturn(null);
        String login = "admin";
        String pass = "admin";
        when(request.getParameter(API.Authorisation.Params.LOGIN)).thenReturn(login);
        when(request.getParameter(API.Authorisation.Params.PASSWORD)).thenReturn(pass);
        AuthEndpoint auth = new AuthEndpoint();
        auth.doPost(request, response);
        verify(response, atLeast(1)).sendRedirect(request.getContextPath() + "/");
        verify(request, atLeast(1)).getSession();
    }

    @Test
    public void authInvalidCredentials() throws ServletException, IOException {
        when(session.getAttribute("SID")).thenReturn(null);
        String login = "invalid";
        String pass = "invalid";
        when(request.getParameter(API.Authorisation.Params.LOGIN)).thenReturn(login);
        when(request.getParameter(API.Authorisation.Params.PASSWORD)).thenReturn(pass);
        AuthEndpoint auth = new AuthEndpoint();
        auth.doPost(request, response);
        verify(response, atLeast(1)).sendRedirect(request.getContextPath() + "/login");
        verify(request, times(0)).getSession();
    }

    @Test
    public void logout() throws ServletException, IOException {
        when(session.getAttribute("SID")).thenReturn("s1");
        LogoutEndpoint logout = new LogoutEndpoint();
        logout.doPost(request, response);
        verify(session).invalidate();
        verify(session).removeAttribute("SID");
    }

    @Test
    public void listAPI() throws IOException {
        when(session.getAttribute("SID")).thenReturn("s1");
        GardenServlet gardenServlet = new GardenServlet();
        gardenServlet.doGet(request, response);
        verify(response, atLeast(1)).getWriter();
    }

    @Test
    public void addOrder() throws ServletException, IOException {
        when(session.getAttribute("SID")).thenReturn("s3");
        when(request.getParameter(API.AddOrder.Params.TYPE)).thenReturn("heal");
        when(request.getParameter(API.AddOrder.Params.QUANTITY)).thenReturn(String.valueOf(10));
        AddOrderEndpoint addOrderEndpoint = new AddOrderEndpoint();
        int ordersBefore = countRowsIn("orders");
        addOrderEndpoint.doPost(request, response);
        int ordersAfter = countRowsIn("orders");
        Assert.assertTrue(ordersBefore < ordersAfter);
        verify(session).setAttribute("last-action", "Order for " + 10 + " of " + "heal" + " posted");
    }

    @Test
    public void approveOrder() throws ServletException, IOException {
        int ordersBefore = countRowsIn("orders");

        when(session.getAttribute("SID")).thenReturn("s3");
        when(request.getParameter(API.ApproveOrder.Params.ORDERID)).thenReturn(String.valueOf(ordersBefore));

        ApproveOrderEndpoint approveOrderEndpoint = new ApproveOrderEndpoint();
        approveOrderEndpoint.doPost(request, response);
        int ordersAfter = countRowsIn("orders");
        Assert.assertEquals(ordersBefore, ordersAfter);
        verify(request).getParameter(API.ApproveOrder.Params.ORDERID);
    }

    @Test
    public void finishJob() throws ServletException, IOException {
        int ordersBefore = countRowsIn("orders");

        when(session.getAttribute("SID")).thenReturn("s2");
        when(request.getParameter(API.FinishJob.Params.ORDERID)).thenReturn(String.valueOf(ordersBefore));

        FinishJobEndpoint finishJobEndpoint = new FinishJobEndpoint();
        finishJobEndpoint.doPost(request, response);
        int ordersAfter = countRowsIn("orders");
        Assert.assertEquals(ordersBefore, ordersAfter);
        verify(request).getParameter(API.FinishJob.Params.ORDERID);
        verify(session).setAttribute("last-action", "Order " + ordersBefore + " completed, waiting for approval");
    }

    @Test
    public void viewOrders() throws ServletException, IOException {
        when(session.getAttribute("SID")).thenReturn("s1");

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        ViewOrdersEndpoint viewOrdersEndpoint = new ViewOrdersEndpoint();
        viewOrdersEndpoint.doGet(request, response);
        verify(response, atLeast(1)).getWriter();
        String representation = stringWriter.toString();
        int ordersCount = countRowsIn("orders");

        Assert.assertEquals(ordersCount, countOccurrences(representation, "id"));
    }

    @Test
    public void viewRoles() throws ServletException, IOException {
        when(session.getAttribute("SID")).thenReturn("s1");

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        ViewRolesEndpoint viewRolesEndpoint = new ViewRolesEndpoint();
        viewRolesEndpoint.doGet(request, response);
        verify(response, atLeast(1)).getWriter();
        String representation = stringWriter.toString();


        Assert.assertTrue(representation.contains("admin"));
        Assert.assertTrue(representation.contains("gardener"));
        Assert.assertTrue(representation.contains("owner"));
    }

    @Test
    public void viewUsers() throws ServletException, IOException {
        when(session.getAttribute("SID")).thenReturn("s1");

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        ViewUsersEndpoint viewUsersEndpoint = new ViewUsersEndpoint();
        viewUsersEndpoint.doGet(request, response);
        verify(response, atLeast(1)).getWriter();
        String representation = stringWriter.toString();
        int usersCount = countRowsIn("users");

        Assert.assertEquals(usersCount, countOccurrences(representation, "role"));
    }

    @Test
    public void viewFinishedJobs() throws IOException, ServletException {
        when(session.getAttribute("SID")).thenReturn("s2");
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        ViewFinishedJobsEndpoint viewFinishedJobsEndpoint = new ViewFinishedJobsEndpoint();
        viewFinishedJobsEndpoint.doGet(request, response);
        verify(response, atLeast(1)).getWriter();
        String representation = stringWriter.toString();
        int finishedCount = countRowsIn("orders WHERE orders.finished = true AND orders.gardener = 2");
        Assert.assertEquals(finishedCount, countOccurrences(representation, "quantity"));
    }

    @Test
    public void viewOpenJobs() throws IOException, ServletException {
        when(session.getAttribute("SID")).thenReturn("s2");
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        ViewOpenJobsEndpoint viewOpenJobsEndpoint = new ViewOpenJobsEndpoint();
        viewOpenJobsEndpoint.doGet(request, response);
        verify(response, atLeast(1)).getWriter();
        String representation = stringWriter.toString();
        int count = countRowsIn("orders WHERE orders.finished = false");
        Assert.assertEquals(count, countOccurrences(representation, "quantity"));
    }

    @Test
    public void viewOpenOrders() throws IOException, ServletException {
        when(session.getAttribute("SID")).thenReturn("s3");
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        ViewOpenOrdersEndpoint viewOpenOrdersEndpoint = new ViewOpenOrdersEndpoint();
        viewOpenOrdersEndpoint.doGet(request, response);
        verify(response, atLeast(1)).getWriter();
        String representation = stringWriter.toString();
        int count = countRowsIn("orders WHERE orders.finished = false AND orders.owner = 3");
        Assert.assertEquals(count, countOccurrences(representation, "quantity"));
    }

    @Test
    public void viewUnconfirmedOrders() throws IOException, ServletException {
        when(session.getAttribute("SID")).thenReturn("s3");
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        ViewUnconfirmedOrdersEndpoint viewUnconfirmedOrdersEndpoint = new ViewUnconfirmedOrdersEndpoint();
        viewUnconfirmedOrdersEndpoint.doGet(request, response);
        verify(response, atLeast(1)).getWriter();
        String representation = stringWriter.toString();
        int count = countRowsIn("orders WHERE orders.finished = true AND orders.approved = false AND orders.owner = 3");
        Assert.assertEquals(count, countOccurrences(representation, "quantity"));
    }


    private int countRowsIn(String tableName) {
        final Integer[] cnt = new Integer[1];
        cnt[0] = 0;
        DatabaseAction.run(connection -> {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM " + tableName);
            resultSet.next();
            cnt[0] = resultSet.getInt(1);
        });
        return cnt[0];
    }

    private int countOccurrences(String str, String pattern) {
        int cnt = 0;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        while (m.find()) {
            cnt++;
        }
        return cnt;
    }
}