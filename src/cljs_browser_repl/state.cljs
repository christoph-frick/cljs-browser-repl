(ns cljs-browser-repl.state
  (:require [reagent.core :refer [atom]]))

;; History

(defn now [] (.now js/Date))
(defn add-entry [h e] (conj h e))

(defn to-repl [o] (assoc o :date (now)))
(defn to-repl-input  [source] (to-repl {:type :input    :value source}))
(defn to-repl-html   [html]   (to-repl {:type :html     :value html}))
(defn to-repl-md     [md]     (to-repl {:type :markdown :value md}))
(defn to-repl-error  [err]    (to-repl {:type :error    :value err}))
(defn to-repl-result [resp]   (to-repl {:type :response :value resp}))

(def initial-history-message
  (to-repl-input "(comment

  Hi! Welcome to the web clojurescript repl.

  Enter any forms in the input at the bottom. Hit enter to evaluate.

  Here are some examples of things to try:

    (doc inc)
    (inc 5)
    (- 5 3)
    (defn square [x] (* x x))
    (square 6)

  Have fun!

  )"))

(defonce history (atom []))

;; Compiler

(defonce current-ns (atom 'cljs.user))

;; UI

(defonce blank-input (to-repl-input ""))

(defonce input (atom blank-input))
