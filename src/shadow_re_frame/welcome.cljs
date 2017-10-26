(ns shadow-re-frame.welcome)

(def welcome-text
  [:div.font-normal.welcome

   [:div.font-large {:style {:margin "1rem 0"}} "Welcome!"]

   [:div.font-normal

    "You've reached a " [:b "demo page"] " of some exciting new things!"


    [:ol
     {:style {:text-align "left"}}
     [:li
      [:p [:b [:a {:href "https://github.com/Day8/re-frame-trace"} "re-frame-trace"]]]

      [:p "This is basically " [:b "devtools for re-frame"] ". It's what you see here: \uD83D\uDC49 \uD83D\uDC49 \uD83D\uDC49 \uD83D\uDC49"]

      [:p "Observe re-frame " [:b "events"] " as they occur, and inspect the " [:b "app state"] ". All live-updating. Very useful!"]

      [:p "This was the " [:a {:href "https://railsgirlssummerofcode.org/"} "RailsGirls Summer of Code"]
       "  project of "
       [:a {:href "http://twitter.com/lipskasa"} "Saskia"] " and "
       [:a {:href "http://twitter.com/daiyitastic"} "Daiyi"]
       ". They did some great work. Read the " [:a {:href "http://www.daiyi.co/dev-diary/2017/10/19/re-frame-trace/"} "announcement blog post"] " for more details."]]

     [:li
      [:p "the "
       [:b [:a {:href "https://github.com/thheller/shadow-cljs/"} "shadow-cljs"]] " build tool by "
       [:a {:href "https://twitter.com/thheller"} "@thheller"] "." ]

      [:p [:i "shadow-cljs"] " provides an excellent live-reloading development experience and compiles with the speed of ⚡, give or take. Check out the "
       [:a {:href "https://github.com/mhuebert/shadow-re-frame/blob/master/shadow-cljs.edn"} "shadow-cljs.edn"] " config file for this project."]]

     [:li
      [:p [:b [:a {:href "https://github.com/braintripping/re-view/tree/master/re-frame-simple"}
               "re-frame-simple,"]] " a simplified syntax for re-frame."]

      [:p [:i "re-frame-simple"]
       " lets you read and write from the re-frame db without any boilerplate code at all, using events based on core Clojure functions. This is great for getting started and prototyping. You can, of course, use more advanced techniques when necessary. "]
      [:p "See the " [:a {:href "https://github.com/braintripping/re-view/blob/master/re-frame-simple/README.md"} "readme"] " for more details."]]]
    [:div.font-large {:style {:margin-top "2rem"}} "🙃"]]])