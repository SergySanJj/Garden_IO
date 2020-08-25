package com.garden;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SIDMakerTest {

    @Test
    public void make() {
        String login1 = "log1";
        String password1 = "pas1";

        String login2 = "log2";
        String password2 = "pas2";

        Assert.assertNotEquals(SIDMaker.make(login1, password1), SIDMaker.make(login2, password2));
        Assert.assertEquals(SIDMaker.make(login1, password1), SIDMaker.make(login1, password1));
        Assert.assertEquals(SIDMaker.make("", ""), SIDMaker.make("", ""));
    }
}