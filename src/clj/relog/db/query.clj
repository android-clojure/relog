(ns relog.db.query
  (:require [datomic.api :as d]))

(def uri "datomic:dev://localhost:4334/relog")

(defn getPosts []
  (def conn (d/connect uri))
  (def db (d/db conn))
  (->> (d/q '[:find ?post ?body ?firstName ?lastName ?tx
         :where [?post :post/body ?body ?tx]
                [?post :post/author ?author]
                [?author :author/firstName ?firstName]
                [?author :author/lastName ?lastName]]
       db)
       (map #(zipmap [:id :body :firstName :lastName :tx] %))
       (sort-by :tx)))
