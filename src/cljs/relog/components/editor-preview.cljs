(ns relog.editor-preview
  (:require [reagent.core :as r]
            [cljsjs.marked]))

(defn Preview [body]
  [:div {:className "Editor_rendered Markdown"
         :dangerouslySetInnerHTML  {:__html  (js/marked body)}}])

