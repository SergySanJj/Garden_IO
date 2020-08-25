package com.garden;

import com.garden.beans.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StoredSIDTest {
    private User user;

    @Before
    public void initUser() {
        User user = new User();
        user.setLogin("testUser");
        user.setName("testName");
        user.setId(1);
        user.setRole("testRole");
    }

    @After
    public void clearStorage(){
        StoredSID.clear();
    }

    @Test
    public void exists() {
        Assert.assertFalse(StoredSID.exists("testSID"));
        StoredSID.addSID("testSID", user);
        Assert.assertTrue(StoredSID.exists("testSID"));
        Assert.assertFalse(StoredSID.exists("otherSID"));
        Assert.assertFalse(StoredSID.exists(null));
    }

    @Test
    public void getUser() {
        Assert.assertFalse(StoredSID.exists("testSID"));
        StoredSID.addSID("testSID", user);
        Assert.assertEquals(user, StoredSID.getUser("testSID"));
        Assert.assertNull(StoredSID.getUser("otherSID"));
    }
}