(ns gitbok.markdown.widgets.code-highlight
  "Widget for handling code blocks with syntax highlighting.
   Supports various languages and provides proper spacing between consecutive blocks."
  (:require
   [nextjournal.markdown.transform :as transform]
   [clojure.string :as str]
   [hiccup2.core :as hiccup2]))

(def language-classes
  "Map of supported languages to their Tailwind CSS classes for syntax highlighting"
  {"shell" "language-shell"
   "bash" "language-shell"
   "sh" "language-shell"
   "docker" "language-docker"
   "dockerfile" "language-docker"
   "json" "language-json"
   "javascript" "language-javascript"
   "js" "language-javascript"
   "typescript" "language-typescript"
   "ts" "language-typescript"
   "clojure" "language-clojure"
   "clj" "language-clojure"
   "sql" "language-sql"
   "yaml" "language-yaml"
   "yml" "language-yaml"
   "html" "language-html"
   "css" "language-css"
   "python" "language-python"
   "py" "language-python"
   "java" "language-java"
   "rust" "language-rust"
   "rs" "language-rust"
   "go" "language-go"
   "golang" "language-go"
   "c" "language-c"
   "cpp" "language-cpp"
   "c++" "language-cpp"
   "csharp" "language-csharp"
   "cs" "language-csharp"
   "php" "language-php"
   "ruby" "language-ruby"
   "rb" "language-ruby"
   "swift" "language-swift"
   "kotlin" "language-kotlin"
   "scala" "language-scala"
   "xml" "language-xml"
   "http" "language-http"
   "markdown" "language-markdown"
   "md" "language-markdown"})

