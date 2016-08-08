(ns relog.editor-tools
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch]]))

(defn hasSelection? [selection]
  (not= (:start selection) (:end selection)))

(defn splice-text [[start-sym end-sym] {start :start end :end} value]
  (let [a (subs value 0 start) b (subs value start end) c (subs value end (count value))]
    (str a start-sym b end-sym c)))

(defn onStyleSelector [symbols current-post selection]
  (if (hasSelection? selection)
    (let [styled (splice-text symbols selection (:body current-post))]
      (dispatch [:change-current-post (assoc current-post :body styled)]))))

(defn Bold [current selection]
  [:button {:onClick #(onStyleSelector ["**" "**"] current selection)} "B"])

(defn Italic [current selection]
  [:button {:onClick #(onStyleSelector ["*" "*"] current selection)} "i"])

(defn Code [current selection]
  [:button {:onClick #(onStyleSelector ["`" "`"] current selection)} "<>"])

(defn JsCodeBlock [current selection]
  [:button {:onClick #(onStyleSelector ["```js\n" "\n```"] current selection)} "js"])

(defn BulletList [current selection]
  [:button {:onClick #(onStyleSelector ["- " ""] current selection)} "•••"])

(defn Blockquote [current selection]
  [:button {:onClick #(onStyleSelector ["> " ""] current selection)} ">"])


