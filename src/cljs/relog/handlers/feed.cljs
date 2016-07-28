(ns relog.handlers.feed
  (:require-macros [cljs.core.async.macros :refer  [go]])
  (:require [reagent.core :as r]
            [re-frame.core :refer [def-event dispatch]]
            [cljs-http.client :as http]
            [cljs.core.async :refer  [<!]]))

(def base-uri "http://localhost:3000/api")

(def-event
  :fetch-feed
  (fn [db _]
  (go (let [response (<! (http/get (str base-uri  "/feed")))]
        (let [json (JSON/parse (:body response))]
          (dispatch [:feed-received (js->clj json :keywordize-keys true)]))))
  db))

(def-event
  :feed-received
  (fn [db [_ feedResponse]]
  (-> db
      (assoc :feed feedResponse))))

