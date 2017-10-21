(ns shadow-re-frame.core
  (:require
    [re-view.core :as v :refer [defview]]
    [shadow-re-frame.examples :as examples]
    [re-frame.core :as re]
    [reagent.core :as reagent]
    ))

(enable-console-print!)

(comment

  (defview text
    "A simple view element with text, using the :name prop."
    [this]
    [:div {:style {:font-size  30
                   :text-align "center"}}
     "Welcome to " (:name this) "!"])

  (defview counter
    "Example of using the :view/state atom to keep local state."
    [this]
    [:div
     {:on-click #(swap! (:view/state this) inc)
      :style    {:padding    10
                 :background "#eee"
                 :cursor     "pointer"}}
     "I have been clicked " (or @(:view/state this) 0) " times."])


  (def spacer [:div {:style {:height 10}}])

  (defview layout [this]
    [:div
     {:style {:max-width 300
              :margin    "50px auto"
              :font-size 16}}
     (text {:name "shadow-re-frame"})
     spacer
     (counter)
     spacer
     (examples/custom-cursor)
     spacer
     (examples/todo-list)])



  (defn ^:export render []
    (v/render-to-dom (layout) "shadow-re-frame")))

;; initialize
(re/reg-event-db
  :initialize
  (constantly {:counters {"A" 0
                          "B" 0
                          "C" 0}}))

;; The six dominoes are:
;;
;; Event dispatch - something happens - the user clicks a button, or a websocket receives a new message.
;; Event handling - Event handler functions compute a description of effects
;; Effect handling - descriptions of effects are realised (actioned)
;; Query -  extracting data from "app state", and providing it in the right format for view functions. de-duplicated signal graph.
;; View - one or more view functions (aka Reagent components) compute hiccup-formatted data
;; DOM - handled for you by Reagent/React


;; View + event Dispatch
(defn count-button
  [[counter-id counter-value]]
  [:div {:key      counter-id
         :on-click #(re/dispatch [:inc-counter counter-id])
         :style    {:padding    20
                    :margin     10
                    :background "rgba(0,0,0,0.05)"}}
   counter-value]
  )

;; Event handling
(re/reg-event-fx :inc-counter
                 (fn [{:keys [db] :as coeffects} [_ counter-id]]
                   {:db (update-in db [:counters counter-id] (fnil inc 0))}))

(comment
  ;; re/reg-event-db passes `db` instead of the 'coeffects' map
  (re/reg-event-db :inc-counter
                   (fn [db [_ counter-id]]))
  )

;; Query - reg-sub associates a query id with a function of db

(re/reg-sub :read-counters
            (fn query-fn
              [db [_ _]]
              (get db :counters)))

;; View

(defn root-view
  []
  [:div
   (let [counters (re/subscribe [:read-counters])]
     (map count-button @counters))])

(defn ^:export render []
  (re/dispatch-sync [:initialize])
  (reagent/render [root-view]
                  (js/document.getElementById "shadow-re-frame")))

