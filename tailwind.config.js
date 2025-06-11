/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "src/**/*.clj",
        "libs/http/src/uui/**/*clj",
        "libs/http/src/resources/public",
        "resources/public/**/*.css",
        "docs/**/*.md"
    ],
    theme: {
        extend: {
            colors: {
                'aidbox': {
                    primary: '#3b82f6',
                    'primary-dark': '#2563eb',
                    secondary: '#64748b',
                    accent: '#0ea5e9',
                    success: '#10b981',
                    warning: '#f59e0b',
                    error: '#ef4444',
                    current: '#dc2626',
                    'current-dark': '#b91c1c',
                    link: '#dc2626',
                    'link-hover': '#b91c1c',
                    'bg-primary': '#ffffff',
                    'bg-secondary': '#f8fafc',
                    'bg-tertiary': '#f1f5f9',
                    'text-primary': '#1e293b',
                    'text-secondary': '#64748b',
                    'text-muted': '#94a3b8',
                    border: '#e2e8f0',
                    'border-hover': '#cbd5e1',
                }
            },
            fontFamily: {
                'sans': ['-apple-system', 'BlinkMacSystemFont', 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', 'sans-serif'],
            },
            boxShadow: {
                'aidbox': '0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1)',
                'aidbox-md': '0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1)',
                'aidbox-lg': '0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1)',
            }
        },
    },
    plugins: [],
}
