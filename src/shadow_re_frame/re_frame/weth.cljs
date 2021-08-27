(ns shadow-re-frame.re-frame.weth
  (:require [promesa.core :as p]
            [shadow-re-frame.interop.contracts :as inter.con]
            [shadow-re-frame.interop.ethers :as inter.ethers]
            [re-frame.core :as rf]))

(defonce weth-contract
 (p/let [contract (shadow-re-frame.interop.ethers/fetch-contract
                   (:weth inter.con/contract->address))]
   (inter.con/->ERC20 contract)))

(rf/reg-event-db ::save-weth-name
  (fn [db [_ token-name]]
    (->> token-name
         (assoc-in db [:tokens :weth :name]))))

(rf/reg-event-fx ::fetch-weth-name
  (fn [_ _]
    {:promise-n [{:call #(-> weth-contract
                             (p/then inter.con/name))
                  :on-success [::save-weth-name]
                  :on-failure [:foo]}]}))

(rf/reg-sub ::weth-balance
  (fn [db _]
    (get-in db [:tokens :weth :balance])))

(rf/reg-event-db ::save-weth-balance
  (fn [db [_ token-name]]
    (js/console.log token-name)
    (->> token-name
         inter.ethers/format-ether
         (assoc-in db [:tokens :weth :balance]))))

(rf/reg-event-fx ::fetch-weth-balance
  (fn [_ [_ address]]
    {:promise-n [{:call (fn []
                          (-> weth-contract
                              (p/chain #(inter.con/balance-of % address))))
                  :on-success [::save-weth-balance]
                  :on-failure [:foo]}]}))

(rf/reg-sub ::weth-zapper-allowance
  (fn [db _]
    (some-> (get-in db [:contracts :weth-zapper :allowance]))))

(rf/reg-event-db ::save-weth-zapper-allowance
  (fn [db [_ token-name]]
    (js/console.log token-name)
    (->> token-name
         inter.ethers/format-ether
         (assoc-in db [:contracts :weth-zapper :allowance]))))

(rf/reg-event-fx ::fetch-weth-zapper-alllowance
  (fn [_ [_ address spender]]
    {:promise-n [{:call (fn []
                          (-> weth-contract
                              (p/chain #(inter.con/allowance % address spender))))
                  :on-success [::save-weth-zapper-allowance]
                  :on-failure [:foo]}]}))

(rf/reg-event-db ::successfully-approved-weth-balance
  (fn [db [_ token-name]]
    (js/console.log token-name)
    (->> token-name
         inter.ethers/format-ether
         (assoc-in db [:tokens :weth :approved-balance]))))

(rf/reg-event-fx ::approve-weth-balance
  (fn [_ [_ contract-address amount]]
    (js/console.dir (clj->js {:approved-amount (inter.ethers/parse-units amount)}))
    {:promise-n [{:call (fn []
                          (-> weth-contract
                              (p/chain #(inter.con/approve %
                                                           contract-address
                                                           (inter.ethers/parse-units amount)))))
                  :on-success [::successfully-approved-weth-balance]
                  :on-failure [:foo]}]}))


(rf/reg-event-db :foo
  (fn [db [_ e]]
    (js/console.warn e)
    (assoc-in db [:errors] e)))

(comment
 (rf/dispatch [::weth-name])
 (let [address @(rf/subscribe [:shadow-re-frame.re-frame.ethers/account])]
  (rf/dispatch [::weth-balance address])))
