(ns shadow-re-frame.simple
  "Example of `re-frame-simple`, an alternate `re-frame` syntax for simple use cases."
  (:require
    [re-view.re-frame-simple :as db :include-macros true]
    [re-frame.core :as rf]
    [reagent.core :as reagent]

    ;; just for tracing
    [day8.re-frame.trace.localstorage :as localstorage]
    [day8.re-frame.trace :as trace]))

;;
;; For a complete introduction to `re-view.re-frame-simple`, see the readme:
;; https://github.com/braintripping/re-view/blob/master/re-frame-simple/README.md
;;


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; COUNTER EXAMPLE
;;
;; Example of...
;;
;; 1. Reading data using `db/get-in`
;;
;; 2. Writing data using `db/update-in!`
;;
;;


(defn counter
  "Given a counter id and its current value, render it as an interactive widget."
  [id]
  ;; NOTICE: `db/update-in!`
  [:div {:on-click #(db/update-in! [::counters id] inc)
         :style    {:padding    20
                    :margin     "10px 0"
                    :background "rgba(0,0,0,0.05)"
                    :cursor     "pointer"}}
   (str (name id) ": ")

   ;; NOTICE: `db/get-in`
   (db/get-in [::counters id])])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; NAMED UPDATES
;;
;; `defupdate` associates a keyword with an update function.
;;  this can be dispatched like any other re-frame handler.
;;

(db/defupdate :initialize [db]
              {::counters {"A" 0
                           "B" 1
                           "C" 2}})

(defonce _ (db/dispatch [:initialize]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Named queries
;;
;; use `defquery` to create named queries that read data using
;; `db/get` and `db/get-in`.
;;
;; `defquery` def's an ordinary Clojure function:
;;

(db/defquery counter-ids
             "Return the list of counters in the db, by id."
             []
             (-> (db/get ::counters)
                 (keys)))

;;
;; a component that uses the query will update when its data changes.
;;

(defn root-view
  "Render the page"
  []
  [:div
   {:style {:max-width  300
            :margin     "50px auto"
            :font-size  16
            :text-align "center"}}
   "Click to count!"

   (doall (for [id (counter-ids)]
            ^{:key id} [counter id]))

   "(Press Control-H to toggle re-frame-trace panel)"])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Boilerplate code to get the page to render:

(defn ^:export render []
  (reagent/render [root-view]
                  (js/document.getElementById "shadow-re-frame")))

(defn ^:export init []
  (localstorage/save! "show-panel" true)
  (trace/init-tracing!)
  (trace/inject-devtools!)

  (render))

