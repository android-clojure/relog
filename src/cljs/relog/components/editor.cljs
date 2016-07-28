(ns relog.editor
  (:require [reagent.core :as r]
            [re-frame.core :refer [def-sub subscribe dispatch]]
            [cljsjs.marked]
            [relog.header :as header :refer [Header]]
            [relog.footer :as footer :refer [Footer]]))

(def markdown  (r/atom  "## Some Markdown"))

(def-sub
  :current-post
  (fn
    [db _]
    (do (reset! markdown (or (:body (:current-post db)) @markdown))
    (:current-post db))))

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

(defn onChange [e currentPost]
  (dispatch [:change-current-post (assoc currentPost :body e.target.value)]))

(defn onLoad []
  (do (dispatch [:fetch-all-posts])
      (dispatch [:modal-open "post_names"])))

(defn onLoadPost [id]
  (do (dispatch [:fetch-post id])
      (closeModal "post_names")))

(defn closeModal [name]
  (dispatch [:modal-close name]))

(defn Editor []
  (let [posts (subscribe [:posts])
        modals (subscribe [:modals])
        currentPost (subscribe [:current-post])]
    (r/create-class
      {:component-did-update
        (fn [this]
          (set! (-> this .-refs .-ta .-value) @markdown))
       :reagent-render
        (fn []
          (let [postNamesClass (if (contains? @modals "post_names") " active" "")
                currentPostBody (or (:body @currentPost) "")]
            [:div {:className "Editor grid"}
              [:div {:className "Editor_header grid-row"}
                [:div {:className "Editor_header_tools grid-col-xs-6"}
                 [:button "B"]]
                [:div {:className "Editor_header_actions grid-col-xs-6"}
                 [:button "Save..."]
                 [:button {:onClick onLoad} "Load..."]
                 [:div {:className (str "Editor_post_names" postNamesClass)}
                  (for [post @posts]
                    ^{:key post} [:p {:className "Editor_post_name" :onClick #(onLoadPost (:id post))} (:name post)])
                  [:button {:onClick #(closeModal "post_names")} "X Close"]]]]
              [:div {:className "Editor_surface grid-row"}
                [:div {:className "Editor_markdown grid-col-xs-6"}
                 [:textArea {:ref "ta"
                             :className "Editor_markdown_area"
                             :onChange #(onChange % @currentPost)
                             :defaultValue @markdown}]]
                [:div {:className "Editor_rendered Markdown grid-col-xs-6"
                       :dangerouslySetInnerHTML {:__html (js/marked @markdown)}}]]]))})))
