(ns relog.editor-actions
  (:require [reagent.core :as r]
            [relog.subs.editor]
            [relog.handlers.editor]
            [re-frame.core :refer [dispatch subscribe]]))

(def initial-markdown "## Some Markdown")

(defn closeModal [name]
  (dispatch [:modal-close name]))

;; Save
(defn onSave [current-post]
  (.log js/console current-post)
  (if (:name current-post)
    (dispatch [:save-current-post current-post])
    (dispatch [:modal-open "name_post"])))

(defn onCreate [current-post]
  (dispatch [:create-current-post current-post]))

(defn onChangeName [e current-post]
  (dispatch [:change-current-post (assoc current-post :name e.target.value)]))

(defn Save []
  (let [current-post (subscribe [:current-post])
        modals (subscribe [:modals])]
    (fn []
      (let [current @current-post
            namePostClass (if (contains? @modals "name_post") " active" "")]
         [:div {:className "Editor_post_names_container"}
           [:button {:onClick #(onSave current)} "Save..."]
           [:div {:className (str "Editor_name_post" namePostClass)}
            [:div {:className "grid grid-row"}
             [:div {:className "grid-col-xs-10"}
              [:input {:type "text" :placeholder "name the post..." :onChange #(onChangeName % current)}]
              [:button {:onClick #(onCreate current)} "Create"]]
             [:div {:className "grid-col-xs-2"}
              [:button {:className "Editor_post_names_close" :onClick #(closeModal "name_post")} "X"]]]]]))))

;; New
(defn onNew []
  (dispatch [:change-current-post {:body initial-markdown}]))

(defn New []
  [:button {:onClick onNew} "New"])

;; Load
(defn onLoad []
  (do (dispatch [:fetch-all-posts])
      (dispatch [:modal-open "post_names"])))

(defn onLoadPost [id]
  (do (dispatch [:fetch-post id])
      (closeModal "post_names")))

(defn Load []
  (let [posts (subscribe [:posts])
        modals (subscribe [:modals])]
    (fn []
      (let [postNamesClass (if (contains? @modals "post_names") " active" "")
            posts @posts]
               [:div {:className "Editor_post_names_container"}
                 [:button {:onClick onLoad} "Load..."]
                 [:div {:className (str "Editor_post_names" postNamesClass)}
                  [:div {:className "grid grid-row"}
                    [:div {:className "grid-col-xs-10"}
                      [:ul {:className "Editor_post_name_list"} (for [post posts]
                        ^{:key post} [:li {:className "Editor_post_name" :onClick #(onLoadPost (:id post))} (:name post)])]]
                    [:div {:className "grid-col-xs-2"}
                      [:button {:className "Editor_post_names_close" :onClick #(closeModal "post_names")} "X"]]]]]))))
