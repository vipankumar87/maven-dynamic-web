<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Dashboard" />
<% request.setAttribute("pageActive", "dashboard"); %>
<%@ include file="/WEB-INF/includes/admin-top.jsp" %>

            <!-- Stats cards -->
            <div class="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-5 mb-8">

                <div class="bg-white rounded-2xl p-5 shadow-sm border border-gray-100">
                    <div class="flex items-center justify-between mb-3">
                        <p class="text-xs font-semibold text-gray-500 uppercase tracking-wide">Total Users</p>
                        <div class="w-9 h-9 bg-indigo-100 rounded-xl flex items-center justify-center">
                            <svg class="w-5 h-5 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
                            </svg>
                        </div>
                    </div>
                    <p class="text-3xl font-bold text-gray-900">${totalUsers}</p>
                    <p class="text-xs text-gray-400 mt-1">Registered accounts</p>
                </div>

                <div class="bg-white rounded-2xl p-5 shadow-sm border border-gray-100">
                    <div class="flex items-center justify-between mb-3">
                        <p class="text-xs font-semibold text-gray-500 uppercase tracking-wide">Admins</p>
                        <div class="w-9 h-9 bg-purple-100 rounded-xl flex items-center justify-center">
                            <svg class="w-5 h-5 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/>
                            </svg>
                        </div>
                    </div>
                    <p class="text-3xl font-bold text-gray-900">${totalAdmins}</p>
                    <p class="text-xs text-gray-400 mt-1">Admin accounts</p>
                </div>

                <div class="bg-white rounded-2xl p-5 shadow-sm border border-gray-100">
                    <div class="flex items-center justify-between mb-3">
                        <p class="text-xs font-semibold text-gray-500 uppercase tracking-wide">Regular Users</p>
                        <div class="w-9 h-9 bg-blue-100 rounded-xl flex items-center justify-center">
                            <svg class="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                            </svg>
                        </div>
                    </div>
                    <p class="text-3xl font-bold text-gray-900">${totalRegularUsers}</p>
                    <p class="text-xs text-gray-400 mt-1">Standard accounts</p>
                </div>

                <div class="bg-white rounded-2xl p-5 shadow-sm border border-gray-100">
                    <div class="flex items-center justify-between mb-3">
                        <p class="text-xs font-semibold text-gray-500 uppercase tracking-wide">New (Today)</p>
                        <div class="w-9 h-9 bg-green-100 rounded-xl flex items-center justify-center">
                            <svg class="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M12 4v16m8-8H4"/>
                            </svg>
                        </div>
                    </div>
                    <p class="text-3xl font-bold text-gray-900">${newUsersToday}</p>
                    <p class="text-xs text-gray-400 mt-1">Signups today</p>
                </div>

            </div>

            <!-- Recent Users table -->
            <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                <div class="flex items-center justify-between px-6 py-4 border-b border-gray-100">
                    <h2 class="text-base font-semibold text-gray-800">Recent Users</h2>
                    <a href="${pageContext.request.contextPath}/admin/users"
                       class="text-xs text-indigo-600 hover:text-indigo-700 font-medium transition">View all &rarr;</a>
                </div>

                <div class="overflow-x-auto">
                    <table class="w-full text-sm">
                        <thead>
                            <tr class="text-left bg-gray-50">
                                <th class="px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wide">User</th>
                                <th class="px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wide">Email</th>
                                <th class="px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wide">Role</th>
                                <th class="px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wide">Joined</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-50">
                            <c:forEach var="u" items="${recentUsers}">
                                <tr class="hover:bg-gray-50/50 transition">
                                    <td class="px-6 py-4">
                                        <div class="flex items-center gap-3">
                                            <div class="w-8 h-8 rounded-full bg-indigo-100 flex items-center justify-center text-xs font-bold text-indigo-600">
                                                ${u.name.substring(0,1).toUpperCase()}
                                            </div>
                                            <span class="font-medium text-gray-800">
                                                <c:out value="${u.name}" />
                                            </span>
                                        </div>
                                    </td>
                                    <td class="px-6 py-4 text-gray-500">
                                        <c:out value="${u.email}" />
                                    </td>
                                    <td class="px-6 py-4">
                                        <c:choose>
                                            <c:when test="${u.role eq 'ADMIN'}">
                                                <span class="inline-flex px-2 py-0.5 rounded-full text-xs font-semibold bg-purple-100 text-purple-700">Admin</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="inline-flex px-2 py-0.5 rounded-full text-xs font-semibold bg-blue-100 text-blue-700">User</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="px-6 py-4 text-gray-400 text-xs">
                                        <fmt:formatDate value="${u.createdAtDate}" pattern="MMM d, yyyy"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty recentUsers}">
                                <tr>
                                    <td colspan="4" class="px-6 py-10 text-center text-gray-400 text-sm">No users found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>

<%@ include file="/WEB-INF/includes/admin-bottom.jsp" %>
