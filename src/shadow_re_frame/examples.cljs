(ns shadow-re-frame.examples
  (:require [re-view.core :as v :refer [defview]]))


(defview custom-cursor
         "A more advanced example using the :view/state atom, a lifecycle method,
          and a custom method."
         {:view/initial-state    {:mouse-position [150 100]}
          :update-mouse-position (fn [{:keys [view/state] :as this} e]
                                   (let [element (v/dom-node this)
                                         position (.getBoundingClientRect element)]
                                     (swap! state assoc :mouse-position [(- (.-clientX e) (.-left position))
                                                                         (- (.-clientY e) (.-top position))])))}
         [this]
         (let [{[mouse-left mouse-top] :mouse-position} @(:view/state this)]
           [:div
            {:on-mouse-move #(.updateMousePosition this %)
             :style         {:background-color "#4bc57e"
                             :position         "relative"
                             :padding          100
                             :cursor           "none"
                             :overflow         "hidden"}}
            [:div {:style {:position  "absolute"
                           :left      (- mouse-left 25)
                           :top       (- mouse-top 25)
                           :transform "scale(2)"
                           :width     0
                           :height    0}} "😀"]]))

(defview todo-list
         "A minimal to-do list using the local state atom."
         {:view/initial-state {:items           [{:label   "Buy milk"
                                                  :checked false}]
                               :next-item-label ""}}
         [{:keys [view/state] :as this}]
         [:div
          [:input {:value       (:next-item-label @state)
                   :style       {:font-size  "16px"
                                 :padding    8
                                 :width      "100%"
                                 :margin     "10px 0"
                                 :box-sizing "border-box"}
                   :placeholder "Add item..."
                   :on-change   #(swap! state assoc :next-item-label (.. % -target -value))
                   :on-key-down #(when (= 13 (.-keyCode %))
                                   (reset! state (-> @state
                                                     (update :items conj {:label   (:next-item-label @state)
                                                                          :checked false})
                                                     (assoc :next-item-label ""))))}]

          (->> (:items @state)
               (map-indexed (fn [index {:keys [label checked]}]
                              [:label
                               {:key   index
                                :style {:text-decoration (when checked "line-through")
                                        :display         "block"
                                        :cursor          "pointer"}}
                               [:input {:type      "checkbox"
                                        :value     checked
                                        :on-change #(swap! state assoc-in [:items index :checked] (.. % -target -checked))
                                        :style     {:margin 10}}]
                               label]))
               (reverse))])
