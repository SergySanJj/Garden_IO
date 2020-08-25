package com.garden.endpoints;

import com.garden.StoredSID;
import com.garden.beans.User;

import javax.servlet.http.HttpServletRequest;

public class RoleValidator {
    public static boolean validate(HttpServletRequest req, String role){
        User user = StoredSID.getUser((String) req.getSession().getAttribute("SID"));

        return user.getRole().equals(role);
    }
}
