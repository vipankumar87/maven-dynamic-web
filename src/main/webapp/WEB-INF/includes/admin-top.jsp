<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty pageTitle ? pageTitle : 'Admin'} | WebBlog Admin</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">

<div class="flex h-screen overflow-hidden">

    <!-- ===== Sidebar ===== -->
    <aside class="w-64 bg-gray-900 text-white flex flex-col shrink-0">

        <!-- Brand -->
        <div class="px-6 py-5 border-b border-gray-700/50">
            <a href="${pageContext.request.contextPath}/" class="flex items-center gap-2.5">
                <div class="w-8 h-8 bg-indigo-500 rounded-lg flex items-center justify-center shadow">
                    <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"/>
                    </svg>
                </div>
                <div>
                    <span class="text-base font-bold">WebBlog</span>
                    <p class="text-xs text-gray-400 leading-none mt-0.5">Admin Portal</p>
                </div>
            </a>
        </div>

        <!-- Nav links -->
        <nav class="flex-1 px-3 py-4 space-y-0.5 overflow-y-auto">
            <p class="text-xs font-semibold text-gray-500 uppercase tracking-wider px-3 mb-2">Main</p>

            <a href="${pageContext.request.contextPath}/admin/dashboard"
               class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition
                      <%= "dashboard".equals(request.getAttribute("pageActive")) ? "bg-indigo-600 text-white shadow-sm" : "text-gray-300 hover:bg-gray-800 hover:text-white" %>">
                <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/>
                </svg>
                Dashboard
            </a>

            <a href="${pageContext.request.contextPath}/admin/users"
               class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition
                      <%= "users".equals(request.getAttribute("pageActive")) ? "bg-indigo-600 text-white shadow-sm" : "text-gray-300 hover:bg-gray-800 hover:text-white" %>">
                <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"/>
                </svg>
                Users
            </a>

            <p class="text-xs font-semibold text-gray-500 uppercase tracking-wider px-3 mt-5 mb-2">Site</p>

            <a href="${pageContext.request.contextPath}/"
               class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium text-gray-300 hover:bg-gray-800 hover:text-white transition">
                <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"/>
                </svg>
                View Site
            </a>
        </nav>

        <!-- User profile at bottom -->
        <% com.rudracomputer.webblog.model.User adminUser =
               (com.rudracomputer.webblog.model.User) session.getAttribute("user"); %>
        <div class="px-4 py-4 border-t border-gray-700/50">
            <div class="flex items-center gap-3">
                <div class="w-9 h-9 bg-indigo-500 rounded-full flex items-center justify-center text-sm font-bold shrink-0">
                    <%= adminUser != null ? adminUser.getName().substring(0,1).toUpperCase() : "A" %>
                </div>
                <div class="flex-1 min-w-0">
                    <p class="text-sm font-medium text-white truncate">
                        <%= adminUser != null ? adminUser.getName() : "Admin" %>
                    </p>
                    <p class="text-xs text-gray-400 truncate">
                        <%= adminUser != null ? adminUser.getEmail() : "" %>
                    </p>
                </div>
                <a href="${pageContext.request.contextPath}/logout"
                   title="Logout"
                   class="text-gray-400 hover:text-white transition shrink-0">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/>
                    </svg>
                </a>
            </div>
        </div>

    </aside>

    <!-- ===== Main area ===== -->
    <div class="flex-1 flex flex-col overflow-hidden">

        <!-- Top header bar -->
        <header class="bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between shrink-0 shadow-sm">
            <h1 class="text-xl font-semibold text-gray-800">${not empty pageTitle ? pageTitle : 'Admin'}</h1>
            <div class="flex items-center gap-3">
                <span class="text-sm text-gray-500 hidden sm:inline">
                    <%= adminUser != null ? adminUser.getEmail() : "" %>
                </span>
                <span class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-semibold bg-indigo-100 text-indigo-700">
                    Admin
                </span>
            </div>
        </header>

        <!-- Page content -->
        <main class="flex-1 overflow-y-auto p-6">