;; Patterns for syntax highlighting
(def syntax-patterns
  {:shell {"^\\s*#.*$" :com                  ; Comments
           "\"[^\"]*\"" :str                 ; Double quoted strings
           "'[^']*'" :str                    ; Single quoted strings
           "\\b(if|then|else|fi|while|do|done|for|in|case|esac|function|return|exit|echo|cd|source|export|eval|read|let)\\b" :kwd ; Keywords
           "\\$\\{?[A-Za-z0-9_]+\\}?" :ident ; Variables including ${VAR}
           "\\b\\d+\\b" :num                 ; Numbers
           "^\\s*(\\w+)\\b" :cmd}            ; Commands at line start
   
   :docker {"^\\s*#.*$" :com                 ; Comments
            "\"[^\"]*\"" :str                ; Double quoted strings
            "'[^']*'" :str                   ; Single quoted strings
            "\\b(FROM|RUN|CMD|LABEL|MAINTAINER|EXPOSE|ENV|ADD|COPY|ENTRYPOINT|VOLUME|USER|WORKDIR|ARG|ONBUILD|STOPSIGNAL|HEALTHCHECK|SHELL)\\b" :dockerfile-directive ; Docker directives
            "\\$\\{?[A-Za-z0-9_]+\\}?" :ident ; Variables including ${VAR}
            "\\b\\d+\\b" :num}               ; Numbers
   
   :json {
          "\"([^\"]*?)\"\\s*:" :key          ; JSON keys
          ":\\s*\"([^\"]*?)\"" :str          ; JSON string values
          ":\\s*(true|false|null)" :kwd      ; JSON keywords
          ":\\s*\\d+(\\.\\d+)?(e[+-]?\\d+)?" :num ; JSON numbers (including decimals and scientific notation)
         }
   
   :yaml {
          "^\\s*([a-zA-Z0-9_-]+)\\s*:" :key  ; YAML keys
          ":\\s*\"([^\"]*?)\"" :str          ; YAML string values with quotes
          ":\\s*'([^']*?)'" :str             ; YAML string values with single quotes
          ":\\s*([^\\n\"{}\\[\\]]+)" :str    ; YAML string values without quotes
          "^\\s*#.*$" :com                   ; YAML comments
          "^\\s*-\\s+" :kwd                  ; YAML list markers
          ":\\s*(true|false|null|~)" :kwd    ; YAML keywords
          ":\\s*\\d+(\\.\\d+)?(e[+-]?\\d+)?" :num ; YAML numbers
         }
   
   :javascript {
          "//.*$" :com                       ; Single-line comments
          "/\\*[\\s\\S]*?\\*/" :com          ; Multi-line comments
          "\"([^\"\\\\]|\\\\.)*?\"" :str     ; Double quoted strings
          "'([^'\\\\]|\\\\.)*?'" :str        ; Single quoted strings
          "`([^`\\\\]|\\\\.)*?`" :str        ; Template strings
          "\\b(var|let|const|function|return|if|else|for|while|do|switch|case|break|continue|try|catch|finally|throw|new|delete|typeof|instanceof|void|class|extends|super|import|export|from|as|default|async|await|yield)\\b" :kwd ; Keywords
          "\\b(true|false|null|undefined|NaN|Infinity)\\b" :kwd ; Constants
          "\\b(console|document|window|Array|Object|String|Number|Boolean|Function|Symbol|Math|Date|Promise)\\b" :fn ; Built-in objects
          "\\b\\d+(\\.\\d+)?(e[+-]?\\d+)?\\b" :num ; Numbers
          "\\b([a-zA-Z_$][a-zA-Z0-9_$]*)\\s*\\(" :fn ; Function calls
         }
   
   :html {
          "<!--[\\s\\S]*?-->" :com           ; HTML comments
          "</?[a-zA-Z][a-zA-Z0-9\\-]*[^>]*>" :tag ; HTML tags
          "\"([^\"\\\\]|\\\\.)*?\"" :str     ; Attribute values
          "\\b([a-zA-Z][a-zA-Z0-9\\-]*)=" :attr ; Attributes
          "&[a-zA-Z0-9#]+;" :ent             ; HTML entities
         }
   
   :css {
          "/\\*[\\s\\S]*?\\*/" :com          ; CSS comments
          "\\{[^}]*\\}" :block               ; CSS blocks
          "\\b([a-zA-Z\\-]+):" :prop         ; Properties
          "#[a-fA-F0-9]{3,6}\\b" :value      ; Hex colors
          "\"([^\"\\\\]|\\\\.)*?\"" :str     ; Strings
          "'([^'\\\\]|\\\\.)*?'" :str        ; Single quoted strings
          "\\b\\d+(\\.\\d+)?(px|em|rem|%|vh|vw|s|ms|deg|rad|turn)\\b" :value ; Values with units
          "\\b(inherit|initial|unset|auto|none|flex|block|inline|grid)\\b" :kwd ; Common keywords
         }
   
   :python {
          "#.*$" :com                        ; Comments
          "\"\"\"[\\s\\S]*?\"\"\"" :com      ; Docstrings
          "'''[\\s\\S]*?'''" :com            ; Single quoted docstrings
          "\"([^\"\\\\]|\\\\.)*?\"" :str     ; Double quoted strings
          "'([^'\\\\]|\\\\.)*?'" :str        ; Single quoted strings
          "\\b(def|class|if|elif|else|for|while|try|except|finally|with|return|import|from|as|global|nonlocal|lambda|and|or|not|is|in|raise|assert|yield|break|continue|pass)\\b" :kwd ; Keywords
          "\\b(True|False|None)\\b" :kwd     ; Constants
          "\\b\\d+(\\.\\d+)?(e[+-]?\\d+)?\\b" :num ; Numbers
          "\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(" :fn ; Function calls
          "\\b(self|cls)\\b" :ident          ; Special identifiers
          "@\\w+" :decorator                 ; Decorators
         }
   
   :clojure {
          ";.*$" :com                        ; Comments
          "\"([^\"\\\\]|\\\\.)*?\"" :str     ; Strings
          "#\"([^\"\\\\]|\\\\.)*?\"" :str    ; Regex literals
          ":\\w+" :key                       ; Keywords
          "\\(def\\w*\\s+\\w+" :def          ; Definitions
          "\\(\\w+" :fn                      ; Function calls
          "\\[|\\]|\\{|\\}|\\(|\\)" :delim   ; Delimiters
          "\\b\\d+(\\.\\d+)?M?\\b" :num      ; Numbers
          "true|false|nil" :kwd              ; Constants
         }
   
   :rust {
          "//.*$" :com                       ; Single-line comments
          "/\\*[\\s\\S]*?\\*/" :com          ; Multi-line comments
          "\"([^\"\\\\]|\\\\.)*?\"" :str     ; Strings
          "'([^'\\\\]|\\\\.)*?'" :char       ; Character literals
          "\\b(let|mut|fn|struct|enum|trait|impl|pub|use|mod|if|else|match|for|while|loop|continue|break|return|as|const|static|type|where|unsafe|extern|crate|self|super|dyn|async|await|move)\\b" :kwd ; Keywords
          "\\b(true|false|Some|None|Ok|Err)\\b" :kwd ; Constants and common types
          "\\b(i8|i16|i32|i64|i128|isize|u8|u16|u32|u64|u128|usize|f32|f64|bool|char|str|String|Vec|Option|Result)\\b" :type ; Types
          "\\b\\d+(\\.\\d+)?(f32|f64)?\\b" :num ; Numbers
          "\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(" :fn ; Function calls
          "\\b([A-Z][a-zA-Z0-9_]*)\\b" :type ; Type names (uppercase first letter)
          "#!?\\[[^\\]]+\\]" :attribute      ; Attributes
         }
   
   :go {
          "//.*$" :com                       ; Single-line comments
          "/\\*[\\s\\S]*?\\*/" :com          ; Multi-line comments
          "\"([^\"\\\\]|\\\\.)*?\"" :str     ; Strings
          "`([^`]*?)`" :str                  ; Raw strings
          "\\b(func|var|const|type|struct|interface|map|chan|package|import|if|else|for|range|switch|case|default|break|continue|return|go|defer|select|fallthrough)\\b" :kwd ; Keywords
          "\\b(true|false|nil|iota)\\b" :kwd ; Constants
          "\\b(int|int8|int16|int32|int64|uint|uint8|uint16|uint32|uint64|float32|float64|complex64|complex128|byte|rune|string|bool|error)\\b" :type ; Types
          "\\b\\d+(\\.\\d+)?\\b" :num        ; Numbers
          "\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(" :fn ; Function calls
         }
   
   :java {
          "//.*$" :com                       ; Single-line comments
          "/\\*[\\s\\S]*?\\*/" :com          ; Multi-line comments
          "\"([^\"\\\\]|\\\\.)*?\"" :str     ; Strings
          "'([^'\\\\]|\\\\.)*?'" :char       ; Character literals
          "\\b(class|interface|enum|extends|implements|import|package|public|private|protected|static|final|abstract|new|this|super|if|else|for|while|do|switch|case|break|continue|return|try|catch|finally|throw|throws|instanceof)\\b" :kwd ; Keywords
          "\\b(true|false|null)\\b" :kwd     ; Constants
          "\\b(int|long|float|double|char|boolean|byte|short|void|String|Object|System)\\b" :type ; Common types
          "\\b\\d+(\\.\\d+)?[fFdDlL]?\\b" :num ; Numbers with optional type suffix
          "\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(" :fn ; Method calls
          "@\\w+" :decorator                 ; Annotations
         }
   
   :c {
          "//.*$" :com                       ; Single-line comments
          "/\\*[\\s\\S]*?\\*/" :com          ; Multi-line comments
          "\"([^\"\\\\]|\\\\.)*?\"" :str     ; Strings
          "'([^'\\\\]|\\\\.)*?'" :char       ; Character literals
          "\\b(if|else|for|while|do|switch|case|break|continue|return|goto|typedef|struct|union|enum|extern|static|const|volatile|register|auto|inline|void)\\b" :kwd ; Keywords
          "\\b(int|char|short|long|unsigned|float|double|size_t|NULL)\\b" :type ; Types
          "\\b\\d+(\\.\\d+)?[uUlLfF]?\\b" :num ; Numbers with optional type suffix
          "\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(" :fn ; Function calls
          "#include\\s*[<\"][^>\"]*[>\"]" :preprocessor ; Include directives
          "#define\\s+\\w+" :preprocessor     ; Define directives
         }
   
   :http {
          "^(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)\\s+([^\\n]*)" :cmd ; HTTP method
          "^([A-Za-z0-9-]+)\\s*:\\s*([^\\n]*)" :key                    ; HTTP headers
          "\\{[^\\}]*\\}" :str                                         ; JSON body (simple detection)
          ":\\s*\"([^\"]*?)\"" :str                                    ; JSON string in body (if JSON)
          "\"([^\"]*?)\"\\s*:" :key                                    ; JSON keys in body (if JSON)
         }
   
   :markdown {
          "^\\s*#{1,6}\\s+.*$" :heading      ; Headings
          "^\\s*>.*$" :blockquote            ; Blockquotes
          "\\*\\*([^*]*)\\*\\*" :strong      ; Bold
          "\\*([^*]*)\\*" :em                ; Italic
          "`[^`]*`" :inline-code             ; Inline code
          "\\[([^\\]]*)\\]\\([^\\)]*\\)" :link ; Links
          "^\\s*[\\*\\-\\+]\\s+" :list       ; List items
          "^\\s*\\d+\\.\\s+" :ordered-list   ; Ordered list
          "!\\[([^\\]]*)\\]\\([^\\)]*\\)" :image ; Images
         }
   
   ;; Map language aliases to their patterns
   :sh :shell
   :bash :shell
   :yml :yaml
   :dockerfile :docker
   :js :javascript
   :ts :typescript
   :py :python
   :clj :clojure
   :rb :ruby
   :cpp :c
   :cs :csharp
   :golang :go
   :rs :rust
   :md :markdown})

