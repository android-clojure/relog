(ns relog.actions.feed
  (:require-macros [cljs.core.async.macros :refer  [go]])
  (:require [reagent.core :as r]
            [cljs-http.client :as http]
            [cljs.core.async :refer  [<!]]))

(defn fetchFeed []
  (go (let [response (<! (http/get "http://localhost:3000/api/feed"))]
      (prn  (aget (JSON/parse (:body response)) "some")))))
