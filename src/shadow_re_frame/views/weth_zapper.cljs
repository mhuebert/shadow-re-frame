(ns shadow-re-frame.views.weth-zapper
  (:require [re-frame.core :as rf]
            [fork.re-frame :as fork]
            [tailwind-hiccup.core :refer [tw]]

            [shadow-re-frame.interop.ethers :as inter.ethers]
            [shadow-re-frame.re-frame.ethers :as rf.ethers]
            [shadow-re-frame.re-frame.weth :as rf.weth]


            [shadow-re-frame.components.forms :as cmps.form]))

(rf/reg-event-fx
 ::submit-handler
  (fn [{db :db} [_ {:keys [values dirty path
                           need-token-approval?]}]]
    ;; dirty tells you whether the values have been touched before submitting.
    ;; Its possible values are nil or a map of changed values
    (let [approval-amount (values "token-approval-val")]
      {:db (fork/set-submitting db path true)
       :dispatch (if need-token-approval?
                   [::rf.weth/approve-balance
                    "0xf5C678Be432F07261e728a58bFFEAC52bA731BF5"
                    approval-amount]
                   [::rf.weth/zap
                    approval-amount])})))

(rf/reg-event-fx
 ::resolved-form
  (fn [{db :db} [_ path values]]
    (js/alert values)
    {:db (fork/set-submitting db path false)}))

(defn my-form
  [{:keys [values handle-change
           handle-blur form-id
           handle-submit props]}]
  (let [{:keys [submit-text]} props]
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
                   {:type "submit"
                    #_#_:disabled true})
       submit-text]]]))

(defn render []
  [:div
   [:h2 "Welcome to frontend"]
   (let [address @(rf/subscribe [::rf.ethers/account])
         block-number @(rf/subscribe [::inter.ethers/current-block])
         weth-balance @(rf/subscribe [::rf.weth/balance])
         weth-allowance @(rf/subscribe [::rf.weth/zapper-allowance address
                                        "0xf5C678Be432F07261e728a58bFFEAC52bA731BF5"])
         need-token-approval? (< weth-allowance weth-balance)]
     [:div
      [:div address]
      [:div weth-balance]
      [:div [:p "Zapper Contract Allowance"] weth-allowance]
      [fork/form {:initial-values {"token-approval-val" 0.0}
                  :props {:submit-text (if need-token-approval?
                                         "Approve" "Zap")}
                  :path [:form]
                  :form-id "form-id"
                  :prevent-default? true
                  :on-submit #(rf/dispatch [::submit-handler (merge % {:need-token-approval?
                                                                       need-token-approval?})])
                  :clean-on-unmount? true}
       my-form]
      [:div block-number]])])
