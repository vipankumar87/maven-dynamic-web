<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Reset Password" />
<c:set var="pageSubtitle" value="Enter your new password below." />
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

            <!-- Invalid / expired token -->
            <c:if test="${tokenInvalid}">
                <div class="text-center py-6">
                    <div class="w-14 h-14 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
                        <svg class="w-7 h-7 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                        </svg>
                    </div>
                    <h2 class="text-lg font-bold text-gray-800 mb-1">Link Invalid or Expired</h2>
                    <p class="text-sm text-gray-500 mb-5">
                        This reset link is no longer valid. Please request a new one.
                    </p>
                    <a href="${pageContext.request.contextPath}/forgot-password"
                       class="inline-block bg-indigo-600 text-white font-medium px-5 py-2.5 rounded-xl hover:bg-indigo-700 transition text-sm">
                        Request New Link
                    </a>
                </div>
            </c:if>

            <!-- Reset form -->
            <c:if test="${not tokenInvalid}">
                <div class="mb-6">
                    <div class="w-12 h-12 bg-indigo-100 rounded-xl flex items-center justify-center mb-4">
                        <svg class="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z"/>
                        </svg>
                    </div>
                    <h2 class="text-xl font-bold text-gray-800">Set new password</h2>
                    <p class="text-sm text-gray-500 mt-1">Choose a strong password you haven't used before.</p>
                </div>

                <form method="POST" action="${pageContext.request.contextPath}/reset-password" class="space-y-5">
                    <!-- Hidden token -->
                    <input type="hidden" name="token" value="<c:out value='${param.token}' />">

                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1.5" for="password">New password</label>
                        <input type="password" id="password" name="password" required minlength="6"
                               class="w-full px-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none text-sm transition placeholder-gray-400"
                               placeholder="Min. 6 characters">
                    </div>

                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1.5" for="confirmPassword">Confirm new password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required
                               class="w-full px-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none text-sm transition placeholder-gray-400"
                               placeholder="••••••••">
                    </div>

                    <!-- Password strength hint -->
                    <ul class="text-xs text-gray-400 space-y-1 pl-1">
                        <li class="flex items-center gap-1.5">
                            <svg class="w-3.5 h-3.5 text-gray-300" fill="currentColor" viewBox="0 0 20 20">
                                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
                            </svg>
                            At least 6 characters
                        </li>
                        <li class="flex items-center gap-1.5">
                            <svg class="w-3.5 h-3.5 text-gray-300" fill="currentColor" viewBox="0 0 20 20">
                                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
                            </svg>
                            Mix letters and numbers recommended
                        </li>
                    </ul>

                    <button type="submit"
                            class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2.5 px-4 rounded-xl transition shadow-sm text-sm">
                        Reset Password
                    </button>
                </form>
            </c:if>

            <p class="text-center text-sm text-gray-500 mt-6">
                <a href="${pageContext.request.contextPath}/login"
                   class="text-indigo-600 hover:text-indigo-700 font-semibold transition">&larr; Back to Sign In</a>
            </p>

<%@ include file="/WEB-INF/includes/auth-bottom.jsp" %>
