(ns gitbok.markdown.widgets.code-highlight
  "Widget for handling code blocks with syntax highlighting.
   Supports various languages and provides proper spacing between consecutive blocks."
  (:require
   [nextjournal.markdown.transform :as transform]
   [clojure.string :as str]))

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
   "clojure" "language-clojure"
   "clj" "language-clojure"
   "sql" "language-sql"
   "yaml" "language-yaml"
   "yml" "language-yaml"
   "html" "language-html"
   "css" "language-css"
   "python" "language-python"
   "py" "language-python"
   "java" "language-java"})

;; Patterns for basic syntax highlighting
(def syntax-patterns
  {:shell {"^\\s*#.*$" :com                  ; Comments
           "\"[^\"]*\"" :str                 ; Double quoted strings
           "'[^']*'" :str                    ; Single quoted strings
           "\\b(if|then|else|fi|while|do|done|for|in|case|esac|function)\\b" :kwd ; Keywords
           "\\$\\w+" :ident                  ; Variables
           "\\b\\d+\\b" :num                 ; Numbers
           "^\\s*(\\w+)\\b" :cmd}            ; Commands at line start
   
   :docker {"^\\s*#.*$" :com                 ; Comments
            "\"[^\"]*\"" :str                ; Double quoted strings
            "'[^']*'" :str                   ; Single quoted strings
            "\\b(FROM|RUN|CMD|LABEL|MAINTAINER|EXPOSE|ENV|ADD|COPY|ENTRYPOINT|VOLUME|USER|WORKDIR|ARG|ONBUILD|STOPSIGNAL|HEALTHCHECK|SHELL)\\b" :dockerfile-directive ; Docker directives
            "\\$\\w+" :ident                 ; Variables
            "\\b\\d+\\b" :num}               ; Numbers
   
   :json {
          "\"([^\"]*?)\"\\s*:" :key          ; JSON keys
          ":\\s*\"([^\"]*?)\"" :str          ; JSON string values
          ":\\s*(true|false|null)" :kwd      ; JSON keywords
          ":\\s*\\d+" :num                   ; JSON numbers
         }})

(defn apply-syntax-highlighting
  "Apply basic syntax highlighting by tokenizing code with regex patterns.
   
   Parameters:
   - code: The code string to tokenize
   - language: The language identifier
   
   Returns a string with HTML span tags for token highlighting."
  [code language]
  (let [lang-key (keyword (or language ""))
        patterns (get syntax-patterns lang-key)]
    (if patterns
      (let [highlighted-code (atom code)]
        ;; Apply each pattern
        (doseq [[pattern token-type] patterns]
          (let [regex (re-pattern pattern)
                replacement (str "<span class=\"" (name token-type) "\">$0</span>")]
            (swap! highlighted-code 
                   #(str/replace % regex replacement))))
        @highlighted-code)
      ;; No patterns for this language, return as-is
      code)))

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
                           code)]
    ;; Add a language indicator for debugging
    [:div.code-block-container.my-6
     [:div.language-indicator.text-xs.text-right.mb-1.text-slate-500
      (str "Language: " (or language "none"))]
     [:pre.rounded-md.p-4.overflow-x-auto.bg-slate-800.text-white.text-sm
      {:class (if (not-empty language-class) 
                "with-language-highlight" 
                "no-language-highlight")}
      [:code {:class language-class}
       ;; Use plain text here to debug the issue
       code]]]))

;; Register code block transformer
(def renderers
  {:code-block transform-code-block})

;; Transform function to intercept the default rendering
(defn replace-default-code-renderer
  "Replace the default code block renderer with our enhanced version"
  [renderers]
  (assoc renderers :code-block transform-code-block)) 