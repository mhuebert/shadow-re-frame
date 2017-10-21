(ns shadow-re-frame.provide-js-deps
  (:require ["react" :as react]
            ["react-dom" :as react-dom]
            ["create-react-class" :as create-react-class]
            ["d3" :as d3]))

(set! (.-React js/window) react)
(set! (.-ReactDOM js/window) react-dom)
(set! (.-createReactClass js/window) create-react-class)
(set! (.-d3 js/window) d3)