(ns cljs-browser-repl.state
  (:require [reagent.core :refer [atom]]))

;; History

(defn now [] (.now js/Date))

(defn gen-tx []
  (gensym (str "TX" (now))))

(defn add-entry 
  [h e]
  {:pre [(fn [e] (every? true? (map #(contains? e %) [:type :value :tx])))]}
  (conj h e))

(defn to-repl 
  ([o] (assoc o :tx (gen-tx)))
  ([t v] (to-repl {:type t :value v})))

(defn to-repl-input  [source] (to-repl :input    source))
(defn to-repl-html   [html]   (to-repl :html     html))
(defn to-repl-md     [md]     (to-repl :markdown md))
(defn to-repl-error  [err]    (to-repl :error    err))
(defn to-repl-result [resp]   (to-repl :response resp))

(def types-for-user
  {:input "CLJS"
   :markdown "MD"
   :html "HTML"})

(def initial-history-messages
  [(to-repl-md "# Hi! Welcome to the web clojurescript repl

Enter any forms in the input at the bottom. Hit enter to evaluate.

Here are some examples of things to try (click on the next entry and e.g. replace `comment` with `do`):")
   (to-repl-input "(comment
  (doc inc)
  (inc 5)
  (- 5 3)
  (defn square [x] (* x x))
  (square 6)
)")
   ])

(defonce history (atom []))

;; Compiler

(defonce current-ns (atom 'cljs.user))

;; UI

(defonce blank-input (to-repl-input ""))

(defonce input (atom blank-input))
