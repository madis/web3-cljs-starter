(ns starter.ui.core
  (:require
    [reagent.dom]
    [mount.core :as mount]
    [re-frame.core :as re]

    [starter.ui.views]
    [starter.ui.events]
    [starter.ui.subs]))

(enable-console-print!)

(defn get-config [] {})

(defn render []
  (reagent.dom/render [starter.ui.views/starter-app] (.getElementById js/document "app")))

(defn ^:dev/after-load clear-cache-and-render! []
  (re/clear-subscription-cache!)
  (render))

(defn start [] (render))
(defn stop [] (re/clear-subscription-cache!))

(mount/defstate starter-app
  :start (start)
  :stop (stop))

(defn ^:export init []
  (let [main-config (get-config)]
    (-> (mount/with-args main-config)
      (mount/start))
    ::started))
