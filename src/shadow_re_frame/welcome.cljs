(ns shadow-re-frame.welcome)

(def welcome
  [:div#welcome.font-normal

   [:ol
    {:style {:text-align "left"}}

    [:li
     [:p [:b [:a {:href "https://github.com/Day8/re-frame-trace"} "re-frame-trace"]]]

     [:p "This is basically " [:b "devtools for re-frame"] ". It's what you see here: \uD83D\uDC49 \uD83D\uDC49 \uD83D\uDC49 \uD83D\uDC49"]

     [:p "Observe re-frame " [:b "events"] " as they occur, and inspect the " [:b "app state"] "."]

     [:p "This was the " [:a {:href "https://railsgirlssummerofcode.org/"} "RailsGirls Summer of Code"]
      "  project of "
      [:a {:href "http://twitter.com/lipskasa"} "Saskia"] " and "
      [:a {:href "http://twitter.com/daiyitastic"} "Daiyi"]
      ". They did some great work. Read the " [:a {:href "http://www.daiyi.co/dev-diary/2017/10/19/re-frame-trace/"} "announcement blog post"] " for more details."]]

    [:li
     [:p "the "
      [:b [:a {:href "https://github.com/thheller/shadow-cljs/"} "shadow-cljs"]] " build tool by "
      [:a {:href "https://twitter.com/thheller"} "@thheller"] "."]

     [:p "An excellent live-reloading ClojureScript tool, compiles with the speed of ⚡, give or take. Check out the "
      [:a {:href "https://github.com/mhuebert/shadow-re-frame/blob/master/shadow-cljs.edn"} "shadow-cljs.edn"] " config file for this project."]]

    [:li
     [:p [:b [:a {:href "https://github.com/braintripping/re-view/tree/master/re-frame-simple"}
              "re-frame-simple,"]] " a simplified syntax for re-frame."]

     [:p
      "Use re-frame without boilerplate, using events based on core Clojure functions. Still plays well with existing re-frame functions. "
      "See the " [:a {:href "https://github.com/braintripping/re-view/blob/master/re-frame-simple/README.md"} "readme"] " for details."]]]
   [:div.font-large {:style {:margin-top "2rem"}} "🙃"]])