(ns ^:figwheel-always cljs-browser-repl.core
    (:require [reagent.core :as reagent]
              [cljs-browser-repl.ui.cljs-browser-repl :refer [cljs-browser-repl]]))

(enable-console-print!)

(defn render! []
  (reagent/render-component [cljs-browser-repl]
                            (. js/document (getElementById "app"))))

(render!)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (render!)
)

