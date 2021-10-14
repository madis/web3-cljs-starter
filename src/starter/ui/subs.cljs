(ns starter.ui.subs
  (:require
    [re-frame.core :as re]
    ["web3" :as web3]))

(def wei-in-eth (Math/pow 10 18))
(defn eth->wei [eth] (* eth wei-in-eth))

(re/reg-sub
  :value-in-wei
  (fn [db _query-v] (eth->wei (get-in db [:transfer-form :amount]))))

; async-experiment START
(re/reg-sub
  :async-experiment-result
  (fn [db _]
    (println "Producing async-experiment-result" (get-in db [:async-experiment :result]))
    (get-in db [:async-experiment :result])))

; async-experiment END
