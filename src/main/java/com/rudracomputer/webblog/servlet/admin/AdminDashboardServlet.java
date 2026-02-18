package com.rudracomputer.webblog.servlet.admin;

import com.rudracomputer.webblog.model.User;
import com.rudracomputer.webblog.util.UserStore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserStore store = UserStore.getInstance();
        List<User> allUsers = store.findAll();

        // Stats
        req.setAttribute("totalUsers",        allUsers.size());
        req.setAttribute("totalAdmins",        store.countByRole("ADMIN"));
        req.setAttribute("totalRegularUsers",  store.countByRole("USER"));
        req.setAttribute("newUsersToday",      store.countNewToday());

        // Recent 5 users (last added)
        int from = Math.max(0, allUsers.size() - 5);
        List<User> recent = allUsers.subList(from, allUsers.size());
        req.setAttribute("recentUsers", recent);

        req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
    }
}
