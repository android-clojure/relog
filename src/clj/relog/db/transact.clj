(ns relog.db.transact
  (:require [datomic.api :as d]
            [clojure.pprint :refer [pprint]]
            [config.core :refer [env]]))

(defn savePost [to-be-saved]
  (def conn (d/connect (:db-uri env)))
  (def public-id (get-in to-be-saved [:id]))
    (-> (d/transact
        conn
        [[:db/add [:post/public-id public-id]
          :post/body (:body to-be-saved)]])
        deref))

(defn createPost [post-id to-be-created]
  (def conn (d/connect (:db-uri env)))
  (let [post-body (:body to-be-created)
        post-name (:name to-be-created)]
    (-> (d/transact
          conn
          [{:db/id (d/tempid :db.part/user)
            :post/public-id post-id
            :post/body post-body
            :post/name post-name
            :post/author [:author/email "davintryon@gmail.com"]}])
        deref)))

