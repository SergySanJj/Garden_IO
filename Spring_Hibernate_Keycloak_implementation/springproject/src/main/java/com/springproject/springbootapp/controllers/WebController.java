package com.springproject.springbootapp.controllers;

import com.springproject.springbootapp.GardenRoles;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;

@Controller
public class WebController {
    @GetMapping(path = "/")
    public ModelAndView index(Map<String, Object> model, Principal principal) {
        String name = principal.getName();
        String role = roleMapper(SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        model.put("user_name", name);
        model.put("role_map", role);

        if (!new UserController().userExists(name)) {
            new UserController().addUser(name, role);
        }

        return new ModelAndView("index");
    }

    public String roleMapper(Collection<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority grantedAuthority : authorities) {
            switch (grantedAuthority.getAuthority()) {
                case GardenRoles.admin:
                    return "admin";
                case GardenRoles.gardener:
                    return "gardener";
                case GardenRoles.manager:
                    return "manager";
                case GardenRoles.owner:
                    return "owner";
                default:
                    break;
            }
        }
        return "";
    }

    @GetMapping(path = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        request.logout();
        response.sendRedirect(request.getContextPath());
    }

    public String getFrom(String url) {
        HttpGet request = new HttpGet(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            return EntityUtils.toString(response.getEntity());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Empty";
    }


}
