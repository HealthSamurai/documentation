(ns gitbok.markdown
  (:require [clojure.string :as str]
            [hiccup2.core :as h]
            [clojure.pprint]
            [gitbok.indexing.core]
            [uui.heroicons :as ico]))

(declare parse-lines)

(defn parse-link [ctx href title]
  (if (or (str/starts-with?  href "http://")
          (str/starts-with?  href "https://"))
    (str (h/html [:a {:href href} (h/raw title)]))
    (if-let [file (gitbok.indexing.core/page-link->uri
                    ctx
                    (:uri ctx)
                    (first (str/split href #"#")))]
      (str (h/html [:a {:href href} (or (:title file) (h/raw title))]))
      (do
        (when-let [bl (:broken-links ctx)] (swap! bl conj [href title]))
        (str (h/html [:a {:href href} [:span {:class "text-red-500"} (h/raw title)]]))))))

(defn parse-inline [ctx txt]
  [(-> txt
       (str/replace #"\*\*([^\*]+)\*\*"  (fn [[_ txt & _r]] (str "<strong>" txt "</strong>")))
       (str/replace #"`([^`]+)`"  (fn [[_ txt & _r]] (str "<code class='inline-code'>" txt "</code>")))
       (str/replace #"\s(?<!\\)_([^_]+)_"  (fn [[_ txt & _r]] (str " <i>" txt "</i>")))
       (str/replace #"\\_" "_")
       (str/replace #"\!\[([^\]]*)\]\(([^\)]+)\)"  (fn [[_ title src & _]] (str "<img src=\"" src "\" alt=\"" title "\"/>")))
       (str/replace #"\[([^\]]*)\]\(([^\)]+)\)"   (fn [[_ title href & _]] (parse-link ctx href title)))
       (str/replace #"(?m)\\$" "<br/>")
       (h/raw))])

(comment

  (parse-inline {:system (atom {})} "tedt [link-name](link.html)")


  )

(defn header-parser [ctx {[l] :lines}]
  (let [lvl (count (re-find #"#+" (str/trim l)))]
    (into [(keyword (str "h" lvl))] (parse-inline ctx (str/replace l #"\s*#+\s*" "")))))

(defn code-parser [ctx block]
  (let [ls (:lines block)
        lang (str/trim (str/replace (first ls) #"^```" ""))]
    [:div {:class "my-4"}
     [:b {:class "text-xs text-gray-400"} lang]
     [:pre
      [:code (str/join "\n" (butlast (rest ls)))]]]))

;; TODO nested lists
(defn ul-parser [ctx block]
  (loop [[l & ls] (:lines block)
         current-item nil
         list-items []]

    (if (nil? l)
      (->> (conj list-items current-item)
           (mapv (fn [lines]
                   (let [res (parse-lines ctx lines)]
                     (println :parse (pr-str lines))
                     (into [:li] (if (= :p (ffirst res))
                                   (into [(rest (first res))] (rest res))
                                   res)))))
           (into [:ul]))

      (cond
        (str/starts-with? l "* ")
        (recur ls [(str/replace l #"\* " "")] (if current-item (conj list-items current-item) list-items))
        (str/starts-with? l "  ")
        (recur ls (conj current-item (str/replace l #"  " "")) list-items)
        :else
        (recur ls (conj current-item l) list-items)))))

(comment
  (parse-lines {} ["* a" "b" "c" "* c" "* d" "  * d1" "  * d2"])
  (parse-lines {} ["d" "* d1" "* d2"])
  ;; TODO does not work
  (parse-lines {} ["d" "* [d1](./a)" "* [d2](./b)"])


  )

(defn ol-parser [ctx block]
  (into [:ol]
        (->>
         (for [l (:lines block)]
           (when-not (str/blank? l)
             (into [:li] (parse-inline ctx (str/replace l #"\s*1\.\s*" "")))))
         (filter identity))))

(defn p-parser [ctx block]
  (into [:p] (parse-inline ctx (str/join " " (:lines block)))))

(def tags {:tabs :endtabs
           :tab  :endtab
           :hint :endhint
           :code :endcode
           :content-ref :endcontent-ref})

(defn init-tag [ctx block-def l]
  (let [tg (second (re-find #"^\s*\{%\s*([-_a-zA-Z0-9]+)\s" l))]
    (assoc block-def
           :tag tg
           :until (when-let [endtag (get tags (keyword tg))]
                    (let [regex (re-pattern (str "^\\s*\\{%\\s*" (name endtag) "\\s"))]
                      (fn [l] (re-find regex l)))))))

(defmulti parse-tag (fn [ctx block] (:tag block)))


(defmethod parse-tag
  "hint"
  [ctx block]
  (let [ls (:lines block)
        style (second (re-find  #"\sstyle\s*=\s*\"(.*)\"" (first ls)))]
    (into [:div {:role "hint" :style style}]
          (parse-lines ctx (butlast (rest ls))))))

(defmethod parse-tag
  "content-ref"
  [ctx block]
  (let [ls (:lines block)
        href (second (re-find  #"\surl\s*=\s*\"(.*)\"" (first ls)))]
    (if-let [file (gitbok.indexing.core/page-link->uri ctx (:uri ctx) href)]
      [:a {:role "content-ref" :href href}
       [:div {:class "flex items-center space-x-2"}
        [:span {:class "flex-1"} (or (:title file) href)]
        (ico/chevron-right "size-4")]]
      [:div {:role "content-ref" :href href}
       [:div {:class "flex items-center text-red-500"}
        (ico/exclamation-triangle "size-4")
        [:span (pr-str href)]]])))

(defmethod parse-tag
  "tabs"
  [ctx block]
  (let [ls (:lines block)]
    [:div {:class "mt-4"}
     (into [:gb-tabs {:role "tabs"}]
           (parse-lines ctx (butlast (rest ls))))]))

(defmethod parse-tag
  "tab"
  [ctx block]
  (let [ls (:lines block)
        title (second (re-find  #"\stitle\s*=\s*\"(.*)\"" (first ls)))]
    (into [:div {:role "tab" :title title}]
          (parse-lines ctx (butlast (rest ls))))))


(defmethod parse-tag
  "code"
  [ctx block]
  (let [ls (:lines block)
        file (second (re-find  #"\stitle\s*=\s*\"(.*)\"" (first ls)))]
    (into [:div {} [:b file]]
          (parse-lines ctx (butlast (rest ls))))))

(defmethod parse-tag
  :default
  [ctx block]
  [:pre (pr-str (select-keys  block [:tag :lines]))])

(defn tag-parser [ctx block]
  (parse-tag ctx block))

(defn quote-parser [ctx block]
  (let [ls (->> (:lines block) (mapv (fn [l] (str/replace l #"^\s*\>\s" ""))))]
    (into [:quote] (parse-lines ctx ls))))

(defn table-parser [ctx block]
  (let [ls (:lines block)
        headers (str/split (str/replace (first ls) #"(^\||\|$)" "") #"\|")
        rows (->> (drop 2 ls)
                  (remove str/blank?)
                  (mapv (fn [l]
                          (str/split (str/replace l #"(^\||\|$)" "") #"\|"))))]
    [:table
     [:thead
      (for [h headers]
        (into [:th] (parse-inline ctx h)))]
     [:tbody
      (for [r rows]
        [:tr
         (for [x r]
           (into [:td]  (parse-inline ctx x)))])]]))

(def block-defs
  {:header {:match #(re-find #"^\s*#" %)                                                 :parser header-parser}
   :code   {:match #(re-find #"^\s*```" %)         :until  #(re-find #"^\s*```" %)       :parser code-parser}
   :ul     {:match #(re-find #"^\s*(\*|-)\s" %)    :until  str/blank?                    :parser ul-parser}
   :ol     {:match #(re-find #"^\s*(1.)\s" %)      :until  str/blank?                    :parser ol-parser}
   :tags   {:match #(re-find #"^\s*\{%" %)         :init  init-tag                       :parser tag-parser}
   :quote  {:match #(re-find #"^\s*\>\s" %)        :until #(not (re-find #"^\s*\>\s" %)) :parser quote-parser}
   :table  {:match #(re-find #"^\|\s" %)           :until str/blank?                     :parser table-parser}})

(defn consume-until [block-def until ls & [not-include?]]
  (loop [[l & ls :as lls] ls res []]
    (if (nil? l)
      [res nil]
      (if (until l)
        (if not-include?
          [res lls]
          [(conj res l) ls])
        (recur ls (conj res l))))))

(defn block-start [l]
  (->> block-defs
       (some (fn [[nm {m :match :as block-def}]]
               (assert m (pr-str block-def))
               (when (m l) (assoc block-def :name nm))))))

(defn find-block [ctx l ls]
  (if-let [block-def (block-start l)]
    (let [block-def (if-let [init (:init block-def)] (init ctx block-def l) block-def)]
      (if-let [until (:until block-def)]
        (let [[consumed-ls ls] (consume-until block-def until ls)]
          [((:parser block-def) ctx (assoc block-def :lines (into [l] consumed-ls))) ls])
        [((:parser block-def) ctx (assoc block-def :lines [l])) ls]))
    (let [[consumed-ls ls] (consume-until {:name :p}  (fn [s] (or (str/blank? s) (block-start s))) ls true)]
      [(p-parser  ctx (assoc {:name :p} :lines (into [l] consumed-ls))) ls])))

(defn parse-lines [ctx ls]
  (loop [[l & ls] ls result []]
    (if (nil? l)
      result
      (if (str/blank? l)
        (recur ls result)
        (if-let [[bl ls] (find-block ctx l ls)]
          (recur ls (conj result bl))
          (recur ls result))))))

(defn broken-links [ctx str]
  (let [broken-links (atom [])]
    (mapv identity (parse-lines (assoc ctx :broken-links broken-links) (str/split str #"\n")))
    @broken-links))

(defn parse-md [ctx str]
  (map identity (parse-lines ctx (str/split str #"\n"))))


;; (spit "/tmp/res.yaml"
;;       (with-out-str (clojure.pprint/pprint (parse-md {:system (atom {})} "
;; # title
;;
;; just a text
;;
;; and paragraph
;;  of the thext
;; something else
;; *just test
;;
;; * item 1
;;   * subitems
;;   * subitems
;;     multiline
;; * item 2
;;
;; ```js
;; var a = b
;; function()
;; # comment
;;
;; ```
;;
;; > quote
;; > quote
;;
;; {% tabs %}
;;
;; {% endtabs %}
;;
;; # REST API
;;
;; {% hint style=\"info\" %}
;; Aidbox provides two REST APIs - FHIR and Aidbox. The main difference is [a format of resources](../../storage-1/other/aidbox-and-fhir-formats.md). Base URL for FHIR API is **/fhir** and for Aidbox **/**
;; {% endhint %}
;;
;; | Interaction type                                       |                                                                     | Format |
;; | ------------------------------------------------------ | ------------------------------------------------------------------- | ------ |
;; | **Instance Level Interactions**                        |                                                                     |        |
;; | [read](crud-1/read.md)                                 | Read the current state of the resource                              | both   |
;; | [vread](crud-1/read.md#vread)                          | Read the state of a specific version of the resource                | both   |
;; | [update](crud-1/update.md)                             | Update an existing resource by its id (or create it if it is new)   | both   |
;; | [patch](crud-1/patch.md)                               | Update an existing resource by posting a set of changes to it       | both   |
;; | [delete](crud-1/delete.md)                             | Delete a resource                                                   | both   |
;; | [history](history-1.md)                                | Retrieve the change history for a particular resource               | both   |
;; | **Type Level Interactions**                            |                                                                     |        |
;; | [create](crud-1/fhir-and-aidbox-crud.md)               | Create a new resource                                               | both   |
;; | [search](search-1/)                                    | Search the resource type based on some filter criteria              | both   |
;; | [history](history-1.md)                                | Retrieve the change history for a particular resource type          | both   |
;; | [$dump](../bulk-api-1/#usddump)                        | Dump all resources of specific type                                 | Aidbox |
;; | [$load](../bulk-api-1/#usdload)                        | Load resources of specific type                                     | both   |
;; | **Whole System Interactions**                          |                                                                     |        |
;; | [capabilities](other/metadata.md)                      | Get a capability statement for the system                           | FHIR   |
;; | [batch/transaction](../transaction.md)                 | Update, create or delete a set of resources in a single interaction | both   |
;; | [batch upsert](../other/batch-upsert.md)               | Batch create or update interaction                                  | Aidbox |
;; | [$import](../bulk-api-1/#usdimport-and-fhir-usdimport) | Bulk Import async operation                                         | both   |
;; | [$load](../bulk-api-1/#usdload)                        | Bulk load ndjson file with resources                                | both   |
;; | [$dump-sql](../bulk-api-1/#usddump-sql)                | Bulk export result of SQL Query                                     | Aidbox |
;; | history                                                | Not supported for performance reason                                |        |
;; | search                                                 | Not supported for performance reason                                |        |
;;
;;
;; "))))

;;

