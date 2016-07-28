(ns relog.db.query
  (:require [datomic.api :as d]
            [clojure.pprint :refer [pprint]]
            [config.core :refer [env]]))

(defn getPosts []
  (def conn (d/connect (:db-uri env)))
  (def db (d/db conn))
  (->> (d/q '[:find ?post ?name ?body ?firstName ?lastName ?date
         :where [?post :post/body ?body ?tx]
                [?post :post/name ?name]
                [?post :post/author ?author]
                [?author :author/firstName ?firstName]
                [?author :author/lastName ?lastName]
                [?tx :db/txInstant ?date]]
       db)
       (map #(zipmap [:id :name :body :firstName :lastName :date] %))
       (sort-by :tx)))

(defn getPost [id]
  (def conn (d/connect (:db-uri env)))
  (def db (d/db conn))
  (def post (d/entity db (read-string id)))
  (def post-id (:db/id post))
  (->> (d/q '[:find ?post ?name ?body ?firstName ?lastName ?date
         :in $ ?post
         :where [?post :post/body ?body ?tx]
                [?post :post/name ?name]
                [?post :post/author ?author]
                [?author :author/firstName ?firstName]
                [?author :author/lastName ?lastName]
                [?tx :db/txInstant ?date]]
       db post-id)
       (map #(zipmap [:id :name :body :firstName :lastName :date] %))
       (sort-by :tx)
       first))

(defn getPostNames []
  (def conn (d/connect (:db-uri env)))
  (def db (d/db conn))
  (->> (d/q '[:find ?post ?name ?date
         :where [?post :post/name ?name ?tx]
                [?tx :db/txInstant ?date]]
       db)
       (map #(zipmap [:id :name :date] %))
       (sort-by :tx)))

