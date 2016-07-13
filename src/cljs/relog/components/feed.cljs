(ns relog.feed
  (:require [reagent.core :as r]
            [relog.actions.feed :as actions]
            [relog.header :as header :refer [Header]]
            [relog.footer :as footer :refer [Footer]]
            [relog.post :as post :refer [Post]]))

(defn Feed []
  (fn []
  [:div
   [header/Header]
   [:div {:class "grid grid-row"}
    [:div {:class "grid-col-xs-12 grid-col-md-8"}
     (for  [post @actions/posts]
      ^{:key post} [post/Post (-> post :post :body)])]
    [:div {:class "grid-col-xs-12 grid-col-md-4"}]]
   [footer/Footer]]))