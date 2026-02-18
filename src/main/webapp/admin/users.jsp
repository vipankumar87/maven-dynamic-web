<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Manage Users" />
<% request.setAttribute("pageActive", "users"); %>
<%@ include file="/WEB-INF/includes/admin-top.jsp" %>

            <!-- Action alerts -->
            <c:if test="${not empty success}">
                <div class="mb-5 flex items-center gap-2.5 p-3.5 bg-green-50 border border-green-200 rounded-xl text-sm text-green-700">
                    <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                    </svg>
                    <c:out value="${success}" />
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="mb-5 flex items-center gap-2.5 p-3.5 bg-red-50 border border-red-200 rounded-xl text-sm text-red-700">
                    <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                    </svg>
                    <c:out value="${error}" />
                </div>
            </c:if>

            <!-- Toolbar -->
            <div class="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 mb-5">
                <div>
                    <h2 class="text-base font-semibold text-gray-800">All Users</h2>
                    <p class="text-xs text-gray-400 mt-0.5">${totalUsers} total accounts</p>
                </div>
                <!-- Search (client-side) -->
                <div class="relative w-full sm:w-64">
                    <svg class="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
                    </svg>
                    <input type="text" id="searchInput" placeholder="Search users..."
                           class="w-full pl-9 pr-4 py-2 border border-gray-300 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none transition">
                </div>
            </div>

            <!-- Users table -->
            <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                <div class="overflow-x-auto">
                    <table class="w-full text-sm" id="usersTable">
                        <thead>
                            <tr class="bg-gray-50 text-left">
                                <th class="px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wide">#</th>
                                <th class="px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wide">Name</th>
                                <th class="px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wide">Email</th>
                                <th class="px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wide">Role</th>
                                <th class="px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wide">Joined</th>
                                <th class="px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wide text-right">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-50" id="usersBody">
                            <c:forEach var="u" items="${users}" varStatus="status">
                                <tr class="hover:bg-gray-50/50 transition user-row">
                                    <td class="px-6 py-4 text-gray-400 text-xs">${status.count}</td>
                                    <td class="px-6 py-4">
                                        <div class="flex items-center gap-3">
                                            <div class="w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold shrink-0
                                                ${u.role eq 'ADMIN' ? 'bg-purple-100 text-purple-600' : 'bg-indigo-100 text-indigo-600'}">
                                                ${u.name.substring(0,1).toUpperCase()}
                                            </div>
                                            <span class="font-medium text-gray-800 user-name">
                                                <c:out value="${u.name}" />
                                            </span>
                                        </div>
                                    </td>
                                    <td class="px-6 py-4 text-gray-500 user-email">
                                        <c:out value="${u.email}" />
                                    </td>
                                    <td class="px-6 py-4">
                                        <c:choose>
                                            <c:when test="${u.role eq 'ADMIN'}">
                                                <span class="inline-flex px-2.5 py-0.5 rounded-full text-xs font-semibold bg-purple-100 text-purple-700">Admin</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="inline-flex px-2.5 py-0.5 rounded-full text-xs font-semibold bg-blue-100 text-blue-700">User</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="px-6 py-4 text-gray-400 text-xs">
                                        <fmt:formatDate value="${u.createdAtDate}" pattern="MMM d, yyyy"/>
                                    </td>
                                    <td class="px-6 py-4">
                                        <div class="flex items-center justify-end gap-2">
                                            <!-- Toggle role -->
                                            <form method="POST" action="${pageContext.request.contextPath}/admin/users">
                                                <input type="hidden" name="action" value="toggleRole">
                                                <input type="hidden" name="userId" value="${u.id}">
                                                <button type="submit"
                                                        title="${u.role eq 'ADMIN' ? 'Demote to User' : 'Promote to Admin'}"
                                                        class="px-2.5 py-1 text-xs rounded-lg font-medium transition
                                                               ${u.role eq 'ADMIN' ? 'bg-gray-100 text-gray-600 hover:bg-gray-200' : 'bg-purple-100 text-purple-700 hover:bg-purple-200'}">
                                                    ${u.role eq 'ADMIN' ? 'Demote' : 'Promote'}
                                                </button>
                                            </form>
                                            <!-- Delete -->
                                            <c:if test="${u.id ne sessionScope.user.id}">
                                                <form method="POST" action="${pageContext.request.contextPath}/admin/users"
                                                      onsubmit="return confirm('Delete ${u.name}? This cannot be undone.')">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="userId" value="${u.id}">
                                                    <button type="submit"
                                                            title="Delete user"
                                                            class="px-2.5 py-1 text-xs rounded-lg font-medium bg-red-100 text-red-600 hover:bg-red-200 transition">
                                                        Delete
                                                    </button>
                                                </form>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty users}">
                                <tr>
                                    <td colspan="6" class="px-6 py-12 text-center text-gray-400 text-sm">No users found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <!-- Table footer -->
                <div class="px-6 py-3 bg-gray-50 border-t border-gray-100 text-xs text-gray-400">
                    Showing <span id="visibleCount">${users != null ? users.size() : 0}</span> of ${totalUsers} users
                </div>
            </div>

            <script>
                // Client-side search filter
                document.getElementById('searchInput').addEventListener('input', function () {
                    var q = this.value.toLowerCase();
                    var rows = document.querySelectorAll('.user-row');
                    var visible = 0;
                    rows.forEach(function (row) {
                        var name = row.querySelector('.user-name').textContent.toLowerCase();
                        var email = row.querySelector('.user-email').textContent.toLowerCase();
                        var show = name.includes(q) || email.includes(q);
                        row.style.display = show ? '' : 'none';
                        if (show) visible++;
                    });
                    document.getElementById('visibleCount').textContent = visible;
                });
            </script>

<%@ include file="/WEB-INF/includes/admin-bottom.jsp" %>
