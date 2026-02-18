<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty pageTitle ? pageTitle : 'Home'} | WebBlog</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">

    <!-- Navbar -->
    <nav class="bg-white border-b border-gray-200 sticky top-0 z-50 shadow-sm">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex justify-between items-center h-16">

                <!-- Logo -->
                <a href="${pageContext.request.contextPath}/" class="flex items-center gap-2">
                    <div class="w-8 h-8 bg-indigo-600 rounded-lg flex items-center justify-center">
                        <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"/>
                        </svg>
                    </div>
                    <span class="text-xl font-bold text-gray-900">WebBlog</span>
                </a>

                <!-- Nav links & user menu -->
                <div class="flex items-center gap-4">
                    <% if (session.getAttribute("user") != null) {
                           com.rudracomputer.webblog.model.User u = (com.rudracomputer.webblog.model.User) session.getAttribute("user"); %>
                        <span class="text-sm text-gray-500 hidden sm:inline">Hi, <%= u.getName() %></span>
                        <% if ("ADMIN".equals(u.getRole())) { %>
                            <a href="${pageContext.request.contextPath}/admin/dashboard"
                               class="text-sm text-indigo-600 hover:text-indigo-700 font-medium transition">Admin</a>
                        <% } %>
                        <a href="${pageContext.request.contextPath}/logout"
                           class="text-sm bg-gray-100 hover:bg-gray-200 text-gray-700 px-4 py-2 rounded-lg transition font-medium">
                            Logout
                        </a>
                    <% } else { %>
                        <a href="${pageContext.request.contextPath}/login"
                           class="text-sm text-gray-600 hover:text-gray-800 font-medium transition">Login</a>
                        <a href="${pageContext.request.contextPath}/register"
                           class="text-sm bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg transition font-medium shadow-sm">
                            Get Started
                        </a>
                    <% } %>
                </div>

            </div>
        </div>
    </nav>

    <!-- Page content -->
    <main class="flex-1">
