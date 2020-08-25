package com.garden;

import com.garden.beans.User;

import java.util.HashMap;
import java.util.Map;

public class StoredSID {
    private static Map<String, User> mapSID = new HashMap<>();

    public static boolean exists(String sid) {
        return mapSID.containsKey(sid);
    }

    public static void addSID(String sid, User user) {
        if (!exists(sid))
            mapSID.put(sid, user);
        System.out.println("SIDs list: " + mapSID);
    }

    public static User getUser(String sid) {
        return mapSID.get(sid);
    }

    public static void clear(){
        mapSID.clear();
    }
}
