(ns relog.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [re-frame.core :refer [dispatch-sync, dispatch]]
              [relog.feed :as feed :refer [Feed]]
              [relog.editor :as editor :refer [Editor]]
              [relog.handlers.init]
              [relog.handlers.feed]
              [relog.handlers.editor]
              [relog.subs.editor]
              [cljsjs.marked]
              [cljsjs.highlight]
              [cljsjs.highlight.langs.javascript]))

(.initHighlightingOnLoad js/hljs)
(.setOptions js/marked #js { "highlight" #(.-value (.highlightAuto js/hljs %))})

;; -------------------------
;; Views

(defn home-page []
  [feed/Feed])

(defn editor []
  [editor/Editor])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/editor" []
  (session/put! :current-page #'editor))

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
  (dispatch-sync [:initialize-db])
  (mount-root))
