(ns starter.ui.events
  (:require
    [re-frame.core :as re]
    [cljs.core.async :refer [go]]
    [cljs.core.async.interop :refer-macros [<p!]]))

(re/reg-event-db :transfer-form/set-address
                 (fn [db [_ new-address]]
                   (println "set-address" new-address)
                   (assoc-in db [:transfer-form :address] new-address)))

(re/reg-event-db :transfer-form/set-amount
                 (fn [db [_ new-amount]]
                   (assoc-in db [:transfer-form :amount] new-amount)))

; async-experiment START
(re/reg-event-db :async-experiment/create
                 (fn [db _]
                   (let [stored-resolve (atom (fn [x] (println "I resolve nothing")))
                         stored-reject (atom (fn [x] (println "I reject nothing")))
                         body (fn [resolve-fn reject-fn]
                                (reset! stored-resolve resolve-fn)
                                (reset! stored-reject reject-fn))
                         new-promise (new js/Promise body)]
                   (println "Creating async experiment")
                   (assoc-in db [:async-experiment] {:promise new-promise
                                                     :resolve-fn stored-resolve
                                                     :reject-fn stored-reject}))))

(re/reg-event-db :async-experiment/resolve
                 (fn [db _]
                   (println "Resolving async experiment")
                   (@(get-in db [:async-experiment :resolve-fn]) "Resolved!")
                   db))

(re/reg-event-db :async-experiment/reject
                 (fn [db _]
                   (println "Rejecting async experiment")
                   (@(get-in db [:async-experiment :reject-fn]) "Rejected")
                   db))

; async-experiment END
