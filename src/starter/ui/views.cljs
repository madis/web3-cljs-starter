(ns starter.ui.views
  (:require
    [re-frame.core :as re]))

(defn dispatch-value [re-frame-event-name js-event-data]
  (let [value-from-event (-> js-event-data .-target .-value)]
    (re/dispatch [re-frame-event-name value-from-event])))

(defn c-button [text event-name & {:keys [type] :or {type :primary}}]
  [:button {:class [:is-block :button (str "is-" (name type)) :mt-4]
            :on-click (fn [e]
              (.preventDefault e)
              (re/dispatch [event-name]))} text])

(defn async-experiment []
  (let [promise-result (re/subscribe [:async-experiment-result])]
    [:div
     [:h1.title.is-3 "Async experiment"]
     [:div.box.columns
      [:div.column.is-one-fifth
       [c-button "Create promise" :async-experiment/create :type :light]
       [c-button "Resolve" :async-experiment/resolve :type :success]
       [c-button "Reject" :async-experiment/reject :type :danger]]
      [:div.column
       [:article
        [:div.message-header "Promise result"]
        [:div.message-body (or @promise-result "<nothing>")]]]]]))

(defn transfer-form []
  (let [wei-value (re/subscribe [:value-in-wei])]
    [:form {:class :box}
     [:label {:class :label} "Address"]
     [:input {:type "text" :class :input :on-change #(dispatch-value :transfer-form/set-address %)}]
     [:label {:class :label} "Amount (ETH)"]
     [:input {:type "text" :class :input :style {:width "15%"} :on-change #(dispatch-value :transfer-form/set-amount %)}]
     [:label {:class :label} "In wei"]
     [:input {:type "text" :class [:input] :style {:width "40%"} :value @wei-value :disabled true}]
     [c-button "Send!" :transfer-form/send]]))

(defn starter-app []
  [:<>
   [:h1 {:class :title} "Web3 starter"]
   [transfer-form]
   [async-experiment]])
