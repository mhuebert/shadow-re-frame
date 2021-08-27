(ns shadow-re-frame.default
  (:require
   [applied-science.js-interop :as j]
   [promesa.core :as p]

   [reagent.core :as r]
   [re-frame.core :as rf]
   [tailwind-hiccup.core :refer [tw]]

   [goog.dom :as gdom]
   [reagent.dom :as rdom]

   [reitit.frontend :as reit.f]
   [reitit.frontend.easy :as reit.fe]
   [reitit.coercion.spec :as reit.spec]
   [spec-tools.data-spec :as ds]

   ["ethers" :as ethers]
   [shadow-re-frame.interop.ethers :as inter.ethers]
   [shadow-re-frame.re-frame.ethers :as rf.ethers]
   [shadow-re-frame.re-frame.weth :as rf.weth]

   [shadow-re-frame.components.buttons :as cmps.btn]
   [shadow-re-frame.components.forms :as cmps.form]

   [fork.re-frame :as fork]
   [fork.re-frame :as fork-re-frame]))

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


;(rf/reg-event-db :initialize
;                 ;; we'll call this once, at the beginning, to set up the db.
;                 (constantly {::counters {"A" 0
;                                          "B" 0
;                                          "C" 0}}))

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

(rf/reg-event-fx
 :submit-handler
  (fn [{db :db} [_ {:keys [values dirty path]}]]
    ;; dirty tells you whether the values have been touched before submitting.
    ;; Its possible values are nil or a map of changed values
    (js/console.dir values)
    (let [approval-amount (values "token-approval-val")]
     {:db (fork/set-submitting db path true)
      :dispatch [::rf.weth/approve-weth-balance
                 "0xf5C678Be432F07261e728a58bFFEAC52bA731BF5"
                 approval-amount]})))


(rf/reg-event-fx
 :resolved-form
  (fn [{db :db} [_ path values]]
    (js/alert values)
    {:db (fork/set-submitting db path false)}))

(defn my-form
  [{:keys [values handle-change handle-blur form-id handle-submit]}]
  [:div
   [:p "Read back: " (values "token-approval-val")]
   [:form {:id form-id
           :on-submit handle-submit}
    [cmps.form/input-with-label
     {:label-val "Value To Approve"}
     {:value (values "token-approval-val")
      :id "token-approval-val"
      :name "token-approval-val"
      :type "text"
      :required true
      :on-change handle-change
      :on-blur handle-blur}]
    [:button (tw [:h-12 :px-6 :m-2 :text-lg :text-indigo-100 :transition-colors :duration-150
                  :bg-indigo-700 :rounded-lg :focus:shadow-outline :hover:bg-indigo-800]
                 {:type "submit"})
     "Approve"]]])

(defn home-page []
  [:div
   [:h2 "Welcome to frontend"]
   (let [address @(rf/subscribe [::rf.ethers/account])
         block-number @(rf/subscribe [::inter.ethers/current-block])
         weth-balance @(rf/subscribe [:shadow-re-frame.re-frame.weth/weth-balance])
         weth-allowance @(rf/subscribe [::rf.weth/weth-zapper-allowance address
                                        "0xf5C678Be432F07261e728a58bFFEAC52bA731BF5"])]

     [:div
      [:div address]
      [:div weth-balance]
      [:div [:p "Zapper Contract Allowance"] weth-allowance]
      [fork/form {:initial-values {"token-approval-val" 0.0}
                  :path [:form]
                  :form-id "form-id"
                  :prevent-default? true
                  :on-submit #(rf/dispatch [:submit-handler %])
                  :clean-on-unmount? true}
       my-form]
      [:div block-number]])])

(comment
 (js/console.log)
 (new js/Number 1)



 (p/let [bal (.getBalance provider "0x44435Bf6AB881133a25bDAaba015Aad0b8A1CDd9")
         mai-abi-res (js/fetch "assets/contracts/0xa3fa99a148fa48d14ed51d610c367c61876997f1.json")
         mai-abi-json (.json mai-abi-res)
         mai-contract (ethers/Contract. "0xa3fa99a148fa48d14ed51d610c367c61876997f1"
                                        mai-abi-json
                                        provider)
         mai-bal (j/call mai-contract
                         :balanceOf
                         "0x44435Bf6AB881133a25bDAaba015Aad0b8A1CDd9")]

  (js/console.log (ethers/utils.formatEther  bal))

  (js/console.log (ethers/utils.formatEther mai-bal))


  (js/console.log mai-abi-json)))


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
     [:img {:src "/assets/QiDaoDay.svg"}]
     [:h2 "Selected item " id]
     (if (:foo query)
       [:p "Optional foo query param: " (:foo query)])]))

(defonce match (r/atom nil))

(defn current-page []

  [:div
   (let [link-classes [:px-3 :py-2 :flex :items-center :text-xs :uppercase :font-bold :leading-snug :text-white :hover:opacity-75]]
    [:nav (tw [:relative :flex :flex-wrap :items-center :justify-between :px-2 :py-3 :bg-green-500 :mb-3])
     [:div (tw [:container :px-4 :mx-auto :flex :flex-wrap :items-center :justify-between])
      [:div (tw [:w-full :relative :flex :justify-between :lg:w-auto :px-4 :lg:static :lg:block :lg:justify-start])
       [:a (tw [:text-sm :font-bold :leading-relaxed :inline-block :mr-4 :py-2 :whitespace-nowrap :uppercase :text-white] {:href "#pablo"}) "teal Color"]
       [cmps.btn/hamburger]]
      [:div#example-navbar-warning (tw [:lg:flex :flex-grow :items-center])
       [:ul (tw [:flex :flex-col :lg:flex-row :list-none :ml-auto])
        [:li (tw [:nav-item])
         [:a (tw link-classes {:href (reit.fe/href ::frontpage)})
          "Front Page"]]
        [:li (tw [:nav-item])
         [:a (tw link-classes {:href (reit.fe/href ::about)})
          "About"]]
        [:li (tw [:nav-item])
         [:a (tw link-classes {:href (reit.fe/href ::item {:id 1})})
          "Item 1"]]
        [:li (tw [:nav-item])
          [:a (tw link-classes {:href (reit.fe/href ::item {:id 2} {:foo "bar"})})
           "Item 2"]]]]]])
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

