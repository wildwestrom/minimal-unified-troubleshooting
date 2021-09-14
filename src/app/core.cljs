(ns app.org-parser
  (:require ["unified$unified" :as unified]
            ["uniorg-parse" :as uniorg]
            #_["uniorg-rehype$default" :as rehype]
            #_["rehype-stringify$default" :as html]
            #_["rehype-raw$default" :as raw]
            #_["uniorg-extract-keywords" :refer [extractKeywords]]
            #_shadow.resource
            ))

(def ^:private processor
  (.. (unified)
      (use uniorg)))

#_(def ^:private processor
  (.. (unified)
      (use uniorg)
      (use extractKeywords)
      (use rehype)
      (use raw)
      (use html)))

(comment
  (defn process-org
    [org-string]
    (.processSync processor org-string))

  (defn org->html
    [org-string]
    (.-contents (process-org org-string)))

  (defn org->metadata
    [org-string]
    (js->clj (.-data (process-org org-string))
             :keywordize-keys :true))

  (defn post-map
    [org-string]
    {:content (org->html org-string)
     :meta    (org->metadata org-string)})

  (defn get-from-meta
    [file key]
    (-> file :meta key))

  (defn- gen-file-title
    [file]
    (get-from-meta file :filename))

  (process-org (shadow.resource/inline "posts/001-first-post.org")))
