package com.rudracomputer.webblog.servlet;

import com.rudracomputer.webblog.dao.UserDAO;
import com.rudracomputer.webblog.dao.jdbc.JdbcUserDAO;
import com.rudracomputer.webblog.model.User;
import com.rudracomputer.webblog.util.PasswordUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new JdbcUserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/account/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        if (email == null || email.isBlank()) {
            req.setAttribute("error", "Please enter your email address.");
            req.getRequestDispatcher("/account/forgot-password.jsp").forward(req, resp);
            return;
        }

        email = email.trim().toLowerCase();
        Optional<User> opt = userDAO.findByEmail(email);

        // Always show a reset link (don't reveal whether email exists)
        String token = opt.isPresent() ? PasswordUtils.generateToken() : "demo-token-not-found";
        opt.ifPresent(u -> userDAO.setResetToken(u, token));

        req.setAttribute("resetLink", buildResetLink(req, token));
        req.getRequestDispatcher("/account/forgot-password.jsp").forward(req, resp);
    }

    private String buildResetLink(HttpServletRequest req, String token) {
        int port = req.getServerPort();
        String portStr = (port == 80 || port == 443) ? "" : ":" + port;
        return req.getScheme() + "://" + req.getServerName() + portStr
                + req.getContextPath() + "/reset-password?token=" + token;
    }
}
