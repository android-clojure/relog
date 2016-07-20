(ns relog.editor
  (:require [reagent.core :as r]
            [re-frame.core :refer [def-sub subscribe dispatch]]
            [relog.header :as header :refer [Header]]
            [relog.footer :as footer :refer [Footer]]))

(defn Editor []
  [:div {:contentEditable true
         :onInput #(.log js/console %)
         :dangerouslySetInnerHTML {:__html "<p>the markdown editor</p>"}}])
