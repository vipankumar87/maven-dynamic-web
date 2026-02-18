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

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Consume flash messages from session
        transferFlash(req);

        UserStore store = UserStore.getInstance();
        List<User> users = store.findAll();

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

        UserStore store = UserStore.getInstance();
        User currentUser = (User) req.getSession().getAttribute("user");

        switch (action) {
            case "delete" -> {
                if (currentUser != null && userId.equals(currentUser.getId())) {
                    req.getSession().setAttribute("flash_error",
                            "You cannot delete your own account.");
                } else {
                    boolean deleted = store.deleteById(userId);
                    req.getSession().setAttribute("flash_success",
                            deleted ? "User deleted successfully." : "User not found.");
                }
            }
            case "toggleRole" -> {
                User target = store.findById(userId);
                if (target == null) {
                    req.getSession().setAttribute("flash_error", "User not found.");
                } else if (currentUser != null && userId.equals(currentUser.getId())) {
                    req.getSession().setAttribute("flash_error",
                            "You cannot change your own role.");
                } else {
                    String oldRole = target.getRole();
                    store.toggleRole(target);
                    req.getSession().setAttribute("flash_success",
                            target.getName() + " is now " + target.getRole() + ".");
                }
            }
            default -> req.getSession().setAttribute("flash_error", "Unknown action.");
        }

        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }

    /** Move flash attributes from session scope to request scope. */
    private void transferFlash(HttpServletRequest req) {
        var session = req.getSession(false);
        if (session == null) return;

        Object success = session.getAttribute("flash_success");
        if (success != null) {
            req.setAttribute("success", success);
            session.removeAttribute("flash_success");
        }
        Object error = session.getAttribute("flash_error");
        if (error != null) {
            req.setAttribute("error", error);
            session.removeAttribute("flash_error");
        }
    }
}
