(ns relog.feed
  (:require [reagent.core :as r]
            [re-frame.core :refer [def-sub subscribe dispatch]]
            [relog.header :as header :refer [Header]]
            [relog.footer :as footer :refer [Footer]]
            [relog.post :as post :refer [Post]]))

(def-sub
  :feed
  (fn
    [db _]
    (:feed db)))

(defn Feed []
  (let [feed (subscribe [:feed])]
    (r/create-class
      {:component-did-mount
        #(dispatch [:fetch-feed])
       :reagent-render
        (fn []
          [:div
           [header/Header]
           [:div {:class "grid grid-row"}
            [:div {:class "grid-col-xs-12 grid-col-md-8"}
             (for  [post @feed]
              ^{:key post} [post/Post (:body post) (:date post) (str (:firstName post) " " (:lastName post))])]
            [:div {:class "grid-col-xs-12 grid-col-md-4"}]]
           [footer/Footer]])})))

