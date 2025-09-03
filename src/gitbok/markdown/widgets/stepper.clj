(ns gitbok.markdown.widgets.stepper
  (:require
   [clojure.string :as str]
   [gitbok.utils :as utils]
   [hiccup2.core]))

(defn parse-steppers [content]
  (let [stepper-start "{% stepper %}"
        stepper-end "{% endstepper %}"
        step-start "{% step %}"
        step-end "{% endstep %}"

        find-stepper-blocks
        (fn [^String content]
          (loop [remaining content
                 blocks []
                 offset 0]
            (if remaining
              (if-let [start-pos (.indexOf ^String remaining stepper-start)]
                (if-let [end-pos (.indexOf ^String remaining stepper-end (+ start-pos (count stepper-start)))]
                  (let [block-start (+ offset start-pos)
                        block-end (+ offset end-pos (count stepper-end))
                        block-content (utils/safe-subs remaining (+ start-pos (count stepper-start)) end-pos)]
                    (if block-content
                      (recur (utils/safe-subs remaining (+ end-pos (count stepper-end)))
                             (conj blocks {:start block-start
                                           :end block-end
                                           :content block-content})
                             (+ offset end-pos (count stepper-end)))
                      blocks))
                  blocks)
                blocks)
              blocks)))

        parse-steps-in-block
        (fn [block-content block-start]
          (loop [remaining block-content
                 steps []
                 offset 0]
            (if (and remaining (not= remaining "") (not= (count remaining) 0))
              (if-let [start-pos (.indexOf ^String remaining step-start)]
                (if (>= start-pos 0)
                  (if-let [content-start (+ start-pos (count step-start))]
                    (if-let [content-end (.indexOf ^String remaining step-end content-start)]
                      (if (>= content-end 0)
                        (let [content (utils/safe-subs remaining content-start content-end)
                              step-start-pos (+ block-start offset start-pos)
                              step-end-pos (+ block-start offset content-end (count step-end))
                              next-remaining (utils/safe-subs remaining (+ content-end (count step-end)))]
                          (if content
                            (recur (or next-remaining "")
                                   (conj steps {:text (str/trim content)
                                                :start step-start-pos
                                                :end step-end-pos})
                                   (+ offset content-end (count step-end)))
                            steps))
                        steps)
                      steps)
                    steps)
                  steps)
                steps)
              steps)))

        blocks (find-stepper-blocks content)]

    (for [block blocks]
      {:text (utils/safe-subs content (:start block) (:end block))
       :start (:start block)
       :end (:end block)
       :steps (parse-steps-in-block (:content block) (:start block))})))

(defn- extract-step-title [step-content]
  ;; Extract title from step content - just use first line as is
  ;; It will be rendered through markdown later
  (let [lines (str/split-lines step-content)
        first-line (first lines)]
    (if (and first-line (not= (str/trim first-line) ""))
      (str/trim first-line)
      "Step")))

(defn- render-stepper-hiccup [context filepath stepper-data
                              parse-markdown-content-fn
                              render-md-fn]
  (let [steps
        (map-indexed
         (fn [idx {:keys [text start end]}]
           (let [lines (str/split-lines text)
                 first-line (first lines)
                 ;; Extract raw title
                 title-raw (extract-step-title text)
                 ;; Check if first line is a heading
                 is-heading (and first-line (re-matches #"^#{1,6}\s+.*" first-line))
                 ;; Check if first line looks like a title (heading or bold text)
                 is-title-line (and first-line
                                    (or is-heading
                                        (re-matches #"\*\*.*\*\*" (str/trim first-line)))) ; Bold text
                 ;; Remove title from content if it's a title line
                 content-without-title
                 (if is-title-line
                   (str/join "\n" (rest lines))
                   text)
                 ;; Render title through markdown
                 title-parsed (:parsed (parse-markdown-content-fn context [filepath title-raw]))
                 title-rendered (render-md-fn context filepath title-parsed)
                 ;; Render main content
                 parsed-content (:parsed (parse-markdown-content-fn context [filepath content-without-title]))
                 rendered-content (render-md-fn context filepath parsed-content)]
             {:index (inc idx)
              :title title-rendered ; Use the rendered title directly
              :content rendered-content
              :is-heading is-heading ; Pass heading info
              :is-last (= idx (dec (count (:steps stepper-data))))}))
         (:steps stepper-data))]

    [:div {:class "stepper-container my-6"}
     (for [{:keys [index title content is-last is-heading]} steps]
       [:div {:class "flex gap-4"}
        ;; Left side with indicator and line (GitBook-style)
        [:div {:class "relative select-none"}
         ;; Circle with number - smaller size like GitBook
         [:div {:class "flex size-7 items-center justify-center rounded-full bg-primary-9 text-white font-semibold text-sm"}
          (str index)]
         ;; Vertical line (not for last step)
         (when-not is-last
           [:div {:class "absolute top-9 bottom-2 left-3.5 w-px bg-tint-4"}])]
        ;; Right side with content
        ;; Use different negative margin based on title type for proper alignment
        [:div {:class (str "flex-1 "
                           (if is-heading
                             "-mt-[0.875rem]" ; For h1-h6 headings (14px)
                             "-mt-[0.4375rem]") ; For bold text (7px)
                           " " (if is-last "pb-0" "pb-6"))}
         [:div {:class "text-lg font-bold text-tint-12 mb-3"}
          ;; Title is already rendered HTML/hiccup
          ;; Extract content from div wrapper if present
          (if (and (vector? title)
                   (= :div (first title))
                   (vector? (last title)))
            (last title) ; Use inner element from div wrapper
            title)]
         [:div {:class "text-base text-tint-11 leading-relaxed"}
          (if (= :pre (first content))
            content
            [:div content])]]])]))

(defn hack-stepper [context filepath
                    parse-markdown-content-fn
                    render-md-fn
                    content]
  (let [stepper-data (parse-steppers content)
        sorted-steppers (sort-by :start > stepper-data)]
    (reduce
     (fn [content stepper-data]
       (let [hiccup
             (render-stepper-hiccup context filepath stepper-data
                                    parse-markdown-content-fn
                                    render-md-fn)
             html
             (str (hiccup2.core/html hiccup))
             ;; Replace newlines to prevent markdown parser from splitting the HTML
             single-line-html
             (str/replace html #"\n" "%%%STEPPER_NL%%%")]
         (str
          (utils/safe-subs content 0 (:start stepper-data))
          single-line-html
          (utils/safe-subs content (:end stepper-data)))))
     content
     sorted-steppers)))