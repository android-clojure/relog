(ns relog.editor-actions
  (:require [reagent.core :as r]
            [relog.subs.editor]
            [relog.handlers.editor]
            [re-frame.core :refer [dispatch subscribe]]))

(defn closeModal [name]
  (dispatch [:modal-close name]))

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

