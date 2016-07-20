(ns relog.editor
  (:require [reagent.core :as r]
            [re-frame.core :refer [def-sub subscribe dispatch]]
            [cljsjs.marked]
            [relog.header :as header :refer [Header]]
            [relog.footer :as footer :refer [Footer]]))

(def markdown (r/atom "## Some Markdown"))

(defn onChange [e]
    (reset! markdown e.target.value))

(defn Editor []
  (fn []
    [:div {:className "Editor grid grid-row"}
      [:div {:className "Editor_markdown grid-col-xs-6"}
       [:textArea {:className "Editor_markdown_area"
                   :onChange onChange
                   :defaultValue @markdown}]]
      [:div {:className "Editor_rendered Markdown grid-col-xs-6"
             :dangerouslySetInnerHTML {:__html (js/marked @markdown)}}]]))
