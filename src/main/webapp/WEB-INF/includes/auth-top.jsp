<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty pageTitle ? pageTitle : 'WebBlog'}</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gradient-to-br from-slate-50 via-indigo-50 to-purple-50 min-h-screen flex items-center justify-center p-4">

    <div class="w-full max-w-md">

        <!-- Brand -->
        <div class="text-center mb-8">
            <a href="${pageContext.request.contextPath}/" class="inline-flex items-center gap-2.5">
                <div class="w-10 h-10 bg-indigo-600 rounded-xl flex items-center justify-center shadow-md">
                    <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"/>
                    </svg>
                </div>
                <span class="text-2xl font-bold text-gray-800">WebBlog</span>
            </a>
            <p class="text-gray-500 mt-2 text-sm">${not empty pageSubtitle ? pageSubtitle : ''}</p>
        </div>

        <!-- Card -->
        <div class="bg-white rounded-2xl shadow-lg border border-gray-100 p-8">
