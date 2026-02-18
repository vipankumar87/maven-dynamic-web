    </main>

    <!-- Footer -->
    <footer class="bg-white border-t border-gray-200 mt-auto">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <div class="flex flex-col sm:flex-row justify-between items-center gap-4">
                <div class="flex items-center gap-2">
                    <div class="w-6 h-6 bg-indigo-600 rounded flex items-center justify-center">
                        <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"/>
                        </svg>
                    </div>
                    <span class="text-sm font-semibold text-gray-700">WebBlog</span>
                </div>
                <div class="flex items-center gap-6 text-sm text-gray-400">
                    <a href="${pageContext.request.contextPath}/" class="hover:text-gray-600 transition">Home</a>
                    <a href="${pageContext.request.contextPath}/login" class="hover:text-gray-600 transition">Login</a>
                    <a href="${pageContext.request.contextPath}/register" class="hover:text-gray-600 transition">Register</a>
                </div>
                <p class="text-xs text-gray-400">&copy; 2025 WebBlog. All rights reserved.</p>
            </div>
        </div>
    </footer>

</body>
</html>
