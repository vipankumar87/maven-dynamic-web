package com.rudracomputer.webblog.servlet;

import com.rudracomputer.webblog.dao.UserDAO;
import com.rudracomputer.webblog.dao.mybatis.MyBatisUserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new MyBatisUserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getSession(false) != null && req.getSession(false).getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/"); return;
        }
        req.getRequestDispatcher("/account/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String name            = trim(req.getParameter("name"));
        String email           = trim(req.getParameter("email"));
        String password        = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        if (name.isEmpty() || email.isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("/account/register.jsp").forward(req, resp); return;
        }
        if (name.length() < 2) {
            req.setAttribute("error", "Name must be at least 2 characters.");
            req.getRequestDispatcher("/account/register.jsp").forward(req, resp); return;
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            req.setAttribute("error", "Please enter a valid email address.");
            req.getRequestDispatcher("/account/register.jsp").forward(req, resp); return;
        }
        if (password.length() < 6) {
            req.setAttribute("error", "Password must be at least 6 characters.");
            req.getRequestDispatcher("/account/register.jsp").forward(req, resp); return;
        }
        if (!password.equals(confirmPassword)) {
            req.setAttribute("error", "Passwords do not match.");
            req.getRequestDispatcher("/account/register.jsp").forward(req, resp); return;
        }
        if (userDAO.existsByEmail(email.toLowerCase())) {
            req.setAttribute("error", "An account with this email already exists.");
            req.getRequestDispatcher("/account/register.jsp").forward(req, resp); return;
        }

        userDAO.create(name, email.toLowerCase(), password);
        req.getSession(true).setAttribute("flash_success", "Account created! You can now sign in.");
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private String trim(String s) { return (s == null) ? "" : s.trim(); }
}
