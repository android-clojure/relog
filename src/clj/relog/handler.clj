(ns relog.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [clojure.data.json :as json]
            [hiccup.page :refer [include-js include-css html5]]
            [relog.middleware :refer [wrap-middleware]]
            [config.core :refer [env]]))

(def mount-target
  [:div#app])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(def loading-page
  (html5
    (head)
    [:body {:class "body-container"}
     mount-target
     (include-js "/js/app.js")]))

(def feedResponse [
                   {:body "# Markdown test

  Some random test markdown. *Italics.*

  ```javascript
  function foo ()  {
    var bar = 'baz';
    return  {
     test: true;
    }
  }
  ```

  More text." :publishDate "2016-07-01T00:00:00Z"}
                   {:body "# Markdown test 2

  Some random test markdown. *Italics.*

  ```javascript
  function foo ()  {
    var bar = 'baz';
    return  {
     test: true;
    }
  }
  ```

  More text." :publishDate "2016-07-02T00:00:00Z"}
                   ])

(defroutes routes
  (GET "/" [] loading-page)
  (GET "/hello/:name" [name] (str "hello there " name ", you well?"))

  (GET "/api/feed" [] (json/write-str feedResponse))

  (resources "/")
  (not-found loading-page))

(def app (wrap-middleware #'routes))
