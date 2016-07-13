(ns relog.post
  (:require [reagent.core :as r]
            [cljsjs.marked]))

(defn Post [body]
  (fn []
    [:div {:class "Post"}
     [:div {:class "Post_content" :dangerouslySetInnerHTML
            {:__html (js/marked body)} }]]))


