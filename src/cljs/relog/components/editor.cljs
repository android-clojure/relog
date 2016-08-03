(ns relog.editor
  (:require [reagent.core :as r]
            [re-frame.core :refer [def-sub subscribe dispatch]]
            [cljsjs.marked]
            [relog.header :as header :refer [Header]]
            [relog.footer :as footer :refer [Footer]]))

(def initial-markdown "## Some Markdown")
(def markdown (r/atom  initial-markdown))

(def selection (r/atom {:start 0 :end 0}))

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

(defn closeModal [name]
  (dispatch [:modal-close name]))

(defn onChange [e current-post]
  (dispatch [:change-current-post (assoc current-post :body e.target.value)]))

(defn onSelect [e current-post]
  (do (reset! selection {:start e.target.selectionStart :end e.target.selectionEnd})
      (dispatch [:change-current-post (assoc current-post :body e.target.value)])))

(defn hasSelection? []
  (not= (:start @selection) (:end @selection)))

(defn onChangeName [e current-post]
  (dispatch [:change-current-post (assoc current-post :name e.target.value)]))

(defn onSave [current-post]
  (if (:name current-post)
    (dispatch [:save-current-post current-post])
    (dispatch [:modal-open "name_post"])))

(defn onCreate [current-post]
  (dispatch [:create-current-post current-post]))

(defn onNew []
  (dispatch [:change-current-post {:body initial-markdown}]))

(defn onLoad []
  (do (dispatch [:fetch-all-posts])
      (dispatch [:modal-open "post_names"])))

(defn onLoadPost [id]
  (do (dispatch [:fetch-post id])
      (closeModal "post_names")))

(defn splice-text [sym {start :start end :end} value]
  (let [a (subs value 0 start) b (subs value start end) c (subs value end (count value))]
    (str a sym b sym c)))

(defn onStyleSelector [sym current-post]
  (if (hasSelection?)
    (let [styled (splice-text sym @selection (:body current-post))]
      (dispatch [:change-current-post (assoc current-post :body styled)]))))

(defn Editor []
  (let [posts (subscribe [:posts])
        modals (subscribe [:modals])
        current-post (subscribe [:current-post])]
    (r/create-class
      {:component-did-update
        (fn [this]
          (set! (-> this .-refs .-ta .-value) @markdown))
       :reagent-render
        (fn []
          (let [postNamesClass (if (contains? @modals "post_names") " active" "")
                namePostClass (if (contains? @modals "name_post") " active" "")
                current @current-post
                markdown @markdown
                posts @posts]
            [:div {:className "Editor grid"}
              [:div {:className "Editor_header grid-row"}
                [:div {:className "Editor_header_tools grid-col-xs-6"}
                 [:button {:onClick #(onStyleSelector "**" current)} "B"]
                 [:button {:onClick #(onStyleSelector "*" current)} "i"]
                 [:button {:onClick #(onStyleSelector "`" current)} "<>"]]
                [:div {:className "Editor_header_actions grid-col-xs-6"}
                 [:button {:onClick onNew} "New"]
                 [:div {:className "Editor_post_names_container"}
                   [:button {:onClick #(onSave current)} "Save..."]
                   [:div {:className (str "Editor_name_post" namePostClass)}
                    [:div {:className "grid grid-row"}
                     [:div {:className "grid-col-xs-10"}
                      [:input {:type "text" :placeholder "name the post..." :onChange #(onChangeName % current)}]
                      [:button {:onClick #(onCreate current)} "Create"]]
                     [:div {:className "grid-col-xs-2"}
                      [:button {:className "Editor_post_names_close" :onClick #(closeModal "name_post")} "X"]]]]]
                 [:div {:className "Editor_post_names_container"}
                   [:button {:onClick onLoad} "Load..."]
                   [:div {:className (str "Editor_post_names" postNamesClass)}
                    [:div {:className "grid grid-row"}
                      [:div {:className "grid-col-xs-10"}
                        [:ul {:className "Editor_post_name_list"} (for [post posts]
                          ^{:key post} [:li {:className "Editor_post_name" :onClick #(onLoadPost (:id post))} (:name post)])]]
                      [:div {:className "grid-col-xs-2"}
                        [:button {:className "Editor_post_names_close" :onClick #(closeModal "post_names")} "X"]]]]]]]
              [:div {:className "Editor_surface grid-row"}
                [:div {:className "Editor_markdown grid-col-xs-6"}
                 [:textArea {:ref "ta"
                             :className "Editor_markdown_area"
                             :onChange #(onChange % current)
                             :onSelect #(onSelect % current)
                             :defaultValue markdown}]]
                [:div {:className "Editor_rendered Markdown grid-col-xs-6"
                       :dangerouslySetInnerHTML {:__html (js/marked markdown)}}]]]))})))
