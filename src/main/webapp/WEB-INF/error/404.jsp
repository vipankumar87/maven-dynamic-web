<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Page Not Found" />
<%@ include file="/WEB-INF/includes/main-top.jsp" %>

        <div class="max-w-lg mx-auto px-4 py-24 text-center">
            <p class="text-8xl font-black text-indigo-200 mb-4">404</p>
            <h1 class="text-2xl font-bold text-gray-800 mb-2">Page not found</h1>
            <p class="text-gray-500 mb-8">Sorry, the page you're looking for doesn't exist or has been moved.</p>
            <a href="${pageContext.request.contextPath}/"
               class="inline-block bg-indigo-600 text-white font-semibold px-6 py-3 rounded-xl hover:bg-indigo-700 transition shadow-sm">
                Go Home
            </a>
        </div>

<%@ include file="/WEB-INF/includes/main-bottom.jsp" %>
