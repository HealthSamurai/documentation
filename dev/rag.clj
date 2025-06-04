(ns rag
  (:require [clojure.java.io :as io] [clojure.string :as str])
  (:import
   [gitbok.ai AssistantWithSources]
   [java.util.concurrent CompletableFuture]
   [java.time Duration]
   [java.util Collections]
   [dev.langchain4j.memory.chat MessageWindowChatMemory]
   [dev.langchain4j.data.document Document Metadata]
   [dev.langchain4j.data.document.splitter DocumentSplitters]
   [dev.langchain4j.rag.content.injector DefaultContentInjector]
   [dev.langchain4j.model.chat.response StreamingChatResponseHandler]
   [dev.langchain4j.data.segment TextSegment]
   [dev.langchain4j.rag.query Query]
   [dev.langchain4j.service Result]
   [dev.langchain4j.rag.query.router QueryRouter]
   [dev.langchain4j.model.input Prompt PromptTemplate]
   [dev.langchain4j.data.message AiMessage]
    ;; [dev.langchain4j.model.chat ChatLanguageModel]
   [dev.langchain4j.rag.content.aggregator ReRankingContentAggregator]
   [dev.langchain4j.model.cohere CohereScoringModel]
   [dev.langchain4j.model.ollama OllamaChatModel]
   [dev.langchain4j.model.ollama OllamaStreamingChatModel]
   [dev.langchain4j.rag.query.transformer QueryTransformer]
   [dev.langchain4j.rag DefaultRetrievalAugmentor]
   [dev.langchain4j.rag.query.transformer CompressingQueryTransformer]
   [dev.langchain4j.model.embedding.onnx.allminilml6v2 AllMiniLmL6V2EmbeddingModel]
   [dev.langchain4j.model.embedding.onnx.bgesmallenv15q BgeSmallEnV15QuantizedEmbeddingModel]
   [dev.langchain4j.store.embedding EmbeddingSearchRequest]
   [dev.langchain4j.store.embedding EmbeddingStoreIngestor]
   [dev.langchain4j.service AiServices]
   [dev.langchain4j.service TokenStream]
   [dev.langchain4j.rag.content.retriever EmbeddingStoreContentRetriever]
   [dev.langchain4j.store.embedding.inmemory InMemoryEmbeddingStore]))

(def cohere
  "0hIYrxVL7TZubD1Zkozec2E0NSdVAsEKCQt8Chdf")

(defn text->document [text filepath]
  (let [d (Document/from text)
        m (.metadata d)]
    ;; todo filepath -> url
    (.put m "url" filepath)
    d))

;; (defn split-text-to-chunks [text]
;;   (let [doc (text->document text)]
;;     (.split (DocumentSplitters/recursive 1000 200) doc)))

