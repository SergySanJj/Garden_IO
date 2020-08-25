package com.garden.endpoints;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class APITest {
    @Test
    public void noMethodsInAPI() {
        Assert.assertEquals(0,
                API.class.getMethods().length - API.class.getSuperclass().getMethods().length);
    }
}