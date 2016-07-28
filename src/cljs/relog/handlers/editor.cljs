(ns relog.handlers.editor
  (:require-macros [cljs.core.async.macros :refer  [go]])
  (:require [reagent.core :as r]
            [re-frame.core :refer [def-event dispatch path trim-v]]
            [cljs-http.client :as http]
            [cljs.core.async :refer  [<!]]))

(def base-uri "http://localhost:3000/api")

(def-event
  :fetch-post
  (fn [db [_ id]]
  (go (let [response (<! (http/get (str base-uri "/post/" id)))]
        (let [json (JSON/parse (:body response))]
          (dispatch [:post-received (js->clj json :keywordize-keys true)]))))
  db))

(def-event
  :post-received
  (fn [db [_ postResponse]]
  (-> db
      (assoc :current-post postResponse))))

(def-event
  :change-current-post
  (fn [db [_ changed]]
    (-> db
        (assoc :current-post changed))))

(def-event
  :fetch-all-posts
  (fn [db _]
  (go (let [response (<! (http/get (str base-uri "/posts")))]
        (let [json (JSON/parse (:body response))]
          (dispatch [:posts-received (js->clj json :keywordize-keys true)]))))
  db))

(def-event
  :posts-received
  (fn [db [_ postsResponse]]
  (-> db
      (assoc :posts postsResponse))))

(def modal-middleware [(path :modals) trim-v])

(def-event
  :modal-open
  modal-middleware
  (fn [modals [name]]
    (conj modals name)))

(def-event
  :modal-close
  modal-middleware
  (fn [modals [name]]
    (disj modals name)))


