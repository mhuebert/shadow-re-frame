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
   [shadow-re-frame.views.weth-zapper :as v.weth-zapper]

   [shadow-re-frame.components.buttons :as cmps.btn]
   [shadow-re-frame.components.forms :as cmps.form]

   [fork.re-frame :as fork]
   [fork.re-frame :as fork-re-frame]))

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
     :view v.weth-zapper/render}]

   ["/about"
    {:name ::about
     :view about-page}]

   ["/item/:id"
    {:name ::item
     :view item-page
     :parameters {:path {:id int?}
                  :query {(ds/opt :foo) keyword?}}}]])

(defn ^:export ^:dev/after-load render []
  (reit.fe/start! (reit.f/router routes {:data {:coercion reit.spec/coercion}})
                  (fn [m] (reset! match m))
                  {:use-fragment false})
  (rdom/render [current-page]
               (gdom/getRequiredElement "shadow-re-frame")))

(defn ^:export init []
  (rf/dispatch-sync [:initialize])
  (render))

