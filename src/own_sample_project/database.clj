(ns own-sample-project.database
  (:require [clojure.java.jdbc :as jdbc]
            [own-sample-project.db :as db]
            )
  )

(defn create-table []
  (jdbc/with-db-connection [db-conn db/db-spec]
      (jdbc/execute! db-conn ["CREATE TABLE IF NOT EXISTS employee (ID INT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR(255)), SALARY INT,ADDRESS VARCHAR(255)"])
    )
  )

()

(defn insert-data [Fname Lname email password]
  (jdbc/with-db-connection [db-conn db/db-spec]
    (jdbc/insert! db-conn :register {:Fname Fname :Lname Lname :Email email :Password password})))

(defn get-user-by-email [email]
  (jdbc/with-db-connection [conn db/db-spec]
    (let [query "SELECT * FROM register WHERE Email = ?"]
      (first (jdbc/query conn [query email])))))
