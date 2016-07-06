;  test out creating datomic schemas

(require '[datomic.api :only [q db] :as d])

(def uri "datomic:dev://localhost:4334/relog")

(d/create-database uri)

(def conn (d/connect uri))

(def schema-author-tx (read-string (slurp "data/schema/author.edn")))

(first schema-author-tx)

@(d/transact conn schema-author-tx)

(def schema-post-tx (read-string (slurp "data/schema/post.edn")))

(first schema-post-tx)

@(d/transact conn schema-post-tx)

; test data
; doesn't work
(def seed-tx (read-string (slurp "data/schema/seed.edn")))

@(d/transact conn seed-tx)

(def results (q '[:find ?c :where [?c :author/firstName]] conn))

