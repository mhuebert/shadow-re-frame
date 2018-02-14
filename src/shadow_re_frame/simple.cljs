(ns shadow-re-frame.simple
  "Example of `re-frame-simple`, an alternate `re-frame` syntax for simple use cases."
  (:require
   [re-view.re-frame-simple :as db :include-macros true]
   [reagent.core :as reagent]
   [shadow-re-frame.welcome :as text]

   ;; this is re-frame-trace's separate instance of re-frame
   [mranderson047.re-frame.v0v10v2.re-frame.db :as trace-db]
   [mranderson047.re-frame.v0v10v2.re-frame.core :as trace-rf]))

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
    [:div.button {:on-mouse-down #(do
                                    (.preventDefault %)
                                    (db/update-in! [::counters id] inc))}
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

(def divider [:div.font-large
              {:style {:margin "2rem 0 1rem"}}
              "〰️〰️〰️〰️〰️〰️〰️"])

(defn root-view
  "Render the page"
  []
  [:div.root-layout


   (doall (for [id (counter-ids)]
            ^{:key id} [counter id]))

   [:div.button
    {:on-click #(db/dispatch [:new-counter])
     :style {:background "pink"}}
    "Add Counter"]



   (let [sample-input (db/get ::sample-input)]
     [:div.text-example
      {:style {:margin "2.5rem 0 0"}}
      [:input {:value sample-input
               :placeholder "Your name"
               :on-change #(db/assoc! ::sample-input (.. % -target -value))}]
      [:div "Hello, " (or sample-input "____")]])

   divider

   text/welcome

   [:p "👉 \u00a0 view the " [:a {:href "https://github.com/mhuebert/shadow-re-frame/blob/master/src/shadow_re_frame/simple.cljs"} "source code"] " for this page."]

   [:p "👨🏻‍💻   by Matt Huebert (" [:a {:href "https://matt.is/"} "website"] ", " [:a {:href "https://www.twitter.com/mhuebert"} "twitter"] ")"]])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Boilerplate code to get the page to render:

(defn ^:export render []
  (reagent/render [root-view]
                  (js/document.getElementById "shadow-re-frame")))

(defn ^:export init []

  ;; initialize the db, create an example counter
  (db/dispatch [:initialize])
  (db/dispatch [:new-counter])
  ;; render to page
  (render)


  ;; open re-frame-trace panel (after a timeout, to make sure it's state has loaded)
  (-> #(when-not (get-in @trace-db/app-db [:settings :show-panel?])
         (trace-rf/dispatch [:settings/user-toggle-panel]))
      (js/setTimeout 100)))