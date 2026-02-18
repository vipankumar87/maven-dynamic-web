package com.rudracomputer.webblog.servlet;

import com.rudracomputer.webblog.model.User;
import com.rudracomputer.webblog.util.PasswordUtils;
import com.rudracomputer.webblog.util.UserStore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Already logged in -> redirect home
        if (req.getSession(false) != null && req.getSession(false).getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        // Transfer flash messages from session to request scope
        var session = req.getSession(false);
        if (session != null) {
            Object flash = session.getAttribute("flash_success");
            if (flash != null) {
                req.setAttribute("success", flash);
                session.removeAttribute("flash_success");
            }
        }
        req.getRequestDispatcher("/account/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        // Basic validation
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("error", "Email and password are required.");
            req.getRequestDispatcher("/account/login.jsp").forward(req, resp);
            return;
        }

        User user = UserStore.getInstance().findByEmail(email.trim().toLowerCase());

        if (user == null || !PasswordUtils.verify(password, user.getPasswordHash())) {
            req.setAttribute("error", "Invalid email or password.");
            req.getRequestDispatcher("/account/login.jsp").forward(req, resp);
            return;
        }

        // Create session
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(30 * 60); // 30 min

        // Redirect admin to admin panel, others to home
        if ("ADMIN".equals(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
}
