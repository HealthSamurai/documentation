(ns gitbok.core
  (:require
   [cheshire.core]
   [clojure.string :as str]
   [gitbok.broken-links]
   [gitbok.markdown.core :as markdown]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.markdown]
   [gitbok.ui]
   [gitbok.static]
   [http]
   [ring.util.response :as resp]
   [system]
   [uui]))

(set! *warn-on-reflection* true)

(defn read-content [context uri]
  (let [filepath (indexing/uri->filepath (uri-to-file/get context) uri)
        content (slurp (str "." filepath)) ]
    (if (str/starts-with? content "---")
      (last (str/split content #"---\n" 3))
      content)))

(defn read-and-render-file* [context uri]
  (let [content* (read-content context uri)]
    (try
      (gitbok.markdown/parse-md (assoc context :uri uri) content*)
      (catch Exception e
        [:div {:role "alert"}
         (.getMessage e)
         [:pre (pr-str e)]
         [:pre content*]]))))

(defn picture? [request]
  (str/includes? (:uri request) ".gitbook"))

(defn render-picture [request]
  (->> #"\.gitbook"
       (str/split (:uri request))
       second
       http/url-decode
       (str "./docs/.gitbook")
       resp/file-response))

(defn read-and-render-file
  [context request]
  [:div.gitbook
   (try
     (read-and-render-file* context (:uri request))
     (catch Exception e
       [:div {:role "alert"}
        (.getMessage e)
        [:pre (pr-str e)]]))])

(defn
  ^{:http {:path "/:path*"}}
  render-file-view
  [context request]
  (cond
    (picture? request)
    (render-picture request)

    :else
    (gitbok.ui/layout context request (read-and-render-file context request))))

(system/defmanifest
  {:description "gitbok"
   :deps ["http"]
   :config {:history {:type "boolean"}}})

(defn render-playground-view
  ^{:http {:path "/play"}}
  [context request]
  (def context context)
  (try 
    (let [content (try
                    (read-content context "/hello")
                    (catch Exception e
                      (println "Error reading content:" (.getMessage e))
                      "# Error reading content\n\nThere was a problem reading the file."))
          process-code-block (fn [match lang code]
                               (try
                                 (let [language (or lang "none")
                                       escaped-code (-> (or code "")
                                                        (str/replace #"&" "&amp;")
                                                        (str/replace #"<" "&lt;")
                                                        (str/replace #">" "&gt;"))
                                       highlighted-code (cond
                                                          ;; Shell highlighting
                                                          (or (= language "shell") (= language "bash") (= language "sh"))
                                                          (-> escaped-code
                                                              (str/replace #"(^|\n)(\s*#.*?)($|\n)" "$1<span class=\"com\">$2</span>$3")
                                                              (str/replace #"\$\w+" "<span class=\"ident\">$0</span>")
                                                              (str/replace #"(^|\n)(\s*)(curl|wget|cat|echo|ls|cd|mkdir|rm|cp|mv|sudo|apt|yum|npm|git|docker|kubectl)\b" "$1$2<span class=\"cmd\">$3</span>")
                                                              (str/replace #"(\s-[a-zA-Z]+|\s--[a-zA-Z0-9-]+)" "<span class=\"kwd\">$1</span>")
                                                              (str/replace #"\"([^\"]*)\"" "<span class=\"str\">\"$1\"</span>")
                                                              (str/replace #"'([^']*)'" "<span class=\"str\">'$1'</span>"))
                                                          
                                                          ;; HTTP request highlighting
                                                          (= language "http")
                                                          (-> escaped-code
                                                              (str/replace #"(^|\n)(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)(\s+)([^\n]*)" "$1<span class=\"cmd\">$2</span>$3<span class=\"kwd\">$4</span>")
                                                              (str/replace #"(^|\n)([A-Za-z0-9-]+)(\s*:\s*)([^\n]*)" "$1<span class=\"key\">$2</span>$3<span class=\"str\">$4</span>"))
                                                          
                                                          ;; Docker highlighting
                                                          (or (= language "docker") (= language "dockerfile"))
                                                          (-> escaped-code
                                                              (str/replace #"\b(FROM|RUN|CMD|LABEL|MAINTAINER|EXPOSE|ENV|ADD|COPY|ENTRYPOINT|VOLUME|USER|WORKDIR|ARG|ONBUILD|STOPSIGNAL|HEALTHCHECK|SHELL)\b" "<span class=\"dockerfile-directive\">$1</span>")
                                                              (str/replace #"(^|\n)(\s*#.*?)($|\n)" "$1<span class=\"com\">$2</span>$3"))
                                                          
                                                          ;; JSON highlighting
                                                          (= language "json")
                                                          (-> escaped-code
                                                              (str/replace #"\"([^\"]+)\":" "<span class=\"key\">\"$1\"</span>:")
                                                              (str/replace #":\s*\"([^\"]*)\"" ": <span class=\"str\">\"$1\"</span>")
                                                              (str/replace #":\s*(true|false|null)\b" ": <span class=\"kwd\">$1</span>")
                                                              (str/replace #":\s*(\d+)" ": <span class=\"num\">$1</span>"))
                                                          
                                                          ;; YAML highlighting
                                                          (or (= language "yaml") (= language "yml"))
                                                          (-> escaped-code
                                                              (str/replace #"(^|\n)([a-zA-Z0-9_-]+)(\s*:)" "$1<span class=\"key\">$2</span>$3")
                                                              (str/replace #":\s*\"([^\"]*)\"" ": <span class=\"str\">\"$1\"</span>")
                                                              (str/replace #":\s*([^\n\"]+)" ": <span class=\"str\">$1</span>")
                                                              (str/replace #"(^|\n)(\s*#.*?)($|\n)" "$1<span class=\"com\">$2</span>$3"))
                                                          
                                                          ;; No highlighting for other languages
                                                          :else escaped-code)]
                                   (str "<div class='code-block-container my-6'>"
                                        "<div class='language-indicator text-xs text-right mb-1 text-slate-500'>Language: " language "</div>"
                                        "<pre class='rounded-md p-4 overflow-x-auto bg-slate-800 text-white text-sm'><code class='language-" language "'>"
                                        highlighted-code
                                        "</code></pre>"
                                        "</div>"))
                                 (catch Exception e
                                   (println "Error highlighting code block:" (.getMessage e))
                                   (str "<div class='code-block-container my-6 border border-red-200'>"
                                        "<div class='language-indicator text-xs text-right mb-1 text-red-500'>Error highlighting " (or lang "none") "</div>"
                                        "<pre class='rounded-md p-4 overflow-x-auto bg-slate-800 text-white text-sm'><code>"
                                        (-> (or code "")
                                            (str/replace #"&" "&amp;")
                                            (str/replace #"<" "&lt;")
                                            (str/replace #">" "&gt;"))
                                        "</code></pre>"
                                        "</div>"))))
          highlighted-content (try
                                (str/replace content 
                                             #"(?s)```([a-zA-Z0-9]*)\n(.*?)\n```" 
                                             (fn [[match lang code]] 
                                               (process-code-block match lang code)))
                                (catch Exception e
                                  (println "Error in content highlighting:" (.getMessage e))
                                  (str "<div class='p-4 border border-red-300 bg-red-50 text-red-700 rounded'>"
                                       "Error processing content: " (.getMessage e)
                                       "</div>")))]
      (gitbok.ui/layout
       context request
       [:div.gitbook
        (uui/raw highlighted-content)]))
    (catch Exception e
      (println "Top-level error in render-playground-view:" (.getMessage e))
      (gitbok.ui/layout
       context request
       [:div.gitbook
        [:div.p-4.border.border-red-500.bg-red-50.text-red-800.rounded
         [:h2 "Error rendering content"]
         [:pre (.getMessage e)]]]))))

(defn simple-code-highlighter 
  "Very simple code block highlighter"
  [content]
  (println "Highlighting code blocks in content of length:" (count (or content "")))
  
  ;; Create a basic HTML wrapper with fixed styling
  (str "<div class='markdown'>"
       "<h1>Markdown Rendering Test</h1>"
       
       ;; Simple transformation of the code blocks
       (str/replace (or content "")
                    #"(?s)```([a-zA-Z0-9]*)\n(.*?)\n```"
                    (fn [[_ lang code]]
                      (println "Processing code block, language:" (or lang "none") "length:" (count (or code "")))
                      (str "<div style='margin: 20px 0; padding: 10px; border: 1px solid #ccc;'>"
                           "<div style='text-align: right; color: #666; font-size: 12px;'>Language: " (or lang "none") "</div>"
                           "<pre style='background-color: #1e293b; color: #f8fafc; padding: 15px; border-radius: 5px; overflow-x: auto;'>"
                           "<code>"
                           (-> (or code "")
                               (str/replace #"&" "&amp;")
                               (str/replace #"<" "&lt;")
                               (str/replace #">" "&gt;"))
                           "</code>"
                           "</pre>"
                           "</div>")))
       
       ;; Add raw content as text for debugging
       "<h2>Raw Content (for debugging)</h2>"
       "<pre style='border: 1px solid #ccc; padding: 10px; background-color: #f5f5f5; max-height: 300px; overflow: auto;'>"
       (-> (or content "")
           (str/replace #"&" "&amp;")
           (str/replace #"<" "&lt;")
           (str/replace #">" "&gt;"))
       "</pre>"
       
       "</div>"))

#_{:clj-kondo/ignore [:unresolved-symbol]}
(system/defstart
  [context config]
  (http/register-ns-endpoints context *ns*)
  (http/register-endpoint
    context
    {:path "/" :method :get :fn #'render-file-view})
  (http/register-endpoint context
                          {:path "/admin/broken" :method :get :fn #'gitbok.broken-links/broken-links-view})
  (http/register-endpoint context
                          {:path "/play" :method :get :fn #'render-playground-view})
  
  ;; Register static file handlers
  (gitbok.static/register-endpoints context)
  
  ;; Set up indexing
  (uri-to-file/set context)
  (file-to-uri/set context)
  (summary/set context)
  {})

(def default-config
  {:services ["http" "uui" "gitbok.core"]
   :http {:port 8081}})
