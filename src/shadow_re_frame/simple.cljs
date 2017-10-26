(ns shadow-re-frame.simple
  "Example of `re-frame-simple`, an alternate `re-frame` syntax for simple use cases."
  (:require
    [re-view.re-frame-simple :as db :include-macros true]
    [re-frame.core :as rf]
    [reagent.core :as reagent]

    ;; just for tracing
    [day8.re-frame.trace.localstorage :as localstorage]
    [day8.re-frame.trace :as trace]

    ;; just for you
    [shadow-re-frame.welcome :as welcome]))

;;
;; For a complete introduction to `re-view.re-frame-simple`, see the readme:
;; https://github.com/braintripping/re-view/blob/master/re-frame-simple/README.md
;;


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; A COUNTER
;;
;; Example of...
;;
;; 1. Reading data using `db/get-in`
;;
;; 2. Writing data using `db/update-in!`
;;
;;


(defn counter
  "Given a counter id, render it as an interactive widget."
  [id]

  ;; NOTICE: `db/get-in`
  (let [total (db/get-in [::counters id])]

    ;; NOTICE: `db/update-in!`
    [:div.button {:on-click #(db/update-in! [::counters id] inc)}
     total
     [:br]
     (if (pos? total)
       (take total (repeat id))
       [:span {:style {:color "#888"}} "click me!"])]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; NAMED UPDATES
;;
;; `defupdate` associates a keyword with an update function.
;;  this can be dispatched like any other re-frame handler.
;;

(db/defupdate :initialize
  "Initialize the `db` with the preselected emoji as counter IDs."
  [db]
  {::counter-ids (shuffle ["👹" "👺" "💩" "👻💀️"
                           "👽" "👾" "🤖" "🎃"
                           "😺" "👏" "🙏" "👅"
                           "👂" "👃" "👣" "👁"
                           "👀" "👨‍" "🚒" "👩‍✈️"
                           "👞" "👓" "☂️" "🎈"
                           "📜" "🏳️‍🌈" "🚣" "🏇"])})

(db/defupdate :new-counter
  "Create a new counter, using an ID from the pre-selected emoji."
  [db]
  (-> db
      (assoc-in [::counters (peek (::counter-ids db))] 0)
      (update ::counter-ids pop)))


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
  [:div.root-layout

   (doall (for [id (counter-ids)]
            ^{:key id} [counter id]))

   [:div.button
    {:on-click #(db/dispatch [:new-counter])
     :style    {:background "pink"}}
    "Add Counter"]


   [:div.font-normal
    {:style {:margin "2rem 0 1rem"}}
    [:div.font-large "👆👆👆👆👆👆👆"]

    "be sure to try the counter app!"]

   welcome/welcome-text

   [:div "👉 \u00a0 view the " [:a {:href "https://github.com/mhuebert/shadow-re-frame/blob/master/src/shadow_re_frame/simple.cljs"} "source code"] " for this page"]])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Boilerplate code to get the page to render:

(defn ^:export render []
  (reagent/render [root-view]
                  (js/document.getElementById "shadow-re-frame")))

(defn ^:export init []

  ;; enable re-frame-trace, show panel by default
  (localstorage/save! "show-panel" true)
  (trace/init-tracing!)
  (trace/inject-devtools!)

  ;; initialize the db, create an example counter
  (db/dispatch [:initialize])
  (db/dispatch [:new-counter])

  ;; render to page
  (render))