;; (defn replace-t-with-space [segments]
;;   (map
;;    (fn [segment]
;;      (let [cleaned-text (-> segment .text (str/replace #"\t" " "))
;;            meta (-> segment .metadata)]
;;
;;        (TextSegment/from cleaned-text meta)))
;;    segments))

(def docs-dir "docs")

(def files
  (->> (file-seq (io/file docs-dir))
       (filter #(and (.isFile %)
                     (str/ends-with? (.getName %) ".md")))))

(defonce embedding-model (BgeSmallEnV15QuantizedEmbeddingModel.))
(defonce embedding-store (InMemoryEmbeddingStore.))

;; (defonce hui (index-docs embedding-model embedding-store))

(defn ollama-chat-model [url model-name]
  (-> (OllamaChatModel/builder)
      (.baseUrl url)
      (.modelName model-name)
      (.temperature 0.8)
      (.timeout (Duration/ofSeconds 600))
      (.build)))

;; (defn ollama-streaming-chat-model [url model-name]
;;   (-> (OllamaStreamingChatModel/builder)
;;       (.baseUrl url)
;;       (.modelName model-name)
;;       (.temperature 0.0)
;;       (.logRequests true)
;;       (.logResponses true)
;;       (.timeout (Duration/ofSeconds 60))
;;       (.build)))

;; (defn stream-chat [^OllamaStreamingChatModel model question]
;;   (let [future (CompletableFuture.)
;;         stream ^dev.langchain4j.service.TokenStream (.chat model question)]
;;     (->
;;      stream
;;      (.onRetrieved
;;       (reify java.util.function.Consumer
;;         (accept [_ contents]
;;           (println "retrieved ")
;;           (def contents contents))))
;;      (.onPartialResponse
;;       (reify java.util.function.Consumer
;;         (accept [_ s]
;;             ;; (println "partial response")
;;           (print s))))
;;      (.onCompleteResponse
;;       (reify java.util.function.Consumer
;;         (accept [_ response]
;;           (def res response)
;;           (println " on complete response")
;;           (.complete future response))))
;;      (.onError
;;       (reify java.util.function.Consumer
;;         (accept [_ error]
;;           (println "error ...")
;;           (def error error)
;;           (.completeExceptionally future error))))
;;      (.start))
;;     (.join future)))

(defn build-retriever [emb-model
                       emb-store
                       top-k]
  (-> (EmbeddingStoreContentRetriever/builder)
      (.embeddingModel emb-model)
      (.embeddingStore emb-store)
      (.maxResults (int top-k))
      (.minScore 0.8)
      (.build)))

(defn build-content-injector []
  (-> (DefaultContentInjector/builder)
      (.metadataKeysToInclude ["url"])
      (.build)))

(defn build-augmentor [query-transformer
                       retriever content-aggregator
                       query-router
                       content-injector]
  (-> (DefaultRetrievalAugmentor/builder)
      (.queryTransformer query-transformer)
      (.contentRetriever retriever)
      (.contentAggregator content-aggregator)
      (.queryRouter query-router)
      (.contentInjector content-injector)
      (.build)))

(defn build-scoring-model [cohere-api-key]
  (-> (CohereScoringModel/builder)
      (.apiKey cohere-api-key)
      (.modelName "rerank-v3.5")
      (.build)))

(defn ingest-files [ingestor files]
  (doseq [f files]
    (println f)
    (let [doc (text->document (slurp f) (.getPath f))]
      (.ingest ingestor doc))))

(defn build-ingestor [emb-model emb-store files]
  (let [ingestor (->
                  (EmbeddingStoreIngestor/builder)
                  (.documentSplitter (DocumentSplitters/recursive 300 0))
                  (.embeddingModel emb-model)
                  (.embeddingStore emb-store)
                  (.build))]
    (ingest-files ingestor files)
    ingestor))

;; streaming
;; (definterface Assistant (^dev.langchain4j.service.TokenStream chat [^String message]))
;; (definterface Assistant
;;   (^String chat [^String message]))

;; (definterface AssistantWithSources
;;   (^dev.langchain4j.service.Result chat [^String message]))

;; (defn build-streaming-assistant [retriever chat-model]
;;   (-> (AiServices/builder Assistant)
;;       (.streamingChatModel chat-model)
;;       (.contentRetriever retriever)
;;       (.build)))

(defn build-assistant [chat-model augmentor]
  (->
    ;; (AiServices/builder Assistant)
   (AiServices/builder gitbok.ai.AssistantWithSources)
   (.chatModel chat-model)
   (.chatMemory (MessageWindowChatMemory/withMaxMessages 10))
   (.retrievalAugmentor augmentor)
   (.build)))

;; (defn ask [minion query]
;;   (stream-chat minion query))

(def ollama-url "http://localhost:11434")
(def model-name "gemma3:1b")

(def prompt
  "Is the following query may be related to the Aidbox documentation or FHIR documentation or programming?
Answer only 'yes', 'no' or 'maybe'.
Query: {{it}}")

(def prompt-template (PromptTemplate/from prompt))

(defn build-query-router [chat-model content-retriever]
  (reify QueryRouter
    (^java.util.Collection route [_ ^Query query]
      (let [^Prompt prompt
            (.apply prompt-template (.text query))
            p (.toUserMessage prompt)
            text (str/join "\n" (mapv #(.text %) (.contents p)))
            res (.chat chat-model ^String text)]
        (println "LLM decided that question is related to doc:" res)
        (if (.contains res "no")
          (Collections/emptyList)
          (Collections/singletonList content-retriever))))))

(def chat-model (ollama-chat-model ollama-url model-name))
;; (def chat-model (ollama-streaming-chat-model ollama-url model-name))

(def retriever (build-retriever embedding-model embedding-store 20))

(def query-transformer
  (CompressingQueryTransformer. chat-model))

(defn build-content-aggregator [scoring-model]
  (-> (ReRankingContentAggregator/builder)
      (.scoringModel scoring-model)
      (.minScore 0.8)
      (.build)))

(defonce ingestor (build-ingestor embedding-model embedding-store files))

(def scoring-model (build-scoring-model cohere))

(def content-aggregator (build-content-aggregator scoring-model))

(def query-router (build-query-router chat-model retriever))

(def content-injector (build-content-injector))

(def augmentor (build-augmentor
                query-transformer
                retriever
                content-aggregator
                query-router
                content-injector))

(def assistant (build-assistant chat-model augmentor))
;; (def assistant (build-minion retriever chat-model))

(defn answer-with-sources [assistant user-query]
  (let [result (.chat assistant user-query)]
    (str (.content result)
         (when (and (.sources result) (seq (.sources result)))
           (str "\n\nSources:"
                (->> (.sources result)
                     (mapv
                       (fn [source]
                         (str "Text: " (.text (.textSegment source))
                              "\n"
                              "Source: " (-> source
                                             .textSegment
                                             .metadata
                                             (.getString "url")))))
                     (str/join "\n")))))))

(println "done!")
(comment
  ;; https://github.com/langchain4j/langchain4j/pull/538
  (def q "What is Aidbox?")
  (time (.chat assistant q))
   ;; "Elapsed time: 84551.447513 msecs"

  (def q "hello")
  (time (answer-with-sources assistant q))
  ;; LLM decided that question is related to doc: no
  ;; "Hello there! How's your day going so far? 😊 \n\nIs there anything you'd like to chat about or anything I can help you with?"
  ;; "Elapsed time: 8130.236074 msecs"

  (def q "What are FHIR Search capabilities in Aidbox?")
  (time (answer-with-sources assistant q))
  ;; "Elapsed time: 33930.018555 msecs"


  (def q "How to define new endpoint for search using SQL?")
  (time (answer-with-sources assistant q))
  ;; timeout
  )
