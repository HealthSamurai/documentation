(ns gitbok.markdown.core
  "Core namespace for GitBook-compatible markdown parsing.
   Handles custom GitBook blocks (hint, content-ref) and standard markdown."
  (:require
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as transform]
   [gitbok.markdown.widgets.hint :as hint]
   [gitbok.markdown.widgets.content-ref :as content-ref]
   [gitbok.markdown.widgets.github-hint :as github-hint]
   [gitbok.markdown.widgets.code-highlight :as code-highlight]
   [gitbok.markdown.debug :as debug]
   [clojure.string :as str]
   [hiccup2.core :as hiccup2]))

;; Counter for generating unique block markers
(defonce block-counter (atom 0))

(defn next-marker
  "Generate a unique marker for temporary block replacement"
  []
  (str "GITBOOK_BLOCK_" (swap! block-counter inc) "_MARKER"))

(defn log-content [prefix content]
  (println prefix "Length:" (count content))
  (when (> (count content) 0)
    (println "  First 50 chars:" (subs content 0 (min 50 (count content))))
    (println "  Last 50 chars:" (subs content (max 0 (- (count content) 50)))))
  content)

(defn find-blocks
  "Find and replace GitBook blocks with temporary markers.
   Returns modified content with blocks replaced by markers.

   Parameters:
   - content: The original content to process
   - pattern: Regex pattern to match blocks
   - parser: Function to parse matched blocks into structured data
   - blocks: Atom to store parsed blocks"
  [content pattern parser blocks]
  (if-let [[match & _groups] (re-seq pattern content)]
    (let [marker (next-marker)
          matched-text (first match)

          ;; Handle the special case of trailing quote in the content
          clean-match (if (and (> (count matched-text) 0)
                               (= (last matched-text) \"))
                        (subs matched-text 0 (dec (count matched-text)))
                        matched-text)

          parsed (parser clean-match)]
      ;; Continue parsing if we have more matches
      (recur
        ;; Replace the matched text with a marker if parsing was successful,
        ;; otherwise leave the original text
        (if parsed
          (str/replace-first content matched-text marker)
          (str/replace-first content matched-text "")) ; Remove invalid blocks
        pattern
        parser
        (if parsed
          ;; Store the parsed block with its marker
          (doto blocks (swap! assoc marker parsed))
          ;; Just return the blocks unchanged
          blocks)))
    ;; No more matches, return the processed content
    content))

(defn restore-blocks
  "Restore GitBook blocks from markers in the AST.
   Recursively processes nodes to replace markers with parsed blocks."
  [node blocks]
  (if (and (map? node) (:type node))
    (case (:type node)
      :text
      (if-let [block (get @blocks (:text node))]
        block
        node)
      (update node :content #(mapv (fn [n] (restore-blocks n blocks)) %)))
    node))

(defn collect-block-lines
  "Collect lines belonging to GitBook blocks.
   Returns [blocks remaining-lines] where blocks contains complete block contents."
  [lines start-pattern end-pattern]
  (loop [remaining-lines lines
         current-block []
         result []]
    (if (empty? remaining-lines)
      [result remaining-lines]
      (let [line (first remaining-lines)]
        (cond
          ;; Start of block
          (and (empty? current-block)
               (re-find start-pattern line))
          (recur (rest remaining-lines)
                 (conj current-block line)
                 result)

          ;; Inside block
          (not-empty current-block)
          (if (re-find end-pattern line)
            ;; End of block
            (recur (rest remaining-lines)
                   []
                   (conj result (conj current-block line)))
            ;; Continue block
            (recur (rest remaining-lines)
                   (conj current-block line)
                   result))

          ;; Not in block
          :else
          (recur (rest remaining-lines)
                 current-block
                 result))))))

;; This function is now unused but kept for reference
(defn process-blocks
  "Process GitBook blocks of a specific type.
   Returns map with :nodes containing processed blocks and :remaining content."
  [content start-pattern end-pattern parser]
  (let [lines (str/split content #"\n")
        [blocks remaining] (collect-block-lines lines start-pattern end-pattern)
        processed-blocks (map #(parser (str/join "\n" %)) blocks)]
    {:nodes (filter some? processed-blocks)  ;; Filter out nil results
     :remaining (str/join "\n" remaining)}))

(defn parse-markdown-content
  "Parse markdown content with GitBook extensions.
   Processes hint and content-ref blocks before standard markdown."
  [content]
  (let [blocks (atom {})
        ;; Ensure content is a string and normalize quotes for both escaped and unescaped
        content-str (if (string? content) content (str content))
        ;; Ensure content ends with a newline to help with pattern matching
        normalized-content (-> (str content-str "\n")
                               (str/replace #"\\\"" "\"")   ;; Replace \" with "
                               (str/replace #"\\\\" "\\"))  ;; Replace \\ with \

        ;; Process all blocks in multiple passes to ensure all are captured
        ;; First find and replace all content-ref blocks with markers
        content-with-markers (find-blocks normalized-content
                                          content-ref/content-ref-pattern
                                          content-ref/parse-content-ref-block
                                          blocks)

        ;; Try again with possible variations in spacing/formatting
        content-with-more-markers (find-blocks content-with-markers
                                              #"(?s)\{\%\s*content-ref\b.*?\{\%\s*endcontent-ref\s*\%\}"
                                              content-ref/parse-content-ref-block
                                              blocks)

        ;; Then find and replace all hint blocks with markers
        content-with-hint-markers (find-blocks content-with-more-markers
                                              hint/hint-pattern
                                              hint/parse-hint-block
                                              blocks)

        ;; Process GitHub-style hint blocks
        content-with-gh-hint-markers (find-blocks content-with-hint-markers
                                                  github-hint/github-hint-pattern
                                                  github-hint/parse-github-hint
                                                  blocks)

        ;; Try another attempt with different pattern for GitHub-style admonitions
        ;; This should handle case where there's no blockquote prefix
        content-with-all-markers (find-blocks content-with-gh-hint-markers
                                               #"(?m)(?:^>?\s*)?\[\!([A-Z]+)\]\s*\n((?:(?:^>?\s*)?.*(?:\n|$))+)"
                                               (fn [text]
                                                 (println "Processing GitHub admonition:" (subs text 0 (min 50 (count text))))
                                                 (try
                                                   (let [[_ hint-type content] (re-matches #"(?m)(?:^>?\s*)?\[\!([A-Z]+)\]\s*\n((?:(?:^>?\s*)?.*(?:\n|$))+)" text)]
                                                     (when (contains? github-hint/supported-types hint-type)
                                                       {:type :github-hint
                                                        :hint-type hint-type
                                                        :content (md/parse (github-hint/extract-content content))}))
                                                   (catch Exception e
                                                     (println "Error processing GitHub admonition:" (.getMessage e))
                                                     nil)))
                                               blocks)

        ;; Clean up any unprocessed markers or syntax at the end of the content
        cleaned-content (-> content-with-all-markers
                            (str/replace #"GITBOOK_BLOCK_\d+_MARKER\"?$" "")
                            (str/replace #"\n+$" "\n")     ;; Normalize line endings
                            (str/replace #"\"$" ""))       ;; Remove trailing quotes

        ;; Parse the cleaned content with markers as markdown
        parsed-content (md/parse cleaned-content)

        ;; Restore all blocks with their parsed content
        restored-content (restore-blocks parsed-content blocks)]
    restored-content))

(defn escape-html
  "Escape HTML special characters"
  [text]
  (-> text
      (str/replace #"&" "&amp;")
      (str/replace #"<" "&lt;")
      (str/replace #">" "&gt;")))

(defn apply-basic-highlighting 
  "Apply basic syntax highlighting to code content"
  [code language]
  (let [escaped-code (escape-html code)
        styled-code 
        (if (not-empty language)
          (cond
            ;; Shell/Bash highlighting
            (or (= language "shell") (= language "bash") (= language "sh"))
            (-> escaped-code
                ;; Comments
                (str/replace #"(^|\n)(\s*#.*?)($|\n)" "$1<span style=\"color:#94a3b8;font-style:italic\">$2</span>$3")
                ;; Commands at line start
                (str/replace #"(^|\n)(\s*\w+\b)" "$1<span style=\"color:#fb923c;font-weight:bold\">$2</span>")
                ;; Strings
                (str/replace #"\"([^\"]*)\"" "<span style=\"color:#86efac\">\"$1\"</span>"))
            
            ;; Docker highlighting
            (or (= language "docker") (= language "dockerfile"))
            (-> escaped-code
                ;; Docker directives
                (str/replace #"\b(FROM|RUN|CMD|LABEL|MAINTAINER|EXPOSE|ENV|ADD|COPY|ENTRYPOINT|VOLUME|USER|WORKDIR|ARG|ONBUILD)\b" 
                            "<span style=\"color:#fb923c;font-weight:bold\">$1</span>")
                ;; Comments
                (str/replace #"(^|\n)(\s*#.*?)($|\n)" "$1<span style=\"color:#94a3b8;font-style:italic\">$2</span>$3"))
            
            ;; JSON highlighting
            (= language "json")
            (-> escaped-code
                ;; JSON keys
                (str/replace #"\"([^\"]+)\":" "<span style=\"color:#93c5fd\">\"$1\"</span>:")
                ;; JSON string values
                (str/replace #":\\s*\"([^\"]*)\"" ": <span style=\"color:#86efac\">\"$1\"</span>"))
            
            ;; Default - no highlighting
            :else escaped-code)
          
          ;; No language specified
          escaped-code)]
    
    ;; Wrap in code tag with monospace font styling
    (str "<code style=\"font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, 'Liberation Mono', 'Courier New', monospace;\">" 
         styled-code 
         "</code>")))

;; Custom transformer for code blocks
(defn transform-code-block
  "Apply custom styling to code blocks with syntax highlighting"
  [ctx node]
  (debug/log "TRANSFORM-CODE-BLOCK CALLED!")
  (debug/log-node "Code block node:" node)
  
  (let [language (or (:info node) "")
        content-text (if (sequential? (:content node))
                       (str/join (map :text (:content node)))
                       (:content node))
        ;; Use the code-highlight module for syntax highlighting
        highlighted-code (code-highlight/apply-syntax-highlighting content-text language)
        html-string (str "<div class='code-block-container' style='margin-top: 1.5rem; margin-bottom: 1.5rem;'>"
                         "<div class='language-indicator' style='text-align: right; font-size: 0.75rem; margin-bottom: 0.25rem; color: #6b7280;'>"
                         "Language: " (or language "none")
                         "</div>"
                         "<pre style='border-radius: 0.375rem; padding: 1rem; overflow-x: auto; background-color: #1e293b; color: #f8fafc; font-size: 0.875rem; line-height: 1.7;'>"
                         "<code class='language-" (or language "") "' style='font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, \"Liberation Mono\", \"Courier New\", monospace;'>"
                         highlighted-code
                         "</code>"
                         "</pre>"
                         "</div>")]
    
    (debug/log "Generated HTML for language:" language)
    
    ;; Return raw HTML
    (hiccup2/raw html-string)))

;; Add a debug transformer that will ensure our code block transformer is registered
(defn debug-transformer [handler]
  (fn [ctx node]
    (debug/log-node "Node being processed:" node)
    (if (= :code-block (:type node))
      (do
        (debug/log "Using our custom code-block transformer!")
        (transform-code-block ctx node))
      (handler ctx node))))

;; Merge custom renderers with default markdown renderers
(def renderers
  (-> transform/default-hiccup-renderers
      (merge hint/renderers
             content-ref/renderers
             github-hint/renderers
             code-highlight/renderers)
      ;; Override code block renderer with our custom implementation
      (assoc :code-block transform-code-block)))

;; Debug the renderers
(defn print-renderers []
  (println "Available renderers:")
  (doseq [k (keys renderers)]
    (println " -" k))
  (println "Code-block renderer:" (get renderers :code-block)))

;; Print renderer info on load
(print-renderers)

(defn render-gitbook
  "Render GitBook-compatible markdown to hiccup.
   Handles both standard markdown and GitBook-specific blocks."
  [content]
  (debug/log "Rendering GitBook content, length:" (count content))
  
  ;; Debug: directly test our code block renderer
  (when (str/includes? content "```")
    (debug/log "Content contains code blocks"))
  
  ;; Let the normal rendering pipeline handle both markdown and code blocks
  ;; Our custom renderers will be used for code blocks and GitBook widgets
  (debug/log "Processing markdown with all renderers")
  (transform/->hiccup renderers (parse-markdown-content content)))
