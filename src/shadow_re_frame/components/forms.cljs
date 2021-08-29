(ns shadow-re-frame.components.forms
  (:require [tailwind-hiccup.core :refer [tw]]))

(defn input-with-label
  [{:keys [for label-val] :as label-props}
   {:keys [id name type auto-complete required] :as input-props}]
  [:div
   [:label (tw [:block :text-sm :font-medium :text-gray-700]
               {:for (or for id)})
    label-val]
   [:div {:class "mt-1"}]
   [:input (tw [:mt-1 :block :w-full :rounded-md :border-gray-300 :shadow-sm
                :focus:border-indigo-300 :focus:ring :focus:ring-indigo-200
                :focus:ring-opacity-50]
               input-props)]])
