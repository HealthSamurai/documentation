(ns gitbok.markdown.widgets.github-hint
  "Widget for handling GitHub-style hint/admonition blocks."
  (:require
   [clojure.string :as str]
   [gitbok.utils :as utils]
   [nextjournal.markdown.transform :as transform]
   [gitbok.ui.heroicons :as ico]))

(def hint-icons
  {"!NOTE" (ico/information-circle "size-5 text-info-9" :outline)
   "!INFO" (ico/information-circle "size-5 text-info-9" :outline)
   "!SUCCESS" (ico/check-circle "size-5 text-success-9" :outline)
   "!TIP" (ico/light-bulb "size-5 text-success-9" :outline)
   "!WARNING" (ico/exclamation-circle "size-5 text-warning-9" :outline)
   "!DANGER" (ico/exclamation-triangle "size-5 text-danger-9" :outline)})

(def hint-colors
  {"!NOTE" {:bg "bg-info-2" :text "text-info-12"}
   "!INFO" {:bg "bg-info-2" :text "text-info-12"}
   "!SUCCESS" {:bg "bg-success-2" :text "text-success-12"}
   "!TIP" {:bg "bg-success-2" :text "text-success-12"}
   "!WARNING" {:bg "bg-warning-2" :text "text-warning-12"}
   "!DANGER" {:bg "bg-danger-2" :text "text-danger-12"}})

(defn find-by-type [m target-type]
  (loop [current m]
    (let [children (:content current)
          first-child (first children)]
      (cond
        (= (:type first-child) target-type)
        first-child

        (nil? first-child)
        nil

        :else
        (recur first-child)))))

(defn github-hint-renderer
  [ctx node]
  (let [start (:text (find-by-type node :text))
        typ1 (when start (second (re-matches #"\[(.*)\].*" start)))
        typ (or typ1 "!NOTE")
        colors (get hint-colors typ
                    {:bg "bg-info-2" :text "text-info-12"})
        icon (get hint-icons typ
                  (ico/information-circle "size-5"))
        content
        (if typ1
          (update-in (:content node) [0 :content 0 :text]
                     (fn [s]
                       (or
                        (utils/safe-subs s (+ 3 (count typ1)))
                         ;; empty line e.g. "[!WARNING]"
                        (utils/safe-subs s (+ 2 (count typ1)))
                        s)))
          (:content node))

        class
        (str "my-4 rounded-lg p-4 " (:bg colors) " " (:text colors))]
    [:div
     {:class class}
     [:div {:class "flex gap-3"}
      [:div {:class "flex-shrink-0"}
       icon]
      (into
       [:div.hint {:class "flex-1"}]
       (mapv #(transform/->hiccup ctx %)
             content))]]))

(defn hack-info
  "{% hint style=\"info\" %}<content>{% endhint %} -> > [!NOTE]\n>"
  [md-file]
  (str/replace
   md-file
   #"(?s)\{% hint style=\"([^\"]+)\" %\}\s*\n(.*?)\n\{% endhint %\}"
   (fn [[_ style content]]
     (str "> [!" (str/upper-case style) "] "
          (str/replace content #"\n" "\n> ")))))
