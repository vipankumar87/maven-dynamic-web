package com.rudracomputer.webblog.servlet;

import com.rudracomputer.webblog.model.User;
import com.rudracomputer.webblog.util.PasswordUtils;
import com.rudracomputer.webblog.util.UserStore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

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
        User user = UserStore.getInstance().findByEmail(email);

        if (user == null) {
            // Don't reveal whether the email exists (security best practice)
            req.setAttribute("resetLink",
                    buildResetLink(req, "demo-token-email-not-found"));
            req.getRequestDispatcher("/account/forgot-password.jsp").forward(req, resp);
            return;
        }

        // Generate reset token
        String token = PasswordUtils.generateToken();
        UserStore.getInstance().setResetToken(user, token);

        // In production: send email with reset link
        // For demo: display the link on screen
        String resetLink = buildResetLink(req, token);
        req.setAttribute("resetLink", resetLink);
        req.getRequestDispatcher("/account/forgot-password.jsp").forward(req, resp);
    }

    private String buildResetLink(HttpServletRequest req, String token) {
        String scheme = req.getScheme();
        String host   = req.getServerName();
        int    port   = req.getServerPort();
        String ctx    = req.getContextPath();

        String portStr = (port == 80 || port == 443) ? "" : ":" + port;
        return scheme + "://" + host + portStr + ctx + "/reset-password?token=" + token;
    }
}
