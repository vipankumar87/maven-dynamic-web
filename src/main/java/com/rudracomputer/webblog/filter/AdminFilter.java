package com.rudracomputer.webblog.filter;

import com.rudracomputer.webblog.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Guards every URL under /admin/*.
 * Unauthenticated users are redirected to /login.
 * Authenticated non-admins are redirected to home.
 */
@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Not logged in
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!"ADMIN".equals(user.getRole())) {
            // Logged in but not admin
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
