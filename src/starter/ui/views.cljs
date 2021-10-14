(ns starter.ui.views
  (:require
    [re-frame.core :as re]))

(defn dispatch-value [re-frame-event-name js-event-data]
  (let [value-from-event (-> js-event-data .-target .-value)]
    (re/dispatch [re-frame-event-name value-from-event])))

(defn transfer-form []
  (let [wei-value (re/subscribe [:value-in-wei])]
    [:form {:class :box}
     [:label {:class :label} "Address"]
     [:input {:type "text" :class :input :on-change #(dispatch-value :transfer-form/set-address %)}]
     [:label {:class :label} "Amount (ETH)"]
     [:input {:type "text" :class :input :style {:width "15%"} :on-change #(dispatch-value :transfer-form/set-amount %)}]
     [:label {:class :label} "In wei"]
     [:input {:type "text" :class [:input] :style {:width "40%"} :value @wei-value :disabled true}]
     [:button {:class [:is-block :button :is-primary :mt-4]} "Send!"]]))

(defn starter-app []
  [:<>
   [:h1 {:class :title} "Web3 starter"]
   [transfer-form]])
