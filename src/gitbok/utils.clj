(ns gitbok.utils
  (:require [clojure.string :as str]))

(defn s->url-slug [s]
  (when s
    (-> s
      (clojure.string/replace #"[^A-Za-z0-9]+" "-")
      (clojure.string/replace #"-{2,}" "-")
      (clojure.string/replace #"^-|-$" "")
      clojure.string/lower-case)))

