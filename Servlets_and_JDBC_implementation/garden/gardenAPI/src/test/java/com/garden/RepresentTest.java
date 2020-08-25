package com.garden;

import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RepresentTest {
    class TestObject {
        public Integer id;
        public String StringValue;
        public Integer IntegerValue;
        public Boolean BooleanValue;

        public TestObject(Integer id, String stringValue, Integer integerValue, Boolean booleanValue) {
            this.id = id;
            StringValue = stringValue;
            IntegerValue = integerValue;
            BooleanValue = booleanValue;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getStringValue() {
            return StringValue;
        }

        public void setStringValue(String stringValue) {
            StringValue = stringValue;
        }

        public Integer getIntegerValue() {
            return IntegerValue;
        }

        public void setIntegerValue(Integer integerValue) {
            IntegerValue = integerValue;
        }

        public Boolean getBooleanValue() {
            return BooleanValue;
        }

        public void setBooleanValue(Boolean booleanValue) {
            BooleanValue = booleanValue;
        }
    }

    @Test
    public void asTable() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        int n = 10;
        List<TestObject> testObjectsList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            testObjectsList.add(generateObject(i));
        }
        Represent<TestObject> representer = new Represent<>(TestObject.class);
        String tableRepresentation = representer.asTable(testObjectsList);

        // Check header
        Assert.assertTrue(tableRepresentation.contains("Id"));
        Assert.assertTrue(tableRepresentation.contains("StringValue"));
        Assert.assertTrue(tableRepresentation.contains("IntegerValue"));
        Assert.assertTrue(tableRepresentation.contains("BooleanValue"));

        // Check table data
        for (int i = 0; i < n; i++) {
            Assert.assertTrue(tableRepresentation.contains(Integer.toString(i)));
            Assert.assertTrue(tableRepresentation.contains("string" + i));
        }
    }

    @Test
    public void asTableEmptyData() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        List<TestObject> testObjectsList = new ArrayList<>();
        Represent<TestObject> representer = new Represent<>(TestObject.class);
        String tableRepresentation = representer.asTable(testObjectsList);

        // Check header
        Assert.assertTrue(tableRepresentation.contains("Id"));
        Assert.assertTrue(tableRepresentation.contains("StringValue"));
        Assert.assertTrue(tableRepresentation.contains("IntegerValue"));
        Assert.assertTrue(tableRepresentation.contains("BooleanValue"));
    }

    @Test
    public void wrapItem() {
        String itemData = "itemData";

        String represented = Represent.wrapItem(itemData);
        Assert.assertNotNull(represented);
        Assert.assertTrue(represented.contains(itemData));
    }

    @Test
    public void asOrderOperatable() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        int n = 10;
        List<TestObject> testObjectsList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            testObjectsList.add(generateObject(i));
        }
        Represent<TestObject> representer = new Represent<>(TestObject.class);
        String tableRepresentation = representer.asOrderOperatable(testObjectsList, request, "test-view-endpoint");

        // Check header
        Assert.assertTrue(tableRepresentation.contains("Id"));
        Assert.assertTrue(tableRepresentation.contains("StringValue"));
        Assert.assertTrue(tableRepresentation.contains("IntegerValue"));
        Assert.assertTrue(tableRepresentation.contains("BooleanValue"));

        // Check action for each row
        Assert.assertEquals(n, countOccurrences(tableRepresentation, "<form"));
    }

    private TestObject generateObject(int id) {
        return new TestObject(id, "string" + id, id, true);
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