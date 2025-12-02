(ns gitbok.ui.page-actions
  (:require
   [gitbok.state :as state]
   [gitbok.products :as products]
   [gitbok.http :as http]
   [gitbok.ui.heroicons :as ico]
   [clojure.string :as str]))

(defn build-github-edit-url
  "Builds GitHub edit URL for a file"
  [context filepath]
  ;; Get docs-relative-path from current product (e.g., "aidbox/docs" or "auditbox/docs")
  ;; and volume path (e.g., "/home/svt/dev/hs/documentation/docs-new" or nil)
  (let [product (products/get-current-product context)
        docs-relative-path (:docs-relative-path product)
        volume-path (products/volume-path context)
        ;; Extract just the directory name from volume path (e.g., "docs-new" from full path)
        volume-dir (when volume-path
                     (let [file (clojure.java.io/file volume-path)]
                       (.getName file)))
        ;; Build full path: if volume-dir exists, prepend it
        full-path (if volume-dir
                    (str volume-dir "/" docs-relative-path "/" filepath)
                    (str docs-relative-path "/" filepath))]
    (str "https://github.com/healthsamurai/documentation/edit/master/" full-path)))

(defn copy-page-item [md-url]
  [:button {:class "w-full flex items-start gap-3 px-4 py-2 text-left text-sm text-on-surface-strong hover:bg-surface-hover transition-colors cursor-pointer"
            :id "copy-page-button-menu"
            :data-md-url md-url
            :onclick "copyPageAsMarkdown(this)"}
   [:svg {:style "mask-image: url('/docs/static/copy-icon.svg'); mask-repeat: no-repeat; mask-position: center center;"
          :class "size-3.5 flex-shrink-0 mt-1 bg-current"}]
   [:div {:class "flex-1 min-w-0"}
    [:div {:class "font-normal"} "Copy Page"]
    [:div {:class "text-xs opacity-60 mt-0.5"} "Copy this page as Markdown for LLMs"]]])

(defn view-markdown-item [md-url]
  [:a {:href md-url
       :target "_blank"
       :onclick "trackPageAction('view_markdown', this.href)"
       :class "w-full flex items-start gap-3 px-4 py-2 text-sm text-on-surface-strong hover:bg-surface-hover transition-colors no-underline"}
   [:svg {:style "mask-image: url('/docs/static/markdown-icon.svg'); mask-repeat: no-repeat; mask-position: center center;"
          :class "size-3.5 flex-shrink-0 mt-1 bg-current"}]
   [:div {:class "flex-1 min-w-0"}
    [:div {:class "font-normal inline-flex items-center gap-1"}
     "View as Markdown"
     (ico/arrow-up-right "size-3")]
    [:div {:class "text-xs opacity-60 mt-0.5"} "View this page as plain text"]]])

(defn github-item [github-url]
  [:a {:href github-url
       :target "_blank"
       :onclick "trackPageAction('edit_github', this.href)"
       :class "w-full flex items-start gap-3 px-4 py-2 text-sm text-on-surface-strong hover:bg-surface-hover transition-colors no-underline"}
   [:svg {:style "mask-image: url('/docs/static/github-icon.svg'); mask-repeat: no-repeat; mask-position: center center;"
          :class "size-3.5 flex-shrink-0 mt-1 bg-current"}]
   [:div {:class "flex-1 min-w-0"}
    [:div {:class "font-normal inline-flex items-center gap-1"}
     "Edit on GitHub"
     (ico/arrow-up-right "size-3")]
    [:div {:class "text-xs opacity-60 mt-0.5"} "Edit this page on GitHub"]]])

(defn page-actions-dropdown
  "Renders a split button with dropdown menu for page actions"
  [context uri filepath]
  (when (and uri filepath)
    (let [current-url (http/get-product-prefixed-url context uri)
          md-url (str current-url ".md")
          github-url (build-github-edit-url context filepath)]
      [:div {:class "relative inline-flex ml-auto overflow-visible"}
       ;; Split button group
       [:div {:class "inline-flex rounded-md shadow-sm"}
        ;; Left button: Copy Page action
        [:button {:type "button"
                  :id "copy-page-button"
                  :data-md-url md-url
                  :onclick "copyPageAsMarkdown(this)"
                  :class "inline-flex items-center gap-1.5 px-2 py-1.5 text-xs font-normal text-on-surface-strong bg-surface border border-outline rounded-l-md hover:bg-surface-hover transition-colors cursor-pointer"}
         [:svg {:style "mask-image: url('/docs/static/copy-icon.svg'); mask-repeat: no-repeat; mask-position: center center;"
                :class "size-3.5 bg-current"}]
         [:span "Copy Page"]]
        ;; Right button: Dropdown toggle
        [:button {:type "button"
                  :id "page-actions-toggle"
                  :class "inline-flex items-center px-1.5 py-1.5 text-xs text-on-surface-strong bg-surface border border-l-0 border-outline rounded-r-md hover:bg-surface-hover transition-colors cursor-pointer"}
         (ico/chevron-down "size-3")]]
       ;; Dropdown menu
       [:div {:id "page-actions-dropdown"
              :class "hidden absolute right-0 top-full mt-2 min-w-80 sm:min-w-64 sm:w-64 rounded-md shadow-lg bg-surface z-[9999] border border-outline"}
        [:div {:class "py-1"}
         (copy-page-item md-url)
         (view-markdown-item md-url)
         (github-item github-url)
         [:div {:class "border-t border-outline my-1"}]
         [:button {:class "w-full flex items-start gap-3 px-4 py-2 text-left text-sm text-on-surface-strong hover:bg-surface-hover transition-colors cursor-pointer"
                   :onclick "openInChatGPT()"}
          [:svg {:style "mask-image: url('/docs/static/chatgpt-icon.svg'); mask-repeat: no-repeat; mask-position: center center;"
                 :class "size-3.5 flex-shrink-0 mt-1 bg-current"}]
          [:div {:class "flex-1 min-w-0"}
           [:div {:class "font-normal inline-flex items-center gap-1"}
            "Open in ChatGPT"
            (ico/arrow-up-right "size-3")]
           [:div {:class "text-xs opacity-60 mt-0.5"} "Ask questions about this page"]]]
         [:button {:class "w-full flex items-start gap-3 px-4 py-2 text-left text-sm text-on-surface-strong hover:bg-surface-hover transition-colors cursor-pointer"
                   :onclick "openInClaude()"}
          [:svg {:style "mask-image: url('/docs/static/claude-icon.svg'); mask-repeat: no-repeat; mask-position: center center;"
                 :class "size-3.5 flex-shrink-0 mt-1 bg-current"}]
          [:div {:class "flex-1 min-w-0"}
           [:div {:class "font-normal inline-flex items-center gap-1"}
            "Open in Claude"
            (ico/arrow-up-right "size-3")]
           [:div {:class "text-xs opacity-60 mt-0.5"} "Ask questions about this page"]]]]]])))



