(ns relog.actions.feed
  (:require-macros [cljs.core.async.macros :refer  [go]])
  (:require [reagent.core :as r]
            [cljs-http.client :as http]
            [cljs.core.async :refer  [<!]]))

(def posts (r/atom []))

(defn fetchFeed [p]
  (go (let [response (<! (http/get "http://localhost:3000/api/feed"))]
      (let [json (JSON/parse (:body response))]
        (reset! p (js->clj json))))))

(fetchFeed posts)
