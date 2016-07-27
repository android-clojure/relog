(ns relog.editor
  (:require [reagent.core :as r]
            [re-frame.core :refer [def-sub subscribe dispatch]]
            [cljsjs.marked]
            [relog.header :as header :refer [Header]]
            [relog.footer :as footer :refer [Footer]]))

(def-sub
  :posts
  (fn
    [db _]
    (:posts db)))

(def markdown (r/atom "## Some Markdown"))

(defn onChange [e]
  (reset! markdown e.target.value))

(defn onLoad []
  (dispatch [:fetch-all-posts]))

(defn Editor []
  (let [posts (subscribe [:posts])]
    (fn []
      [:div {:className "Editor grid"}
        [:div {:className "Editor_header grid-row"}
          [:div {:className "Editor_header_tools grid-col-xs-6"}
           [:button "B"]]
          [:div {:className "Editor_header_actions grid-col-xs-6"}
           [:button "Save..."]
           [:button {:onClick onLoad} "Load..."]
           [:div {:className "Editor_post_names"}
            (for [post @posts]
              ^{:key post} [:p (:name post)])]]]
        [:div {:className "Editor_surface grid-row"}
          [:div {:className "Editor_markdown grid-col-xs-6"}
           [:textArea {:className "Editor_markdown_area"
                       :onChange onChange
                       :defaultValue @markdown}]]
          [:div {:className "Editor_rendered Markdown grid-col-xs-6"
                 :dangerouslySetInnerHTML {:__html (js/marked @markdown)}}]]])))
