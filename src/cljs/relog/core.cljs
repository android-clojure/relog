(ns relog.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [relog.feed :as feed :refer [Feed]]
              [cljsjs.marked]
              [cljsjs.highlight]
              [cljsjs.highlight.langs.javascript]))

(.initHighlightingOnLoad js/hljs)
(.setOptions js/marked #js { "highlight" (fn [code] (do (.log js/console (.highlightAuto js/hljs code))
                                                        (.-value (.highlightAuto js/hljs code))))})

;; -------------------------k
;; Views

(defn home-page []
  [feed/Feed])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
