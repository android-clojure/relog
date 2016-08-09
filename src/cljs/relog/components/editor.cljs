(ns relog.editor
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe]]
            [cljsjs.marked]
            [relog.editor-surface :refer [Surface]]
            [relog.editor-preview :refer [Preview]]
            [relog.editor-tools :as tools :refer [Toolbar]]
            [relog.editor-actions :as actions :refer [Toolbar]]))

(def initial-markdown "## Some Markdown")

(def markdown (r/atom  initial-markdown))
(def selection (r/atom {:start 0 :end 0}))

(defn onSelection [updatedSelection]
  (reset! selection updatedSelection))

(defn Editor []
  (let [current-post (subscribe [:current-post])]
    (r/create-class
       {:reagent-render
        (fn []
          (let [current @current-post
                body (or (:body @current-post) @markdown)
                selection @selection]
            [:div {:className "Editor grid"}
              [:div {:className "Editor_header grid-row"}
                [:div {:className "grid-col-xs-6"}
                 [tools/Toolbar current selection]]
                [:div {:className "grid-col-xs-6"}
                 [actions/Toolbar]]]
              [:div {:className "Editor_surface grid-row"}
                [:div {:className "grid-col-xs-6"}
                 [Surface body onSelection]]
                [:div {:className "grid-col-xs-6"}
                 [Preview body]]]]))})))
