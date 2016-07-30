(ns relog.middleware
  (:require [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.json :as json-middleware]
            [ring.middleware.reload :refer [wrap-reload]]))

(defonce very-insecure-settings  {:security  {:anti-forgery false}})

(defn wrap-middleware [handler]
  (-> handler
      (wrap-defaults (merge site-defaults very-insecure-settings))
      json-middleware/wrap-json-params
      json-middleware/wrap-json-response
      wrap-exceptions
      wrap-reload))