;; Set colors for different token types
(def token-colors
  {:com "#94a3b8"          ; Comments - slate-400
   :str "#86efac"          ; Strings - green-300
   :char "#86efac"         ; Characters - green-300
   :kwd "#93c5fd"          ; Keywords - blue-300
   :type "#c4b5fd"         ; Types - violet-300
   :ident "#e2e8f0"        ; Identifiers - slate-200
   :fn "#c4b5fd"           ; Functions - violet-300
   :num "#fda4af"          ; Numbers - rose-300
   :cmd "#fb923c"          ; Shell commands - orange-400
   :dockerfile-directive "#fb923c" ; Docker directives - orange-400
   :key "#93c5fd"          ; Object keys - blue-300
   :value "#86efac"        ; Values - green-300
   :delim "#64748b"        ; Delimiters - slate-500
   :tag "#fb923c"          ; HTML tags - orange-400
   :attr "#93c5fd"         ; HTML attributes - blue-300
   :ent "#fda4af"          ; HTML entities - rose-300
   :block "#e2e8f0"        ; CSS blocks - slate-200
   :prop "#93c5fd"         ; CSS properties - blue-300
   :decorator "#fbbf24"    ; Decorators - amber-400
   :attribute "#fbbf24"    ; Rust attributes - amber-400
   :preprocessor "#fbbf24" ; C preprocessor - amber-400
   :inline-code "#e2e8f0"  ; Markdown inline code - slate-200
   :heading "#fb923c"      ; Markdown headings - orange-400
   :blockquote "#94a3b8"   ; Markdown blockquotes - slate-400
   :strong "#fda4af"       ; Markdown bold - rose-300
   :em "#c4b5fd"           ; Markdown italic - violet-300
   :link "#93c5fd"         ; Markdown links - blue-300
   :list "#94a3b8"         ; Markdown lists - slate-400
   :ordered-list "#94a3b8" ; Markdown ordered lists - slate-400
   :image "#fbbf24"        ; Markdown images - amber-400
   :def "#fb923c"          ; Clojure definitions - orange-400
   })

