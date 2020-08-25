package com.garden.endpoints;

import com.garden.StoredSID;
import com.garden.beans.User;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RoleValidatorTest {

    @Test
    public void validate() {
        User user = new User();
        user.setLogin("testUser");
        user.setName("testName");
        user.setId(1);
        user.setRole("testRole");

        StoredSID.addSID("testSID", user);

        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(session.getAttribute("SID")).thenReturn("testSID");
        when(request.getSession()).thenReturn(session);

        Assert.assertTrue(RoleValidator.validate(request, "testRole"));
        Assert.assertFalse(RoleValidator.validate(request, "otherRole"));

        verify(session, times(2)).getAttribute("SID");
        verify(request, times(2)).getSession();
    }
}