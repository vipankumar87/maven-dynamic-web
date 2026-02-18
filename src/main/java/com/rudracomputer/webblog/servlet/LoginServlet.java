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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new JdbcUserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getSession(false) != null && req.getSession(false).getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
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

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("error", "Email and password are required.");
            req.getRequestDispatcher("/account/login.jsp").forward(req, resp);
            return;
        }

        Optional<User> opt = userDAO.findByEmail(email.trim().toLowerCase());

        if (opt.isEmpty() || !PasswordUtils.verify(password, opt.get().getPasswordHash())) {
            req.setAttribute("error", "Invalid email or password.");
            req.getRequestDispatcher("/account/login.jsp").forward(req, resp);
            return;
        }

        User user = opt.get();
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(30 * 60);

        if ("ADMIN".equals(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
}
