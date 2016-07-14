(ns relog.server
  (:require [relog.handler :refer [app]]
            [config.core :refer [env]]
            [relog.db.tasks :as db-tasks]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

(defmacro final
  [& body]
  `(do ~@body (System/exit 0)))

 (defn -main [cmd]
   (cond
     (= cmd "db/migrate") (final (println (db-tasks/install-schema)))
     :else (let [port (Integer/parseInt (or (env :port) "3000"))]
             (run-jetty app {:port port :join? false}))))
