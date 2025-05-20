(ns gitbok.static
  (:require
   [ring.util.response :as resp]
   [clojure.string :as str]
   [system]
   [http]))

;; (defn
;;   ^{:http {:path "/css/:file" :method :get}}
;;   handle-css
;;   [_context request]
;;   (let [file (get-in request [:params :file])
;;         file-path (str "resources/public/css/" file)]
;;     (println "Serving CSS file:" file-path)
;;     (-> (resp/file-response file-path)
;;         (resp/content-type "text/css"))))
;;
;; (defn
;;   ^{:http {:path "/static/:file" :method :get}}
;;   handle-static
;;   [_context request]
;;   (let [file (get-in request [:params :file])
;;         file-path (str "resources/public/static/" file)]
;;     (println "Serving static file:" file-path)
;;     (-> (resp/file-response file-path)
;;         (resp/content-type (cond
;;                              (str/ends-with? file ".js") "application/javascript"
;;                              (str/ends-with? file ".json") "application/json"
;;                              :else "text/plain")))))
;;
;; (defn
;;   ^{:http {:path "/pictures/:path"}}
;;   handle-gitbook-assets
;;   [context request]
;;   (def re request)
;;   (resp/file-response "docker.png")
;;   )
;;
;; (defn register-endpoints
;;   "Register static file serving endpoints"
;;   [context]
;;   (http/register-ns-endpoints context *ns*)
;;
;;   (http/register-endpoint
;;     context
;;     {:path "/pictures" :method :get :fn #'handle-gitbook-assets})
;;   )
