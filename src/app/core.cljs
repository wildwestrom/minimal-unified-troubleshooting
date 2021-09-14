(ns app.core
  (:require ["unified$unified" :as unified]
            ["uniorg-parse" :as uniorg]
            [reagent.core :as reagent]
            [reagent.dom :as r.dom]
            ["uniorg-rehype$default" :as rehype]
            ["rehype-stringify$default" :as html]
            ["rehype-raw$default" :as raw]
            ["uniorg-extract-keywords" :refer [extractKeywords]]
            ))

(def ^:private processor
  (.. (unified)
      (use uniorg)
      (use extractKeywords)
      (use rehype)
      (use raw)
      (use html)))

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

(def org-text "* One heading\n** And here's another heading")

(defn app
  []
  [:<>
   [:h3 "Holy fuckeroni, is this working? I guess it is if you can see it."]
   [:br]
   [:p "Ok, now let's see if this works:"]
   [:p org-text]
   [:p (org->html org-text)]])

(defn mount-root
  [component]
  (r.dom/render component (.getElementById js/document "app")))

(defn ^:export init
  []
  (js/console.debug "init")
  (mount-root [app]))

(defn ^:dev/before-load stop
  []
  (js/console.debug "stop"))
