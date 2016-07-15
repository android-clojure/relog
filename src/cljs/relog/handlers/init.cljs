(ns relog.handlers.init
  (:require [reagent.core :as r]
            [re-frame.core :refer [def-event ]]
            [relog.db :as rdb]))

(def-event
  :initialize-db
  (fn
    [db _]
    (merge db rdb/initial-state)))
