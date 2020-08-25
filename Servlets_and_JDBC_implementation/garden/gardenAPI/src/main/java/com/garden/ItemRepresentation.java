package com.garden;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public interface ItemRepresentation<E>{
    String represent(E item, List<Method> itemMethods) throws InvocationTargetException, IllegalAccessException;
}
