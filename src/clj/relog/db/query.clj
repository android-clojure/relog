(ns relog.db.query
  (:require [datomic.api :as d]
            [clojure.pprint :refer [pprint]]
            [config.core :refer [env]]))

(defn getPosts []
  (def conn (d/connect (:db-uri env)))
  (def db (d/db conn))
  (->> (d/q '[:find ?post ?id ?name ?body ?firstName ?lastName ?date
         :where [?post :post/public-id ?id ?tx]
                [?post :post/body ?body]
                [?post :post/name ?name]
                [?post :post/author ?author]
                [?author :author/firstName ?firstName]
                [?author :author/lastName ?lastName]
                [?tx :db/txInstant ?date]]
       db)
       (map #(zipmap [:eid :id :name :body :firstName :lastName :date] %))
       (sort-by :date)))

(defn getPost [id]
  (def conn (d/connect (:db-uri env)))
  (def db (d/db conn))
  (->> (d/q '[:find ?post ?id ?name ?body ?firstName ?lastName ?date
         :in $ ?id
         :where [?post :post/public-id ?id ?tx]
                [?post :post/body ?body]
                [?post :post/name ?name]
                [?post :post/author ?author]
                [?author :author/firstName ?firstName]
                [?author :author/lastName ?lastName]
                [?tx :db/txInstant ?date]]
       db id)
       (map #(zipmap [:eid :id :name :body :firstName :lastName :date] %))
       (sort-by :date)
       first))

(defn getPostNames []
  (def conn (d/connect (:db-uri env)))
  (def db (d/db conn))
  (->> (d/q '[:find ?post ?id ?name ?date
         :where [?post :post/public-id ?id ?tx]
                [?post :post/name ?name]
                [?tx :db/txInstant ?date]]
       db)
       (map #(zipmap [:eid :id :name :date] %))
       (sort-by :date)))

