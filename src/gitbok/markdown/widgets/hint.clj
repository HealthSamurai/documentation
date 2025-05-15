(ns gitbok.markdown.widgets.hint
  (:require
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as md.transform]))


(defn parse-hint [])


(def data
  (md/parse "> et tout autour, la longue cohorte de ses personnages, avec leur histoire, leur passé, leurs légendes:
> 1. Pélage vainqueur d'Alkhamah se faisant couronner à Covadonga
> 2. La cantatrice exilée de Russie suivant Schönberg à Amsterdam
> 3. Le petit chat sourd aux yeux vairons vivant au dernier étage
> 4. ...

**Georges Perec**, _La Vie mode d'emploi_.

---
"))

(md.transform/->hiccup data)
