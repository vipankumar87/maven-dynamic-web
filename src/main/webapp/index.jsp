<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Home" />
<%@ include file="/WEB-INF/includes/main-top.jsp" %>

        <!-- Hero -->
        <section class="bg-gradient-to-br from-indigo-600 to-purple-700 text-white">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 text-center">
                <span class="inline-block bg-white/20 text-white text-xs font-semibold px-3 py-1 rounded-full mb-6 tracking-wide uppercase">
                    Welcome to WebBlog
                </span>
                <h1 class="text-4xl sm:text-5xl font-extrabold mb-4 leading-tight">
                    Share Your Stories<br>with the World
                </h1>
                <p class="text-indigo-100 text-lg max-w-xl mx-auto mb-8">
                    A modern blogging platform built with Jakarta EE. Write, publish, and connect with readers everywhere.
                </p>
                <div class="flex flex-col sm:flex-row gap-3 justify-center">
                    <% if (session.getAttribute("user") == null) { %>
                        <a href="${pageContext.request.contextPath}/register"
                           class="bg-white text-indigo-700 font-semibold px-6 py-3 rounded-xl hover:bg-indigo-50 transition shadow-md">
                            Get Started Free
                        </a>
                        <a href="${pageContext.request.contextPath}/login"
                           class="border border-white/40 text-white font-medium px-6 py-3 rounded-xl hover:bg-white/10 transition">
                            Sign In
                        </a>
                    <% } else { %>
                        <a href="${pageContext.request.contextPath}/admin/dashboard"
                           class="bg-white text-indigo-700 font-semibold px-6 py-3 rounded-xl hover:bg-indigo-50 transition shadow-md">
                            Go to Dashboard
                        </a>
                    <% } %>
                </div>
            </div>
        </section>

        <!-- Features -->
        <section class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
            <h2 class="text-2xl font-bold text-center text-gray-800 mb-10">Why WebBlog?</h2>
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-8">

                <div class="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 text-center">
                    <div class="w-12 h-12 bg-indigo-100 rounded-xl flex items-center justify-center mx-auto mb-4">
                        <svg class="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                        </svg>
                    </div>
                    <h3 class="font-semibold text-gray-800 mb-2">Easy Publishing</h3>
                    <p class="text-sm text-gray-500">Write and publish posts with a clean, distraction-free editor.</p>
                </div>

                <div class="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 text-center">
                    <div class="w-12 h-12 bg-purple-100 rounded-xl flex items-center justify-center mx-auto mb-4">
                        <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"/>
                        </svg>
                    </div>
                    <h3 class="font-semibold text-gray-800 mb-2">Secure &amp; Private</h3>
                    <p class="text-sm text-gray-500">Your data is protected with industry-standard security practices.</p>
                </div>

                <div class="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 text-center">
                    <div class="w-12 h-12 bg-green-100 rounded-xl flex items-center justify-center mx-auto mb-4">
                        <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
                        </svg>
                    </div>
                    <h3 class="font-semibold text-gray-800 mb-2">Community</h3>
                    <p class="text-sm text-gray-500">Join a growing community of writers and readers around the globe.</p>
                </div>

            </div>
        </section>

        <!-- CTA -->
        <% if (session.getAttribute("user") == null) { %>
        <section class="bg-indigo-600 text-white py-14">
            <div class="max-w-2xl mx-auto px-4 text-center">
                <h2 class="text-2xl font-bold mb-3">Ready to start writing?</h2>
                <p class="text-indigo-200 mb-6">Create your free account and publish your first post today.</p>
                <a href="${pageContext.request.contextPath}/register"
                   class="inline-block bg-white text-indigo-700 font-semibold px-8 py-3 rounded-xl hover:bg-indigo-50 transition shadow-md">
                    Create Free Account
                </a>
            </div>
        </section>
        <% } %>

<%@ include file="/WEB-INF/includes/main-bottom.jsp" %>
