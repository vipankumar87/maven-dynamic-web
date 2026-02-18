<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Forgot Password" />
<c:set var="pageSubtitle" value="We'll send you a link to reset your password." />
<%@ include file="/WEB-INF/includes/auth-top.jsp" %>

            <!-- Error alert -->
            <c:if test="${not empty error}">
                <div class="mb-5 flex items-start gap-2.5 p-3.5 bg-red-50 border border-red-200 rounded-xl text-sm text-red-700">
                    <svg class="w-5 h-5 shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                    </svg>
                    <c:out value="${error}" />
                </div>
            </c:if>

            <!-- Reset link (shown after submission - replaces email form in real app) -->
            <c:if test="${not empty resetLink}">
                <div class="mb-5 p-4 bg-green-50 border border-green-200 rounded-xl">
                    <div class="flex items-center gap-2 mb-2">
                        <svg class="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                        </svg>
                        <p class="text-sm font-semibold text-green-700">Reset link generated!</p>
                    </div>
                    <p class="text-xs text-green-600 mb-3">
                        In production, this link would be emailed. For demo purposes, click below:
                    </p>
                    <a href="${resetLink}" class="inline-block text-xs bg-green-600 text-white px-3 py-1.5 rounded-lg hover:bg-green-700 transition break-all">
                        <c:out value="${resetLink}" />
                    </a>
                </div>
            </c:if>

            <div class="mb-6">
                <div class="w-12 h-12 bg-indigo-100 rounded-xl flex items-center justify-center mb-4">
                    <svg class="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                    </svg>
                </div>
                <h2 class="text-xl font-bold text-gray-800">Forgot your password?</h2>
                <p class="text-sm text-gray-500 mt-1">
                    Enter your email and we'll generate a password reset link.
                </p>
            </div>

            <form method="POST" action="${pageContext.request.contextPath}/forgot-password" class="space-y-5">
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1.5" for="email">Email address</label>
                    <input type="email" id="email" name="email" required
                           value="<c:out value='${param.email}' />"
                           class="w-full px-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none text-sm transition placeholder-gray-400"
                           placeholder="you@example.com">
                </div>
                <button type="submit"
                        class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2.5 px-4 rounded-xl transition shadow-sm text-sm">
                    Send Reset Link
                </button>
            </form>

            <p class="text-center text-sm text-gray-500 mt-6">
                Remembered it?
                <a href="${pageContext.request.contextPath}/login"
                   class="text-indigo-600 hover:text-indigo-700 font-semibold transition">Back to Sign In</a>
            </p>

<%@ include file="/WEB-INF/includes/auth-bottom.jsp" %>
