(ns shadow-re-frame.components.buttons
  (:require [tailwind-hiccup.core :refer [tw]]))


(defn hamburger
  []
  [:button (tw [:cursor-pointer :text-xl :leading-none :px-3 :py-1 :border :border-solid :border-transparent :rounded
                :bg-transparent :block :lg:hidden :outline-none :focus:outline-none] {:type "button"})
   [:span (tw [:block :relative :w-6 :h-px :rounded-sm :bg-white])]
   [:span (tw [:block :relative :w-6 :h-px :rounded-sm :bg-white :mt-1])]
   [:span (tw [:block :relative :w-6 :h-px :rounded-sm :bg-white :mt-1])]])
