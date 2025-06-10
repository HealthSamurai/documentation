(ns gitbok.markdown.widgets.github-hint
  "Widget for handling GitHub-style hint/admonition blocks."
  (:require
   [clojure.string :as str]
   [nextjournal.markdown.transform :as transform]))

(def hint-styles
  {"!NOTE" "note"
   "!SUCCESS" "tip"
   "!TIP" "tip"
   "!WARNING" "warning"})
(defn github-hint-renderer
  [ctx node]
  (let [start (-> node :content first :content first :text)
        typ1 (second (re-matches #"\[(.*)\].*" start))
        typ (or typ1 "!NOTE")
        class (get hint-styles typ "note")
        content (if typ1
                  (update-in (:content node) [0 :content 0 :text]
                             #(subs % (+ 3 (count typ1))))
                  (:content node))]
    (into
     [:blockquote {:class class}]
     (mapv #(transform/->hiccup ctx %)
           content))))

(defn hack-info
  "{% hint style=\"info\" %}<content>{% endhint %} -> > [!NOTE]\n>"
  [md-file]
  (str/replace
   md-file
   #"(?s)\{% hint style=\"([^\"]+)\" %\}\n(.*?)\n\{% endhint %\}"
   (fn [[_ style content]]
     (str "> [!" (str/upper-case style) "] "
          (str/replace content #"\n" "\n> ")))))
