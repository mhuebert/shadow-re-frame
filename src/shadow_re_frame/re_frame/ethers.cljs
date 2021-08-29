(ns shadow-re-frame.re-frame.ethers
  (:require [shadow-re-frame.interop.ethers :as ethers]
            [re-promise.core]
            [re-frame.core :as rf]))

;; 2. Event Handling
;;    register a handler for a given event.
;;
;;    - handlers are identified by keyword.
;;    - simple method:   `reg-event-db` is passed `db` as 1st argument.
;;      advanced method: `reg-event-fx` is passed 'co-effects' map as 1st argument of which `:db` is one key.

(rf/reg-event-fx
 :successful-account-fetch
  (fn [{:keys [db]} [_ account-address]]
    {:db (assoc db ::account account-address)
     :fx [[:dispatch [:shadow-re-frame.re-frame.weth/fetch-balance
                      account-address]]]}))

(rf/reg-event-db :failed-account-fetch
  (fn [db [_ account-address]]
    (assoc db ::account account-address)))

(rf/reg-event-fx :initialize
  (fn [_]
    {:promise-n [{:call ethers/fetch-current-address
                  :on-success [:successful-account-fetch]
                  :on-failure [:failed-account-fetch]}]
     :fx [
          [:dispatch [:shadow-re-frame.re-frame.weth/fetch-name]]]

     :db {}}))


;; 3. Queries
;;    make a query for every kind of 'read' into the db.
;;
;;    - queries are identified by keyword.
;;    - queries can (optionally) take parameters.
;;    - `db` is passed as 1st arg to function.
;;      vector of [query-id & args] is passed as 2nd arg.

(rf/reg-sub ::account
            (fn [db [_ _]]
              (get-in db [::account])))


;(rf/reg-event-fx
; qi-dao-masterchef (fetch-contract "0x574Fe4E8120C4Da1741b5Fd45584de7A5b521F0F"))

(rf/reg-sub ::counter-ids
            (fn [db _]
              (-> (get db ::counters)
                  (keys))))