;; Add escape_html function
(defn escape-html
  "Escape HTML special characters"
  [text]
  (-> text
      (str/replace #"&" "&amp;")
      (str/replace #"<" "&lt;")
      (str/replace #">" "&gt;")))

(defn create-token-style
  "Create CSS style for a token type"
  [token-type]
  (let [color (get token-colors token-type "#e2e8f0")
        style (cond
                (= token-type :com) (str "color:" color ";font-style:italic")
                (= token-type :kwd) (str "color:" color ";font-weight:bold")
                (= token-type :cmd) (str "color:" color ";font-weight:bold")
                (= token-type :dockerfile-directive) (str "color:" color ";font-weight:bold")
                (= token-type :strong) (str "color:" color ";font-weight:bold")
                (= token-type :em) (str "color:" color ";font-style:italic")
                (= token-type :heading) (str "color:" color ";font-weight:bold")
                (= token-type :def) (str "color:" color ";font-weight:bold")
                :else (str "color:" color))]
    style))

(defn apply-syntax-highlighting
  "Apply syntax highlighting by tokenizing code with regex patterns.
   
   Parameters:
   - code: The code string to tokenize
   - language: The language identifier
   
   Returns a string with HTML span tags for token highlighting."
  [code language]
  (let [lang-key (keyword (or language ""))
        patterns-key (get syntax-patterns lang-key lang-key)
        patterns (get syntax-patterns patterns-key)]
    (if patterns
      (let [escaped-code (escape-html code)
            highlighted-code (atom escaped-code)]
        ;; Apply each pattern
        (doseq [[pattern token-type] patterns]
          (let [regex (re-pattern pattern)
                style (create-token-style token-type)
                replacement (str "<span style=\"" style "\">$0</span>")]
            (swap! highlighted-code 
                   #(str/replace % regex replacement))))
        @highlighted-code)
      ;; No patterns for this language, return escaped code
      (escape-html code))))

(defn transform-code-block
  "Transform a code block into highlighted hiccup markup.
   Applies syntax highlighting based on language.
   
   Parameters:
   - ctx: Context parameter
   - node: The code block node from markdown AST
   
   Returns hiccup vector with styled pre/code structure."
  [ctx node]
  (println "Code block found! Language:" (or (:info node) "none"))
  (let [code (if (sequential? (:content node))
               (str/join (map :text (:content node)))
               (:content node))
        info (or (:info node) "")
        language (first (str/split info #"\s+"))
        language-class (get language-classes language "")
        highlighted-code (if (not-empty language)
                           (apply-syntax-highlighting code language)
                           (escape-html code))]
    ;; Add a language indicator and properly highlighted code
    [:div.code-block-container.my-6
     [:div.language-indicator.text-xs.text-right.mb-1.text-slate-500
      (str "Language: " (or language "none"))]
     [:pre.rounded-md.p-4.overflow-x-auto.bg-slate-800.text-white.text-sm.leading-relaxed
      {:class (if (not-empty language-class) 
                "with-language-highlight" 
                "no-language-highlight")}
      ;; Use raw HTML for highlighted code with spans
      (hiccup2.core/raw 
        (str "<code class=\"language-" (or language "") "\">" 
             highlighted-code 
             "</code>"))]]))

;; Register code block transformer
(def renderers
  {:code-block transform-code-block})

;; Transform function to intercept the default rendering
(defn replace-default-code-renderer
  "Replace the default code block renderer with our enhanced version"
  [renderers]
  (assoc renderers :code-block transform-code-block)) 