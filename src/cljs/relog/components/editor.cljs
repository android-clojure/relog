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

(def-sub
  :modals
  (fn
    [db _]
    (:modals db)))

(def markdown (r/atom "## Some Markdown"))

(defn onChange [e]
  (reset! markdown e.target.value))

(defn onLoad []
  (do (dispatch [:fetch-all-posts])
      (dispatch [:modal-open "post_names"])))

(defn closeModal [name]
  (dispatch [:modal-close name]))

(defn Editor []
  (let [posts (subscribe [:posts])
        modals (subscribe [:modals])]
    (fn []
      (let [postNamesClass (if (contains? @modals "post_names") " active" "")]
        [:div {:className "Editor grid"}
          [:div {:className "Editor_header grid-row"}
            [:div {:className "Editor_header_tools grid-col-xs-6"}
             [:button "B"]]
            [:div {:className "Editor_header_actions grid-col-xs-6"}
             [:button "Save..."]
             [:button {:onClick onLoad} "Load..."]
             [:div {:className (str "Editor_post_names" postNamesClass)}
              (for [post @posts]
                ^{:key post} [:p (:name post)])
              [:button {:onClick #(closeModal "post_names")} "X Close"]]]]
          [:div {:className "Editor_surface grid-row"}
            [:div {:className "Editor_markdown grid-col-xs-6"}
             [:textArea {:className "Editor_markdown_area"
                         :onChange onChange
                         :defaultValue @markdown}]]
            [:div {:className "Editor_rendered Markdown grid-col-xs-6"
                   :dangerouslySetInnerHTML {:__html (js/marked @markdown)}}]]]))))
