package com.rudracomputer.webblog.servlet;

import com.rudracomputer.webblog.model.User;
import com.rudracomputer.webblog.util.UserStore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String token = req.getParameter("token");

        if (!isValidToken(token)) {
            req.setAttribute("tokenInvalid", true);
        }
        req.getRequestDispatcher("/account/reset-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String token           = req.getParameter("token");
        String password        = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        if (!isValidToken(token)) {
            req.setAttribute("tokenInvalid", true);
            req.getRequestDispatcher("/account/reset-password.jsp").forward(req, resp);
            return;
        }

        User user = UserStore.getInstance().findByResetToken(token);

        // Check token expiry
        if (user.getResetTokenExpiry() != null
                && LocalDateTime.now().isAfter(user.getResetTokenExpiry())) {
            UserStore.getInstance().clearResetToken(user);
            req.setAttribute("tokenInvalid", true);
            req.getRequestDispatcher("/account/reset-password.jsp").forward(req, resp);
            return;
        }

        // Validate new password
        if (password == null || password.length() < 6) {
            req.setAttribute("error", "Password must be at least 6 characters.");
            req.getRequestDispatcher("/account/reset-password.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("error", "Passwords do not match.");
            req.getRequestDispatcher("/account/reset-password.jsp").forward(req, resp);
            return;
        }

        // Update password and clear token
        UserStore.getInstance().updatePassword(user, password);

        // Flash success and redirect to login
        req.getSession(true).setAttribute("flash_success",
                "Password reset successful! Please sign in with your new password.");
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private boolean isValidToken(String token) {
        if (token == null || token.isBlank()) return false;
        User user = UserStore.getInstance().findByResetToken(token);
        return user != null;
    }
}
