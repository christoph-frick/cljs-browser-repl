(ns cljs-browser-repl.ui.cljs-browser-repl
  (:require [cljs-browser-repl.ui.top-bar :refer [top-bar]]
            [cljs-browser-repl.ui.history :refer [history]]
            [cljs-browser-repl.ui.repl-input :refer [repl-input]]
            [cljs-browser-repl.actions.repl :refer [repl-input!]]
            [cljs-browser-repl.state :as state]
            ))

(defn cljs-browser-repl []
  (println @state/current-ns)
  [:div.cljs-browser-repl
   [top-bar]
   [history @state/history]
   [repl-input {:pre-label (str @state/current-ns)
                :on-input repl-input!}]])
