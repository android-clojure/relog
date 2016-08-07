(ns relog.editor-tools
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch]]))

(defn Bold [{onClick :onClick}]
  [:button {:onClick #(onClick ["**" "**"])} "B"])

(defn Italic [{onClick :onClick}]
  [:button {:onClick #(onClick ["*" "*"])} "i"])

(defn Code [{onClick :onClick}]
  [:button {:onClick #(onClick ["`" "`"])} "<>"])

(defn JsCodeBlock [{onClick :onClick}]
  [:button {:onClick #(onClick ["```js\n" "\n```"])} "js"])

(defn BulletList [{onClick :onClick}]
  [:button {:onClick #(onClick ["- " ""])} "•••"])

(defn Blockquote [{onClick :onClick}]
  [:button {:onClick #(onClick ["> " ""])} ">"])


