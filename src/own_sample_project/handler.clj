(ns own-sample-project.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [clojure.string :as str]
            [own-sample-project.database :as database]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as response]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.nested-params :refer [wrap-nested-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.session :refer [wrap-session]]
           ))

(defn home []
  (str "<h1>HOME PAGE FOR LOGIN</h1>"
       "<a href='/login'>LOGIN</a>"
   )
  )

(defn login []
  (str "<p><b>LOGIN FORM</b></p>"
       "<form action='/login'  method='POST'>"
       (anti-forgery-field) ;; Include the anti-forgery field in the form
       "EMAIL: <input type='email' name='email'><br/>"
       "PASSWORD: <input type='password' name='password'><br/>"
       "<input type='submit' value='Submit' ></form>"
       "<p> Don't have an account? <a href='/register'>REGISTER</a></p>"))

(defn register []
  (str "<p><b>REGISTER FORM</b></p>"
       "<form action='/register' method='POST' >"
       (anti-forgery-field) ;; Also include the anti-forgery field in the register form
       "FIRST NAME: <input type='text' name='first_name'><br/>"
       "LAST NAME: <input type='text' name='last_name'><br/>"
       "EMAIL: <input type='email' name='email'><br/>"
       "PASSWORD: <input type='password' name='password'><br/>"
       "<input type='submit' value='Submit'></form>"
       "<p>Already have an account? <a href='/login'>LOGIN</a></p>"))

(defn database-opr []
  
  )
(database-opr)


(defn login-post [req]
  (let [form-params (-> req :params)
        email       (form-params "email")
        password    (form-params "password")
        user-record (database/get-user-by-email email)]
    (println user-record) ;; print the user-record map
    (if (and user-record (= (str password) (:password user-record)))
      (response/redirect "/dashboard") ; Redirect to a dashboard page upon successful login
      "Invalid email or password. <a href='/login'>Try again</a>")))




(defroutes app-routes
  (GET "/" [] (home))
  (GET "/dashboard" [] "successfully login...")
  (GET "/login" [] (login))
  (GET "/register" [] (register))
   
  (POST "/register" req
    (let [form-params (-> req :params)
          Fname       (form-params "first_name")
          Lname       (form-params "last_name")
          Email       (form-params "email")
          Password    (form-params "password")]
      (database/insert-data Fname Lname Email Password)
      (response/redirect "/")))
  (POST "/login" req (login-post req))
  )

  
 

(def app
  (-> app-routes
      wrap-params
      wrap-nested-params
      wrap-keyword-params
      wrap-session ;; Moved before wrap-anti-forgery
      wrap-anti-forgery
      (wrap-defaults site-defaults)))


;; (def app
;;   (wrap-defaults app-routes site-defaults))
