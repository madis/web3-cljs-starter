(ns starter.ui.events
  (:require
    [re-frame.core :as re]))

(re/reg-event-db :transfer-form/set-address
                 (fn [db [_ new-address]]
                   (println "set-address" new-address)
                   (assoc-in db [:transfer-form :address] new-address)))

(re/reg-event-db :transfer-form/set-amount
                 (fn [db [_ new-amount]]
                   (assoc-in db [:transfer-form :amount] new-amount)))
