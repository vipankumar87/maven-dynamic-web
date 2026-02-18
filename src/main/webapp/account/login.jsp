<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Sign In" />
<c:set var="pageSubtitle" value="Welcome back! Sign in to your account." />
<%@ include file="/WEB-INF/includes/auth-top.jsp" %>

            <!-- Alert messages -->
            <c:if test="${not empty error}">
                <div class="mb-5 flex items-start gap-2.5 p-3.5 bg-red-50 border border-red-200 rounded-xl text-sm text-red-700">
                    <svg class="w-5 h-5 shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                    </svg>
                    <c:out value="${error}" />
                </div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="mb-5 flex items-start gap-2.5 p-3.5 bg-green-50 border border-green-200 rounded-xl text-sm text-green-700">
                    <svg class="w-5 h-5 shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                    </svg>
                    <c:out value="${success}" />
                </div>
            </c:if>

            <h2 class="text-xl font-bold text-gray-800 mb-6">Sign in to your account</h2>

            <form method="POST" action="${pageContext.request.contextPath}/login" class="space-y-5">

                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1.5" for="email">Email address</label>
                    <input type="email" id="email" name="email" required
                           value="<c:out value='${param.email}' />"
                           class="w-full px-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none text-sm transition placeholder-gray-400"
                           placeholder="you@example.com">
                </div>

                <div>
                    <div class="flex justify-between items-center mb-1.5">
                        <label class="block text-sm font-medium text-gray-700" for="password">Password</label>
                        <a href="${pageContext.request.contextPath}/forgot-password"
                           class="text-xs text-indigo-600 hover:text-indigo-700 font-medium transition">
                            Forgot password?
                        </a>
                    </div>
                    <input type="password" id="password" name="password" required
                           class="w-full px-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none text-sm transition placeholder-gray-400"
                           placeholder="••••••••">
                </div>

                <button type="submit"
                        class="w-full bg-indigo-600 hover:bg-indigo-700 active:bg-indigo-800 text-white font-semibold py-2.5 px-4 rounded-xl transition shadow-sm text-sm">
                    Sign In
                </button>

            </form>

            <!-- Divider -->
            <div class="flex items-center gap-3 my-6">
                <div class="flex-1 h-px bg-gray-200"></div>
                <span class="text-xs text-gray-400">or</span>
                <div class="flex-1 h-px bg-gray-200"></div>
            </div>

            <p class="text-center text-sm text-gray-500">
                Don't have an account?
                <a href="${pageContext.request.contextPath}/register"
                   class="text-indigo-600 hover:text-indigo-700 font-semibold transition">Create one free</a>
            </p>

<%@ include file="/WEB-INF/includes/auth-bottom.jsp" %>
