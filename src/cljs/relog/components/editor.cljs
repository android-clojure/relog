(ns relog.editor
  (:require [reagent.core :as r]
            [re-frame.core :refer [def-sub subscribe dispatch]]
            [cljsjs.marked]
            [relog.subs.editor]
            [relog.header :as header :refer [Header]]
            [relog.editor-tools :as tools :refer [Bold Italic Code JsCodeBlock BulletList Blockquote]]
            [relog.editor-actions :as actions :refer [New Save Load]]
            [relog.footer :as footer :refer [Footer]]))

(def initial-markdown "## Some Markdown")

(def markdown (r/atom  initial-markdown))
(def selection (r/atom {:start 0 :end 0}))

(defn onChange [e current-post]
  (dispatch [:change-current-post (assoc current-post :body e.target.value)]))

(defn onSelect [e current-post]
  (do (reset! selection {:start e.target.selectionStart :end e.target.selectionEnd})
      (dispatch [:change-current-post (assoc current-post :body e.target.value)])))

(defn Editor []
  (let [current-post (subscribe [:current-post])]
    (r/create-class
      {:component-did-update
        (fn [this]
          (set! (-> this .-refs .-ta .-value) (or (:body @current-post) @markdown)))
       :reagent-render
        (fn []
          (let [current @current-post
                body (:body @current-post)
                selection @selection
                markdown @markdown]
            [:div {:className "Editor grid"}
              [:div {:className "Editor_header grid-row"}
                [:div {:className "Editor_header_tools grid-col-xs-6"}
                 [tools/Bold current selection]
                 [tools/Italic current selection]
                 [tools/Blockquote current selection]
                 [tools/Code current selection]
                 [tools/JsCodeBlock current selection]
                 [tools/BulletList current selection]]
                [:div {:className "Editor_header_actions grid-col-xs-6"}
                 [actions/New]
                 [actions/Save]
                 [actions/Load]]]
              [:div {:className "Editor_surface grid-row"}
                [:div {:className "Editor_markdown grid-col-xs-6"}
                 [:textArea {:ref "ta"
                             :className "Editor_markdown_area"
                             :onChange #(onChange % current)
                             :onSelect #(onSelect % current)
                             :defaultValue markdown}]]
                [:div {:className "Editor_rendered Markdown grid-col-xs-6"
                       :dangerouslySetInnerHTML {:__html (js/marked (or body markdown))}}]]]))})))
