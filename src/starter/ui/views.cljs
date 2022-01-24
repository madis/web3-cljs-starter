(ns starter.ui.views
  (:require
    [re-frame.core :as re]
    [cljs.core.async :as async :refer [chan take!]]
    [cljs.core.async.interop :refer [p->c]]))

(defn dispatch-value [re-frame-event-name js-event-data]
  (let [value-from-event (-> js-event-data .-target .-value)]
    (re/dispatch [re-frame-event-name value-from-event])))

(defn c-button [text action & {:keys [type] :or {type :primary}}]
  (let [emit-event #(re/dispatch [action])
        on-click-fn (if (fn? action) action emit-event)]
    [:button {:class [:is-block :button (str "is-" (name type)) :mt-4]
              :on-click (fn [e]
                          (.preventDefault e)
                          (on-click-fn))} text]))


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
        [:div.message-body
         [:div (str "promise-result: " @promise-result)]
         ]]]]]))

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

(def sections [transfer-form async-experiment])

(defn starter-app []
  [:<>
   [:h1 {:class :title} "Web3 starter"]
   (into [:div] (map vector sections))])
