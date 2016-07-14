(ns relog.db.tasks
  (:require [config.core :refer [env]]
            [io.rkn.conformity :as c]
            [datomic.api :as d]))

(def uri "datomic:dev://localhost:4334/relog")
(d/create-database uri)

(defn install-schema []
  (println (c/ensure-conforms
            (d/connect uri)
            (c/read-resource "initialSchema.edn")
            [:relog/relog-schema1])))

