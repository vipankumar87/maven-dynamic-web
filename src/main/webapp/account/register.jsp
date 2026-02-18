<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Create Account" />
<c:set var="pageSubtitle" value="Join WebBlog and start sharing your stories." />
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

            <h2 class="text-xl font-bold text-gray-800 mb-6">Create your account</h2>

            <form method="POST" action="${pageContext.request.contextPath}/register" class="space-y-5">

                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1.5" for="name">Full name</label>
                    <input type="text" id="name" name="name" required
                           value="<c:out value='${param.name}' />"
                           class="w-full px-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none text-sm transition placeholder-gray-400"
                           placeholder="John Doe">
                </div>

                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1.5" for="email">Email address</label>
                    <input type="email" id="email" name="email" required
                           value="<c:out value='${param.email}' />"
                           class="w-full px-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none text-sm transition placeholder-gray-400"
                           placeholder="you@example.com">
                </div>

                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1.5" for="password">Password</label>
                    <input type="password" id="password" name="password" required minlength="6"
                           class="w-full px-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none text-sm transition placeholder-gray-400"
                           placeholder="Min. 6 characters">
                </div>

                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1.5" for="confirmPassword">Confirm password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required
                           class="w-full px-4 py-2.5 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none text-sm transition placeholder-gray-400"
                           placeholder="••••••••">
                </div>

                <button type="submit"
                        class="w-full bg-indigo-600 hover:bg-indigo-700 active:bg-indigo-800 text-white font-semibold py-2.5 px-4 rounded-xl transition shadow-sm text-sm">
                    Create Account
                </button>

                <p class="text-center text-xs text-gray-400">
                    By signing up, you agree to our
                    <a href="#" class="text-indigo-600 hover:underline">Terms of Service</a>
                    and
                    <a href="#" class="text-indigo-600 hover:underline">Privacy Policy</a>.
                </p>

            </form>

            <!-- Divider -->
            <div class="flex items-center gap-3 my-6">
                <div class="flex-1 h-px bg-gray-200"></div>
                <span class="text-xs text-gray-400">already have an account?</span>
                <div class="flex-1 h-px bg-gray-200"></div>
            </div>

            <a href="${pageContext.request.contextPath}/login"
               class="block w-full text-center border border-gray-300 hover:bg-gray-50 text-gray-700 font-medium py-2.5 px-4 rounded-xl transition text-sm">
                Sign In
            </a>

<%@ include file="/WEB-INF/includes/auth-bottom.jsp" %>
