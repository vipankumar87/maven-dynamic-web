package com.rudracomputer.webblog.servlet.admin;

import com.rudracomputer.webblog.dao.UserDAO;
import com.rudracomputer.webblog.dao.jpa.JpaUserDAO;
import com.rudracomputer.webblog.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final UserDAO userDAO = new JpaUserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<User> allUsers = userDAO.findAll();
        int from = Math.max(0, allUsers.size() - 5);

        req.setAttribute("totalUsers",       allUsers.size());
        req.setAttribute("totalAdmins",       userDAO.countByRole("ADMIN"));
        req.setAttribute("totalRegularUsers", userDAO.countByRole("USER"));
        req.setAttribute("newUsersToday",     userDAO.countNewToday());
        req.setAttribute("recentUsers",       allUsers.subList(from, allUsers.size()));

        req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
    }
}
