package com.rudracomputer.webblog.servlet.admin;

import com.rudracomputer.webblog.dao.UserDAO;
import com.rudracomputer.webblog.dao.jdbc.JdbcUserDAO;
import com.rudracomputer.webblog.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {

    private final UserDAO userDAO = new JdbcUserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        transferFlash(req);

        List<User> users = userDAO.findAll();
        req.setAttribute("users",      users);
        req.setAttribute("totalUsers", users.size());
        req.getRequestDispatcher("/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        String userId = req.getParameter("userId");

        if (action == null || userId == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/users");
            return;
        }

        User currentUser = (User) req.getSession().getAttribute("user");

        switch (action) {
            case "delete" -> {
                if (currentUser != null && userId.equals(currentUser.getId())) {
                    req.getSession().setAttribute("flash_error", "You cannot delete your own account.");
                } else {
                    boolean deleted = userDAO.deleteById(userId);
                    req.getSession().setAttribute("flash_success",
                            deleted ? "User deleted successfully." : "User not found.");
                }
            }
            case "toggleRole" -> {
                Optional<User> target = userDAO.findById(userId);
                if (target.isEmpty()) {
                    req.getSession().setAttribute("flash_error", "User not found.");
                } else if (currentUser != null && userId.equals(currentUser.getId())) {
                    req.getSession().setAttribute("flash_error", "You cannot change your own role.");
                } else {
                    userDAO.toggleRole(target.get());
                    req.getSession().setAttribute("flash_success",
                            target.get().getName() + " is now " + target.get().getRole() + ".");
                }
            }
            default -> req.getSession().setAttribute("flash_error", "Unknown action.");
        }

        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }

    private void transferFlash(HttpServletRequest req) {
        var session = req.getSession(false);
        if (session == null) return;
        Object success = session.getAttribute("flash_success");
        if (success != null) { req.setAttribute("success", success); session.removeAttribute("flash_success"); }
        Object error = session.getAttribute("flash_error");
        if (error != null) { req.setAttribute("error", error); session.removeAttribute("flash_error"); }
    }
}
