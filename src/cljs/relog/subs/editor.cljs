(ns relog.subs.editor
  (:require [reagent.core :as r]
            [re-frame.core :refer [def-sub]]))

(def-sub
  :current-post
  (fn
    [db _]
      (:current-post db)))

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

