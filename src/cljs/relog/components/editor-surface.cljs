(ns relog.editor-surface
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]))

(defn onChange [e current-post]
  (dispatch [:change-current-post (assoc current-post :body e.target.value)]))

(defn onSelect [e current-post onSelection]
  (do (onSelection {:start e.target.selectionStart :end e.target.selectionEnd})
      (dispatch [:change-current-post (assoc current-post :body e.target.value)])))

(defn Surface [default-body onSelection]
  (let [current-post (subscribe [:current-post])]
    (r/create-class
      {:component-did-update
        (fn [this]
          (set! (-> this .-refs .-ta .-value) (or (:body @current-post) default-body)))
      :reagent-render
        (fn [default-body onSelection]
          (let [current @current-post]
            [:textArea {:ref "ta"
                       :className "Editor_markdown_area"
                       :onChange #(onChange % current)
                       :onSelect #(onSelect % current onSelection)
                       :defaultValue default-body}]))})))
