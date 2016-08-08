(ns relog.editor-tools
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch]]))

(defn hasSelection? [selection]
  (not= (:start selection) (:end selection)))

(defn splice-text [[start-sym end-sym] {start :start end :end} value]
  (let [a (subs value 0 start) b (subs value start end) c (subs value end (count value))]
    (str a start-sym b end-sym c)))

(defn onClickStyleSelector [symbols current-post selection]
  (if (hasSelection? selection)
    (let [styled (splice-text symbols selection (:body current-post))]
      (dispatch [:change-current-post (assoc current-post :body styled)]))))

(defn styleSelector [symbols [current selection]]
  #(onClickStyleSelector symbols current selection))

(defn Bold [& props]
  [:button {:onClick (styleSelector ["**" "**"] props)} "B"])

(defn Italic [& props]
  [:button {:onClick (styleSelector ["*" "*"] props)} "i"])

(defn Code [& props]
  [:button {:onClick (styleSelector ["`" "`"] props)} "<>"])

(defn JsCodeBlock [& props]
  [:button {:onClick (styleSelector ["```js\n" "\n```"] props)} "js"])

(defn BulletList [& props]
  [:button {:onClick (styleSelector ["- " ""] props)} "•••"])

(defn Blockquote [& props]
  [:button {:onClick (styleSelector ["> " ""] props)} ">"])


