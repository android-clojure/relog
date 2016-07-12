(ns relog.header
  (:require [reagent.core :as r]))

(defn Header []
  [:header {:class "Header"} [:h2 {:class "Header-title"} "RELOG"]])
