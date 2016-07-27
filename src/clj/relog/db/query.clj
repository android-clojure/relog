(ns relog.db.query
  (:require [datomic.api :as d]
            [clojure.pprint :refer [pprint]]
            [config.core :refer [env]]))

(defn getPosts []
  (def conn (d/connect (:db-uri env)))
  (def db (d/db conn))
  (->> (d/q '[:find ?post ?body ?firstName ?lastName ?date
         :where [?post :post/body ?body ?tx]
                [?post :post/author ?author]
                [?author :author/firstName ?firstName]
                [?author :author/lastName ?lastName]
                [?tx :db/txInstant ?date]]
       db)
       (map #(zipmap [:id :body :firstName :lastName :date] %))
       (sort-by :tx)))

(defn getPostNames []
  (def conn (d/connect (:db-uri env)))
  (def db (d/db conn))
  (->> (d/q '[:find ?post ?name ?date
         :where [?post :post/name ?name ?tx]
                [?tx :db/txInstant ?date]]
       db)
       (map #(zipmap [:id :name :date] %))
       (sort-by :tx)))
