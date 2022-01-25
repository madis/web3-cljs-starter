(ns starter.ui.events
  (:require
    [re-frame.core :as re]
    [re-frame.db :refer [app-db]]
    [cljs.core.async :refer [go put!]]
    [cljs.core.async.interop :refer-macros [<p!]]
    ))

(re/reg-event-db :transfer-form/set-address
                 (fn [db [_ new-address]]
                   (println ":transfer-form/:set-address new-address" new-address)
                   (assoc-in db [:transfer-form :address] new-address)))

(re/reg-event-db :transfer-form/set-amount
                 (fn [db [_ new-amount]]
                   (println ":transfer-form/set-amount new-amount" new-amount)
                   (assoc-in db [:transfer-form :amount] new-amount)))

(re/reg-event-db :transfer-form/send
                 (fn [db [_ _]]
                   (println ":transfer-form/send new-amount" (get-in db [:transfer-form :amount]))
                   db))

; async-experiment START
(defn time-str [] (.toUTCString (js/Date.)))

(defn promise-result-indicator [db] (get-in db [:async-experiment :promise-result]))

(defn resolve-or-reject-based-on-db [db resolve-fn reject-fn]
  (fn []
    (case (promise-result-indicator @db)
      :resolve (do (resolve-fn (time-str))
                   (js/clearInterval (get-in @db [:async-experiment :interval-id]))
                   (reset! db (assoc-in @db [:async-experiment] {:promise-result nil :result (str "RESOLVED! " (time-str))})))
      :reject (do (reject-fn (time-str))
                  (js/clearInterval (get-in @db [:async-experiment :interval-id]))
                  (reset! db (assoc-in @db [:async-experiment] {:promise-result nil :result (str "REJECTED! " (time-str))})))
      nil (println "No result yet, repeating interval..."))))

(defn create-register-interval [resolve-fn reject-fn]
  (let [interval-id (js/setInterval (resolve-or-reject-based-on-db app-db resolve-fn reject-fn) 1000)]
    (reset! app-db (assoc-in @app-db [:async-experiment :interval-id] interval-id))))


(re/reg-event-db :async-experiment/create
                 (fn [db _]
                   (let [creation-time (time-str)
                         promise-body (fn [resolve-fn reject-fn]
                                        (println "Executing promise body")
                                        (create-register-interval resolve-fn reject-fn))
                         new-promise (new js/Promise promise-body)]
                   (println "Creating async experiment")
                   ; Ugly as hell and non-idiomatic to directly refer to the DB atom
                   (assoc-in @app-db [:async-experiment :promise] new-promise))))

(re/reg-event-db :async-experiment/resolve
                 (fn [db _]
                   (println "Resolving async experiment")
                   (assoc-in db [:async-experiment :promise-result] :resolve)))

(re/reg-event-db :async-experiment/reject
                 (fn [db _]
                   (println "Rejecting async experiment")
                   (assoc-in db [:async-experiment :promise-result] :reject)))

; async-experiment END
