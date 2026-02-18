package com.rudracomputer.webblog.servlet;

import com.rudracomputer.webblog.dao.UserDAO;
import com.rudracomputer.webblog.dao.jpa.JpaUserDAO;
import com.rudracomputer.webblog.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new JpaUserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!isValidToken(req.getParameter("token"))) {
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

        User user = userDAO.findByResetToken(token).get();

        if (user.getResetTokenExpiry() != null
                && LocalDateTime.now().isAfter(user.getResetTokenExpiry())) {
            userDAO.clearResetToken(user);
            req.setAttribute("tokenInvalid", true);
            req.getRequestDispatcher("/account/reset-password.jsp").forward(req, resp);
            return;
        }

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

        userDAO.updatePassword(user, password);
        req.getSession(true).setAttribute("flash_success",
                "Password reset successful! Please sign in with your new password.");
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private boolean isValidToken(String token) {
        if (token == null || token.isBlank()) return false;
        return userDAO.findByResetToken(token).isPresent();
    }
}
