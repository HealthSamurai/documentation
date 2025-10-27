(ns gitbok.llms-txt
  "Generator for llms.txt files according to the llms-txt specification"
  (:require
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [gitbok.state :as state]
   [gitbok.products :as products]))

(defn count-whitespace
  "Count leading whitespace in a string"
  [s]
  (count (re-find #"^\s*" s)))

(defn parse-md-link
  "Parse markdown link from SUMMARY.md line"
  [line]
  (when-let [match (re-find #"\[(.*?)\]\((.*?)\)"
                            (str/replace (str/trim line) #"\s*\*\s*" ""))]
    (let [href (nth match 2)
          title (nth match 1)]
      {:title title :path href})))

(defn file->path
  "Convert SUMMARY.md href to clean path (remove .md, README, etc.)"
  [href]
  (cond
    (str/starts-with? href "http")
    nil ; Skip external links

    :else
    (-> href
        (str/replace #"\.md$" "")
        (str/replace #"README$" "")
        (str/replace #"/$" "")
        (str/replace #"^/" ""))))

(defn build-absolute-url
  "Build absolute URL for a page"
  [ctx product-path page-path]
  (let [base-url (state/get-config ctx :base-url)
        prefix (state/get-config ctx :prefix "")
        ;; Ensure clean path construction
        clean-path (if (str/blank? page-path)
                     ""
                     page-path)]
    (str base-url prefix product-path "/" clean-path ".md")))

(defn indent-level->markdown
  "Convert indentation level to markdown list indentation"
  [level]
  (str/join (repeat level "  ")))

(defn summary-entry->llms-link
  "Convert SUMMARY.md entry to llms.txt link format"
  [ctx product-path entry indent-level]
  (let [{:keys [title path]} entry
        clean-path (file->path path)]
    (when clean-path
      (let [url (build-absolute-url ctx product-path clean-path)
            indent (indent-level->markdown indent-level)]
        (str indent "- [" title "](" url ")")))))

(defn collect-children
  "Collect children entries with greater indentation"
  [indent-level remaining-entries]
  (loop [[entry & rest] remaining-entries
         children []]
    (if (nil? entry)
      [children nil]
      (if (> (:indent entry) indent-level)
        (recur rest (conj children entry))
        [children (cons entry rest)]))))

(defn process-entries
  "Process entries recursively, building tree structure"
  [ctx product-path entries current-indent]
  (loop [[entry & remaining] entries
         result []]
    (if (nil? entry)
      result
      (let [link (summary-entry->llms-link ctx product-path entry current-indent)]
        (if link
          (let [[children rest-entries] (collect-children (:indent entry) remaining)
                child-links (when (seq children)
                              (process-entries ctx product-path children (inc current-indent)))]
            (recur rest-entries
                   (concat result [link] child-links)))
          (recur remaining result))))))

(defn parse-summary-for-llms
  "Parse SUMMARY.md content into sections for llms.txt"
  [summary-content]
  (loop [current-section nil
         sections []
         [line & remaining] (str/split summary-content #"\n")]
    (if (nil? line)
      ;; Add final section if exists
      (if current-section
        (conj sections current-section)
        sections)
      (cond
        ;; Section header (# or ##)
        (str/starts-with? (str/trim line) "#")
        (let [section-title (-> line
                                (str/replace #"^#+\s*" "")
                                (str/trim))]
          ;; Skip "Table of contents"
          (if (= section-title "Table of contents")
            (recur nil sections remaining)
            (let [new-sections (if current-section
                                 (conj sections current-section)
                                 sections)]
              (recur {:title section-title :entries []}
                     new-sections
                     remaining))))

        ;; Blank line
        (str/blank? line)
        (recur current-section sections remaining)

        ;; Entry line
        :else
        (if-let [parsed (parse-md-link line)]
          (let [indent (count-whitespace line)
                entry (assoc parsed :indent indent)
                updated-section (if current-section
                                  (update current-section :entries conj entry)
                                  {:title "" :entries [entry]})]
            (recur updated-section sections remaining))
          (recur current-section sections remaining))))))

(defn generate-product-llms-txt
  "Generate llms.txt content for a specific product"
  [ctx product]
  (try
    (let [product-id (:id product)
          product-name (:name product)
          product-path (:path product)
          og-preview (or (:og-preview-text product) product-name)

          ;; Read SUMMARY.md
          config product
          summary-path (products/summary-path config)
          _ (log/debug "Reading summary for llms.txt" {:product-id product-id :path summary-path})
          summary-content (state/slurp-resource ctx summary-path)

          ;; Parse into sections
          sections (parse-summary-for-llms summary-content)

          ;; Build content
          header (str "# " product-name " Documentation\n\n"
                      "> " og-preview "\n\n")

          body (str/join "\n\n"
                         (for [section sections]
                           (let [section-title (:title section)
                                 entries (:entries section)]
                             (str (when-not (str/blank? section-title)
                                    (str "## " section-title "\n\n"))
                                  (str/join "\n"
                                            (process-entries ctx product-path entries 0))))))]

      (str header body "\n"))

    (catch Exception e
      (log/error e "Failed to generate llms.txt" {:product (:id product)})
      (str "# " (:name product) " Documentation\n\n"
           "> Error generating documentation index\n"))))

(defn generate-root-llms-txt
  "Generate root llms.txt with list of all products"
  [ctx]
  (let [base-url (state/get-config ctx :base-url)
        prefix (state/get-config ctx :prefix "")
        products (state/get-products ctx)

        header "# Health Samurai Documentation\n\n"
        description "> Comprehensive documentation for Health Samurai's FHIR-based healthcare platforms\n\n"
        products-section "## Products\n\n"

        product-links (str/join "\n"
                                (for [product products]
                                  (let [product-name (:name product)
                                        product-path (:path product)
                                        product-llms-url (str base-url prefix product-path "/llms.txt")
                                        product-desc (or (:og-preview-text product) product-name)]
                                    (str "- [" product-name "](" product-llms-url "): " product-desc))))]

    (str header description products-section product-links "\n")))
