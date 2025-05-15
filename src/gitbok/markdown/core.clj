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

;; Debug function to check if content contains GitHub hint blocks
(defn find-github-hints [content]
  (println "Looking for GitHub hints in content...")
  (when-let [matches (re-seq #">\s*\[\!(NOTE|TIP|IMPORTANT|WARNING|CAUTION)\]" content)]
    (println "Found GitHub hints:" (map first matches))
    matches))

(defn find-blocks
  "Find and replace GitBook blocks with temporary markers."
  [content pattern parser blocks]
  (println "Finding blocks with pattern:" (pr-str pattern))
  (if-let [matches (re-seq pattern content)]
    (let [marker (next-marker)
          [whole-match _groups] (first matches)
          matched-text whole-match]
      
      (println "Match found, length:" (count matched-text))
      (when (> (count matched-text) 0)
        (println "First 30 chars of match:" (subs matched-text 0 (min 30 (count matched-text)))))
      
      (let [parsed (parser matched-text)]
        (recur
          (if parsed
            (do 
              (println "Block parsed successfully, replacing with marker:" marker)
              (str/replace-first content matched-text marker))
            (do
              (println "Block parsing failed, keeping original text")
              (str/replace-first content matched-text matched-text)))
          pattern
          parser
          (if parsed
            (doto blocks (swap! assoc marker parsed))
            blocks))))
    content))

(defn restore-blocks
  "Restore GitBook blocks from markers in the AST."
  [node blocks]
  (if (and (map? node) (:type node))
    (case (:type node)
      :text
      (if-let [block (get @blocks (:text node))]
        (do 
          (println "Restoring block for marker:" (:text node))
          block)
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
  "Parse markdown content with GitBook extensions."
  [content]
  (let [blocks (atom {})
        content-str (if (string? content) content (str content))
        normalized-content (str content-str "\n")
        
        ;; Debug check for GitHub hints
        _ (find-github-hints normalized-content)
        
        ;; Process blocks that need special handling
        content-with-content-refs (find-blocks normalized-content
                                              content-ref/content-ref-pattern
                                              content-ref/parse-content-ref-block
                                              blocks)
        
        content-with-hints (find-blocks content-with-content-refs
                                       hint/hint-pattern
                                       hint/parse-hint-block
                                       blocks)
        
        ;; Clean up content
        cleaned-content (str/replace content-with-hints #"\n+$" "\n")
        
        ;; Parse markdown - we'll let blockquote handler detect GitHub hints
        parsed-content (md/parse cleaned-content)
        
        ;; Restore blocks
        restored-content (restore-blocks parsed-content blocks)]
    
    restored-content))

(defn escape-html
  "Escape HTML special characters"
  [text]
  (-> text
      (str/replace #"&" "&amp;")
      (str/replace #"<" "&lt;")
      (str/replace #">" "&gt;")))

;; Merge custom renderers with default markdown renderers
(def renderers
  (-> transform/default-hiccup-renderers
      (merge hint/renderers
             content-ref/renderers
             github-hint/renderers
             code-highlight/renderers)))

;; Debug the renderers
(defn print-renderers []
  (println "Available renderers:")
  (doseq [k (keys renderers)]
    (println " -" k))
  (println "GitHub hint renderer:" (get renderers :github-hint))
  (println "Blockquote renderer:" (get renderers :blockquote))
  (println "Code-block renderer:" (get renderers :code-block)))

;; Print renderer info on load
(print-renderers)

(defn render-gitbook
  "Render GitBook-compatible markdown to hiccup."
  [content]
  (debug/log "Rendering GitBook content, length:" (count content))

  ;; Debug GitHub hint blocks
  (when (str/includes? content "[!NOTE]")
    (debug/log "Content contains GitHub-style admonition blocks"))
  
  ;; Process the content
  (transform/->hiccup renderers (parse-markdown-content content)))
