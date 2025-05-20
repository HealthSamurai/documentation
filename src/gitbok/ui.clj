(ns gitbok.ui
  (:require
   [cheshire.core]
   [http]
   [gitbok.indexing.impl.summary :as summary]
   [system]
   [uui]
   [uui.heroicons :as ico]))

(defn render-menu [items & [open]]
  (if (:children items)
    [:details {:class "" :open open}
     [:summary {:class "flex items-center"} [:div {:class "flex-1"} (:title items)] (ico/chevron-right "chevron size-5 text-gray-400")]
     [:div {:class "ml-4 border-l border-gray-200"}
      (for [c (:children items)] (render-menu c))]]
    [:div {:class ""} (:title items)]))

(defn menu [summary]
  [:div
   [:a {:href "/admin/broken" :class "block px-5 py-1"} "Broken Links"]
   (for [item summary]
     [:div
      [:div {:class "pl-4 mt-4 mb-2"} [:b (:title item)]]
      (for [ch (:children item)]
        (render-menu ch))])])

;; Inline CSS style for code highlighting and GitHub hints
(def code-highlight-css
  "
  /* Code highlighting styles */
  pre {
    background-color: #1e293b;
    border-radius: 0.375rem;
    padding: 1rem;
    overflow-x: auto;
    margin: 1.5rem 0;
  }

  code {
    font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
    font-size: 0.875rem;
    line-height: 1.5;
  }

  /* GitHub Hint Styles */
  .github-hint {
    margin: 16px 0;
    padding: 16px;
    border-radius: 6px;
    border-left-width: 4px;
    border-left-style: solid;
  }

  .gh-note {
    background-color: #ddf4ff !important;
    border-color: #54aeff !important;
  }

  .gh-tip {
    background-color: #dafbe1 !important;
    border-color: #56d364 !important;
  }

  .gh-important {
    background-color: #fbefff !important;
    border-color: #d2a8ff !important;
  }

  .gh-warning {
    background-color: #fff8c5 !important;
    border-color: #f2cc60 !important;
  }

  .gh-caution {
    background-color: #fff1e5 !important;
    border-color: #ffb77c !important;
  }

  .gh-content p {
    margin-top: 0.5rem;
    margin-bottom: 0.5rem;
  }
  ")

(defn layout [context request content]
  (if (uui/hx-target request)
    (uui/response [:div#content {:class "m-x-auto flex-1 py-6 px-12  h-screen overflow-auto"} content])
    (uui/boost-response
     context request
     [:div {:class "flex items-top"}
      [:script {:src "/static/tabs.js"}]
      [:script {:src "/static/syntax-highlight.js"}]

      ;; Add inline styles for code highlighting and GitHub hints
      [:style code-highlight-css]

      [:div.nav {:class "px-6 py-6 w-80 text-sm h-screen overflow-auto bg-gray-50 shadow-md"}
       (menu (summary/get-summary context))]
      [:div#content {:class "m-x-auto flex-1 py-6 px-12  h-screen overflow-auto"} content]])))
