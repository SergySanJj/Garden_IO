package com.garden;

import com.garden.beans.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FrontEndAuthenticationFilterTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private FilterChain chain;

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
        chain = mock(FilterChain.class);
        when(request.getSession()).thenReturn(session);
    }

    @After
    public void clearSID(){
        StoredSID.clear();
    }

    @Test
    public void doFilterLoggedInUser() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/protected-content");
        when(session.getAttribute("SID")).thenReturn("s1");
        FrontEndAuthenticationFilter filter = new FrontEndAuthenticationFilter();
        filter.doFilter(request, response, chain);
        verify(session, atLeast(1)).getAttribute("SID");
        verify(chain, atLeast(1)).doFilter(request, response);
        verify(response, times(0)).sendRedirect(request.getContextPath() + "/login");

    }

    @Test
    public void doFilterNotLoggedInUser() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/protected-content");
        when(session.getAttribute("SID")).thenReturn(null);
        FrontEndAuthenticationFilter filter = new FrontEndAuthenticationFilter();
        filter.doFilter(request, response, chain);
        verify(session, atLeast(1)).getAttribute("SID");
        verify(chain, times(0)).doFilter(request, response);
        verify(response, atLeast(1)).sendRedirect(request.getContextPath() + "/login");
    }

    @Test
    public void doFilterPretendingToBeLoggedInUser() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/protected-content");
        when(session.getAttribute("SID")).thenReturn("ImLoggedIn");
        FrontEndAuthenticationFilter filter = new FrontEndAuthenticationFilter();
        filter.doFilter(request, response, chain);
        verify(session, atLeast(1)).getAttribute("SID");
        verify(chain, times(0)).doFilter(request, response);
        verify(response, atLeast(1)).sendRedirect(request.getContextPath() + "/login");
    }

    @Test
    public void isLoggedIn() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        // Correct SID
        when(session.getAttribute("SID")).thenReturn("s1");
        Assert.assertTrue(FrontEndAuthenticationFilter.isLoggedIn(request));
        when(session.getAttribute("SID")).thenReturn("s2");
        Assert.assertTrue(FrontEndAuthenticationFilter.isLoggedIn(request));
        when(session.getAttribute("SID")).thenReturn("s3");
        Assert.assertTrue(FrontEndAuthenticationFilter.isLoggedIn(request));
        // Incorrect SID
        when(session.getAttribute("SID")).thenReturn("unexistingSID");
        Assert.assertFalse(FrontEndAuthenticationFilter.isLoggedIn(request));
        // Empty case
        when(session.getAttribute("SID")).thenReturn("");
        Assert.assertFalse(FrontEndAuthenticationFilter.isLoggedIn(request));
        // Null case
        when(session.getAttribute("SID")).thenReturn(null);
        Assert.assertFalse(FrontEndAuthenticationFilter.isLoggedIn(request));
    }
}