(ns relog.handler
  (:require [compojure.core :refer [GET PUT defroutes]]
            [compojure.route :refer [not-found resources]]
            [clojure.data.json :as json]
            [hiccup.page :refer [include-js include-css html5]]
            [relog.middleware :refer [wrap-middleware]]
            [relog.db.query :as q]
            [relog.db.transact :as t]
            [config.core :refer [env]]))

(def mount-target
  [:div#app])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   [:title "relog"]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(def loading-page
  (html5
    (head)
    [:body {:class "body-container"}
     mount-target
     (include-js "/js/app.js")]))

(defn my-value-writer [key value]
  (if (= key :date)
    (str (java.sql.Timestamp. (.getTime value)))
    value))

(defn my-value-reader [key value]
  (if (= key :date)
    (java.sql.Date/valueOf value)
    value))

(defroutes routes
  (GET "/" [] loading-page)
  (GET "/api/feed" [] (json/write-str (q/getPosts)
                                      :value-fn my-value-writer
                                      :key-fn name))

  (GET "/api/posts" [] (json/write-str (q/getPostNames)
                                       :value-fn my-value-writer
                                       :key-fn name))

  (GET "/api/post/:id" [id] (json/write-str (q/getPost id)
                                       :value-fn my-value-writer
                                       :key-fn name))

  (PUT  "/api/post/:id" req [] (let [post (get-in req [:params]) id (:id post)]
                                  (t/savePost post)
                                  (json/write-str (q/getPost id)
                                       :value-fn my-value-writer
                                       :key-fn name)))

  (resources "/")
  (not-found loading-page))

(def app (wrap-middleware #'routes))
