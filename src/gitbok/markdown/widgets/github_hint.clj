(ns gitbok.markdown.widgets.github-hint
  "Widget for handling GitHub-style hint/admonition blocks."
  (:require
   [clojure.string :as str]
   [gitbok.utils :as utils]
   [nextjournal.markdown.transform :as transform]
   [uui.heroicons :as ico]))

(def hint-styles
  {"!NOTE" "note"
   "!SUCCESS" "tip"
   "!TIP" "tip"
   "!WARNING" "warning"})

(def hint-icons
  {"!NOTE" (ico/information-circle "size-5 text-blue-600")
   "!SUCCESS" (ico/check-circle "size-5 text-emerald-600")
   "!TIP" (ico/light-bulb "size-5 text-emerald-600")
   "!WARNING" (ico/exclamation-circle "size-5 text-amber-600")})

(def hint-colors
  {"!NOTE" {:bg "bg-blue-50" :border "border-blue-200" :text "text-blue-900"}
   "!SUCCESS" {:bg "bg-emerald-50" :border "border-emerald-200" :text "text-emerald-900"}
   "!TIP" {:bg "bg-emerald-50" :border "border-emerald-200" :text "text-emerald-900"}
   "!WARNING" {:bg "bg-amber-50" :border "border-amber-200" :text "text-amber-900"}})

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
        typ1 (second (re-matches #"\[(.*)\].*" start))
        typ (or typ1 "!NOTE")
        colors (get hint-colors typ {:bg "bg-blue-50" :border "border-blue-200" :text "text-blue-900"})
        icon (get hint-icons typ (ico/information-circle "size-5"))
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
        (str "rounded-lg border p-4 " (:bg colors) " " (:border colors) " " (:text colors))]
    [:div
     {:class class}
     [:div {:class "flex gap-3"}
      [:div {:class "flex-shrink-0 mt-0.5"}
       icon]
      (into
         [:div.hint {:class "flex-1 space-y-2"}]
         (mapv #(transform/->hiccup ctx %)
             content))]]))

(defn hack-info
  "{% hint style=\"info\" %}<content>{% endhint %} -> > [!NOTE]\n>"
  [md-file]
  (str/replace
   md-file
   #"(?s)\{% hint style=\"([^\"]+)\" %\}\n(.*?)\n\{% endhint %\}"
   (fn [[_ style content]]
     (str "> [!" (str/upper-case style) "] "
          (str/replace content #"\n" "\n> ")))))
