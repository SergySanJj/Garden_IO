package com.garden;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebFilter("/AuthenticationFilter")
public class FrontEndAuthenticationFilter implements Filter {
    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;

    private List<String> ignoreURIs = List.of("/login", "/api/auth");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        httpRequest = (HttpServletRequest) request;
        httpResponse = (HttpServletResponse) response;
        System.out.println(httpRequest.getRequestURL());
        if (isLoggedIn(httpRequest)) {
            chain.doFilter(request, response);
        } else if (ignoreURIs.contains(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }

    public static boolean isLoggedIn(HttpServletRequest req) {
        return (req.getSession() != null) &&
                (req.getSession().getAttribute("SID") != null) &&
                (StoredSID.exists((String) req.getSession().getAttribute("SID")));
    }

    public FrontEndAuthenticationFilter() {
    }

    public void destroy() {
    }

    public void init(FilterConfig fConfig) throws ServletException {
    }

}