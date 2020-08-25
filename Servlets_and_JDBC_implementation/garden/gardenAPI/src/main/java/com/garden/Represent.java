package com.garden;

import com.garden.endpoints.API;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class Represent<E> {
    private Class<E> typeHost;

    public Represent(Class<E> typeHost) {
        this.typeHost = typeHost;
    }

    public String asOrderOperatable(List<E> items, HttpServletRequest request, String endpoint)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        return customTable(items,
                (item, itemMethods) -> {
                    StringBuilder row = new StringBuilder();
                    for (Method method : itemMethods) {
                        try {
                            Object res = method.invoke(item);
                            if (res == null) {
                                row.append(Represent.wrapItem("null"));
                            } else {
                                row.append(Represent.wrapItem(res.toString()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    row.append("<form action=\"").
                            append(request.getContextPath()).append("/api/").append(endpoint).
                            append("\"method=\"post\" >").
                            append("<input type=\"hidden\" name=\"").append(API.ApproveOrder.Params.ORDERID).
                            append("\"value=\"").append(itemMethods.get(0).invoke(item)).append("\">").
                            append("<input type=\"submit\" value=\"Check\">").
                            append("</form>");
                    return row.toString();
                });
    }

    public String customTable(List<E> items, ItemRepresentation<E> itemRepresentation)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        List<Method> fields = formFields();
        StringBuilder tableRepresentation = new StringBuilder();
        StringBuilder rowRepresentation = new StringBuilder();
        for (Method method : fields) {
            rowRepresentation.append(wrapItem(extractItemName(method)));
        }
        tableRepresentation.append(wrapHeaderRow(rowRepresentation.toString()));

        for (E item : items) {
            rowRepresentation = new StringBuilder();
            rowRepresentation.append(itemRepresentation.represent(item, fields));
            tableRepresentation.append(wrapRow(rowRepresentation.toString()));
        }

        return wrapTable(tableRepresentation.toString());
    }

    public String asTable(List<E> items)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        return customTable(items, (item, itemMethods) -> {
            StringBuilder row = new StringBuilder();
            for (Method method : itemMethods) {
                try {
                    Object res = method.invoke(item);
                    if (res == null) {
                        row.append(wrapItem("null"));
                    } else {
                        row.append(wrapItem(res.toString()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return row.toString();
        });
    }

    // Puts id getter on the first place in returned field names list
    private List<Method> formFields() throws IntrospectionException {
        LinkedList<Method> res = new LinkedList<>();
        Method idMethod = null;
        for (PropertyDescriptor propertyDescriptor :
                Introspector.getBeanInfo(typeHost, Object.class).getPropertyDescriptors()) {
            if (propertyDescriptor.getReadMethod().getName().equals("getId"))
                idMethod = propertyDescriptor.getReadMethod();
            else
                res.addLast(propertyDescriptor.getReadMethod());
        }
        res.addFirst(idMethod);
        return res;
    }

    private static String extractItemName(Method getter) {
        String methodName = getter.getName();
        if (methodName.startsWith("get"))
            return methodName.substring(3);
        else if (methodName.startsWith("is"))
            return methodName.substring(2);
        else
            return methodName;
    }

    public static String wrapItem(String itemRepresentation) {
        return "<p style=\"" +
                "display: flex; " +
                "flex-direction: row; " +
                "min-width: 200px; " +
                "max-width: 200px; " +
                "\">" + itemRepresentation + "</p>";
    }

    private static String wrapHeaderRow(String rowRepresentation) {
        return "<div style=\"" +
                "display: flex; " +
                "flex-direction: row;  " +
                "background-color: #5f5f5f; " +
                "color: #f1f1f1; " +
                "\">" + rowRepresentation + "</div>";
    }

    private static String wrapRow(String rowRepresentation) {
        return "<div style=\"" +
                "display: flex; " +
                "flex-direction: row; " +
                "background-color: #f1f1f1; " +
                "border-bottom: 1px solid #aaa; " +
                "\" \">" + rowRepresentation + "</div>";
    }

    private String wrapTable(String tableRepresentation) {
        return "<div style=\"display: flex; flex-direction: column; margin: 10px; \">" + tableRepresentation + "</div>";
    }
}
