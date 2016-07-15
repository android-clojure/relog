(ns relog.post
  (:require [reagent.core :as r]
            [cljsjs.marked]
            [cljsjs.moment]))

(defn Post [body date authorName]
  (fn [body date authorName]
    (let [formattedDate (-> date js/moment (.format "LLL"))]
    [:div {:class "Post"}
     [:div {:class "Post_header grid grid-row"}
       [:div {:class "Post_author grid-col-xs-12 grid-col-md-6"} authorName]
       [:div {:class "Post_date grid-col-xs-12 grid-col-md-6"} formattedDate]]
     [:div {:class "Post_content" :dangerouslySetInnerHTML
            {:__html (js/marked body)}}]])))


