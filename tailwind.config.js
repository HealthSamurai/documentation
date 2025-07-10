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
                'primary': {
                    1: 'rgb(255 255 255)',
                    2: 'rgb(255 248 246)',
                    3: 'rgb(255 244 240)',
                    4: 'rgb(255 237 232)',
                    5: 'rgb(255 230 223)',
                    6: 'rgb(255 220 212)',
                    7: 'rgb(249 205 196)',
                    8: 'rgb(241 191 181)',
                    9: 'rgb(217 86 64)', // Main orange color
                    10: 'rgb(202 72 51)',
                    11: 'rgb(145 95 86)',
                    12: 'rgb(36 26 24)',
                    'original': 'rgb(217 86 64)',
                },
                'tint': {
                    1: 'rgb(255 255 255)',
                    2: 'rgb(251 249 249)',
                    3: 'rgb(250 247 246)',
                    4: 'rgb(246 241 240)',
                    5: 'rgb(242 236 234)',
                    6: 'rgb(235 228 226)',
                    7: 'rgb(224 215 213)',
                    8: 'rgb(212 202 200)',
                    9: 'rgb(152 126 120)',
                    10: 'rgb(140 114 109)',
                    11: 'rgb(118 108 105)',
                    12: 'rgb(31 28 28)',
                    'original': 'rgb(120 120 120)',
                    'base': 'rgb(255 255 255)',
                    'subtle': 'rgb(251 249 249)',
                    'hover': 'rgb(246 241 240)',
                    'active': 'rgb(235 228 226)',
                    'strong': 'rgb(31 28 28)',
                },
                'neutral': {
                    1: 'rgb(255 255 255)',
                    2: 'rgb(250 250 250)',
                    3: 'rgb(247 247 247)',
                    4: 'rgb(242 242 242)',
                    5: 'rgb(237 237 237)',
                    6: 'rgb(229 229 229)',
                    7: 'rgb(217 217 217)',
                    8: 'rgb(204 204 204)',
                    9: 'rgb(120 120 120)',
                    10: 'rgb(121 121 121)',
                    11: 'rgb(110 110 110)',
                    12: 'rgb(29 29 29)',
                    'original': 'rgb(120 120 120)',
                },
                'info': {
                    1: 'rgb(255 255 255)',
                    2: 'rgb(250 250 250)',
                    3: 'rgb(247 247 247)',
                    4: 'rgb(242 242 242)',
                    5: 'rgb(237 237 237)',
                    6: 'rgb(229 229 229)',
                    7: 'rgb(217 217 217)',
                    8: 'rgb(204 204 204)',
                    9: 'rgb(120 120 120)',
                    10: 'rgb(121 121 121)',
                    11: 'rgb(110 110 110)',
                    12: 'rgb(29 29 29)',
                    'original': 'rgb(120 120 120)',
                },
                'warning': {
                    1: 'rgb(255 255 255)',
                    2: 'rgb(254 249 244)',
                    3: 'rgb(255 245 236)',
                    4: 'rgb(255 239 225)',
                    5: 'rgb(254 233 214)',
                    6: 'rgb(250 224 200)',
                    7: 'rgb(242 211 182)',
                    8: 'rgb(233 197 164)',
                    9: 'rgb(254 154 0)',
                    10: 'rgb(187 92 0)',
                    11: 'rgb(138 102 66)',
                    12: 'rgb(35 28 21)',
                    'original': 'rgb(254 154 0)',
                },
                'danger': {
                    1: 'rgb(255 255 255)',
                    2: 'rgb(255 247 246)',
                    3: 'rgb(255 242 239)',
                    4: 'rgb(255 234 230)',
                    5: 'rgb(255 226 221)',
                    6: 'rgb(255 215 210)',
                    7: 'rgb(255 200 193)',
                    8: 'rgb(254 184 177)',
                    9: 'rgb(251 44 54)',
                    10: 'rgb(228 0 33)',
                    11: 'rgb(158 87 81)',
                    12: 'rgb(39 25 23)',
                    'original': 'rgb(251 44 54)',
                },
                'success': {
                    1: 'rgb(255 255 255)',
                    2: 'rgb(245 252 246)',
                    3: 'rgb(238 252 240)',
                    4: 'rgb(229 249 231)',
                    5: 'rgb(219 246 222)',
                    6: 'rgb(207 240 210)',
                    7: 'rgb(190 229 194)',
                    8: 'rgb(172 218 177)',
                    9: 'rgb(0 201 80)',
                    10: 'rgb(0 152 23)',
                    11: 'rgb(74 124 82)',
                    12: 'rgb(22 32 23)',
                    'original': 'rgb(0 201 80)',
                },
                // Enhanced Aidbox-inspired design system
                'aidbox': {
                    primary: '#d95640',
                    'primary-dark': '#ca4833',
                    secondary: '#64748b',
                    accent: '#0ea5e9',
                    success: '#00c950',
                    warning: '#fe9a00',
                    error: '#fb2c36',
                    current: '#d95640',
                    'current-dark': '#ca4833',
                    link: '#d95640',
                    'link-hover': '#ca4833',
                    'bg-primary': '#ffffff',
                    'bg-secondary': '#fbf9f9',
                    'bg-tertiary': '#faf7f6',
                    'text-primary': '#1f1c1c',
                    'text-secondary': '#787878',
                    'text-muted': '#6e6e6e',
                    border: '#e0d7d5',
                    'border-hover': '#d8c8c6',
                },
                // Header and navigation specific colors
                'header': {
                    'bg': 'rgba(255, 255, 255, 0.88)',
                    'border': 'rgba(31, 28, 28, 0.08)',
                    'text': 'rgb(31, 28, 28)',
                },
                // Code syntax highlighting colors based on Shiki tokens from Aidbox
                'syntax': {
                    'text': 'rgb(118 108 105)',
                    'comment': 'rgba(120, 120, 120, 0.7)',
                    'keyword': 'rgb(228 0 33)',
                    'string': 'rgb(187 92 0)',
                    'function': 'rgb(202 72 51)',
                    'constant': 'rgb(187 92 0)',
                    'parameter': 'rgb(187 92 0)',
                    'punctuation': 'rgb(118 108 105)',
                    'link': 'rgb(202 72 51)',
                    'inserted': 'rgb(0 152 23)',
                    'deleted': 'rgb(228 0 33)',
                    'changed': 'rgb(31 28 28)',
                    'expression': 'rgb(0 152 23)',
                }
            },
            fontFamily: {
                'sans': ['Inter', '"Inter Fallback"', 'system-ui', 'arial'],
                'content': ['Inter', '"Inter Fallback"', 'system-ui', 'arial'],
                'mono': ['"IBM Plex Mono"', 'monospace'],
                'code': ['"JetBrains Mono"', 'ui-monospace', 'Menlo', 'Monaco', '"Cascadia Mono"', '"Segoe UI Mono"', '"Roboto Mono"', '"Oxygen Mono"', '"Ubuntu Monospace"', '"Source Code Pro"', '"Fira Mono"', '"Droid Sans Mono"', '"Courier New"', 'monospace'],
                'emoji': ['"Noto Color Emoji"', '"Noto Color Emoji Fallback"'],
            },
            fontSize: {
                // Typography scale based on Aidbox analysis
                'micro': ['12px', '16px'],     // --scalar-micro
                'mini': ['13px', '18px'],      // --scalar-mini  
                'small': ['14px', '20px'],     // --scalar-small
                'base': ['16px', '24px'],      // --scalar-paragraph
                'lg': ['16px', '26px'],        // h6, h5, h4
                'xl': ['20px', '32px'],        // h3, h2
                '2xl': ['21px', '32px'],       // --scalar-font-size-1
                '3xl': ['24px', '32px'],       // h1 (--scalar-heading-1)
                '4xl': ['30px', '36px'],       // h2 observed
                '5xl': ['36px', '40px'],       // h1 observed
            },
            lineHeight: {
                'snug': '1.375',    // --leading-snug
                'normal': '1.5',    // --leading-normal
            },
            fontWeight: {
                'regular': '400',   // --scalar-regular
                'medium': '500',    // --scalar-medium, --scalar-semibold
                'semibold': '600',  // --scalar-bold
                'bold': '700',      // --scalar-font-bold
            },
            boxShadow: {
                'aidbox': '0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1)',
                'aidbox-md': '0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1)',
                'aidbox-lg': '0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1)',
                // Header shadow from Aidbox
                'header': 'rgba(31, 28, 28, 0.08) 0px 1px 0px 0px',
            },
            borderRadius: {
                'aidbox': '3px',    // --scalar-radius
                'aidbox-lg': '6px', // --scalar-radius-lg  
                'aidbox-xl': '8px', // --scalar-radius-xl
                '2xl': '1rem',
                '3xl': '1.5rem',
            },
            spacing: {
                '1.5': '0.375rem',
                '2.5': '0.625rem',
                // Header height from analysis
                '16': '4rem',  // 64px header height
            },
            zIndex: {
                'header': '30',
                'sidebar': '20',
                'overlay': '40',
            },
            transitionDuration: {
                'default': '150ms',  // --default-transition-duration
            },
            transitionTimingFunction: {
                'ease-in-out': 'cubic-bezier(0.4, 0, 0.2, 1)',  // --ease-in-out, --default-transition-timing-function
            },
            animation: {
                'fade-in': 'fadeIn 0.15s ease-in-out',
                'slide-down': 'slideDown 0.15s ease-in-out',
                'slide-up': 'slideUp 0.15s ease-in-out',
            },
            keyframes: {
                fadeIn: {
                    '0%': { opacity: '0' },
                    '100%': { opacity: '1' },
                },
                slideDown: {
                    '0%': { transform: 'translateY(-8px)', opacity: '0' },
                    '100%': { transform: 'translateY(0)', opacity: '1' },
                },
                slideUp: {
                    '0%': { transform: 'translateY(8px)', opacity: '0' },
                    '100%': { transform: 'translateY(0)', opacity: '1' },
                },
            },
            backdropBlur: {
                'header': '8px',
            },
        },
    },
    plugins: [],
    corePlugins: {
        preflight: true,
    },
}
