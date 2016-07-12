(ns relog.feed
  (:require [reagent.core :as r]
            [relog.header :as header :refer [Header]]
            [relog.footer :as footer :refer [Footer]]
            [relog.post :as post :refer [Post]]))

(defn Feed []
  [:div
   [header/Header]
   [post/Post]
   [footer/Footer]])
