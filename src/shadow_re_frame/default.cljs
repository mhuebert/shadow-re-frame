(ns shadow-re-frame.default
  (:require
   [re-frame.core :as rf]
   [tailwind-hiccup.core :refer [tw]]
   [goog.dom :as gdom]
   [reagent.dom :as rdom]

   [reitit.frontend :as reit.f]
   [reitit.frontend.easy :as reit.fe]
   [reitit.coercion.spec :as reit.spec]
   [reagent.core :as r]
   [spec-tools.data-spec :as ds]))

;; 1. Event Dispatch
;;    make a view, dispatch an event in a click handler

(defn counter
  "Given a counter id and its current value, render it as an interactive widget."
  [id]
  (let [counter-value @(rf/subscribe [::counter id])]
    [:div (tw [:p-6 :max-w-sm :mx-auto :bg-white :rounded-xl
               :shadow-md :flex :items-center :space-x-4]
              {:on-click #(rf/dispatch [:inc-counter id])
               :style {:padding 20
                       :margin "10px 0"
                       :background "rgba(0,0,0,0.05)"
                       :cursor "pointer"}})
     (str "Counter " (name id) ": ")
     counter-value]))


;; 2. Event Handling
;;    register a handler for a given event.
;;
;;    - handlers are identified by keyword.
;;    - simple method:   `reg-event-db` is passed `db` as 1st argument.
;;      advanced method: `reg-event-fx` is passed 'co-effects' map as 1st argument of which `:db` is one key.

(rf/reg-event-db :inc-counter
                 (fn [db [_ counter-id]]
                   (update-in db [::counters counter-id] inc)))


(rf/reg-event-db :initialize
                 ;; we'll call this once, at the beginning, to set up the db.
                 (constantly {::counters {"A" 0
                                          "B" 0
                                          "C" 0}}))

;; 3. Queries
;;    make a query for every kind of 'read' into the db.
;;
;;    - queries are identified by keyword.
;;    - queries can (optionally) take parameters.
;;    - `db` is passed as 1st arg to function.
;;      vector of [query-id & args] is passed as 2nd arg.

(rf/reg-sub ::counter
            (fn [db [_ counter-id]]
              (get-in db [::counters counter-id])))

(rf/reg-sub ::counter-ids
            (fn [db _]
              (-> (get db ::counters)
                  (keys))))

(defn home-page []
  [:div
   [:h2 "Welcome to frontend"]

   [:button
    {:type "button"
     :on-click #(reit.fe/push-state ::item {:id 3})}
    "Item 3"]

   [:button
    {:type "button"
     :on-click #(reit.fe/replace-state ::item {:id 4})}
    "Replace State Item 4"]])

(defn about-page []
  [:div
   [:h2 "About frontend"]
   [:ul
    [:li [:a {:href "http://google.com"} "external link"]]
    [:li [:a {:href (reit.fe/href ::foobar)} "Missing route"]]
    [:li [:a {:href (reit.fe/href ::item)} "Missing route params"]]]

   [:div
    {:content-editable true
     :suppressContentEditableWarning true}
    [:p "Link inside contentEditable element is ignored."]
    [:a {:href (reit.fe/href ::frontpage)} "Link"]]])

(defn item-page [match]
  (let [{:keys [path query]} (:parameters match)
        {:keys [id]} path]
    [:div
     [:h2 "Selected item " id]
     (if (:foo query)
       [:p "Optional foo query param: " (:foo query)])]))

(defonce match (r/atom nil))

(defn current-page []
  [:div
   [:ul
    [:li [:a {:href (reit.fe/href ::frontpage)} "Frontpage"]]
    [:li [:a {:href (reit.fe/href ::about)} "About"]]
    [:li [:a {:href (reit.fe/href ::item {:id 1})} "Item 1"]]
    [:li [:a {:href (reit.fe/href ::item {:id 2} {:foo "bar"})} "Item 2"]]]
   (if @match
     (let [view (:view (:data @match))]
       [view @match]))
   [:pre @match]])

(def routes
  [["/"
    {:name ::frontpage
     :view home-page}]

   ["/about"
    {:name ::about
     :view about-page}]

   ["/item/:id"
    {:name ::item
     :view item-page
     :parameters {:path {:id int?}
                  :query {(ds/opt :foo) keyword?}}}]])

;; 4. Views
;;    Use reagent to create views.
;;
;;    - use `re-frame.core/subscribe` to read queries
;;    - use `reagent/atom` for local state (not shown here)

(defn root-view
  "Render the page"
  []
  [:div (tw [:max-w-md :flex :flex-col :items-center :m-auto])
   ;{:style {
   ;         :margin    "50px auto"
   ;         :font-size 16}}
   (let [counters (rf/subscribe [::counter-ids])]
     (doall (for [id @counters]
              ^{:key id} [counter id])))])

(defn ^:export ^:dev/after-load render []
  (reit.fe/start! (reit.f/router routes {:data {:coercion reit.spec/coercion}})
                  (fn [m] (reset! match m))
                  {:use-fragment false})
  (rdom/render [current-page]
               (gdom/getRequiredElement "shadow-re-frame")))

(defn ^:export init []
  (rf/dispatch-sync [:initialize])
  (render))

