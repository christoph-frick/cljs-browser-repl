(ns cljs-browser-repl.actions.repl
  (:require [cljs-browser-repl.state :as state]
            [cljs-browser-repl.compiler :refer [read-eval-print empty-compiler-state]]
            [clojure.string :refer [blank?]]
            ))

(defonce repl-compiler-state (empty-compiler-state))

(defn new-input! 
  ([] (new-input! state/blank-input))
  ([s] (reset! state/input s)))

(defn repl-entry!
  ([code] (repl-entry! code true))
  ([{code :value code-type :type :as input} history?]
   (println code code-type history?)
   (when-not (blank? code)
     ; Reset the current input
     (new-input!)
     ; Add just typed command to history
     (when history?
       (swap! state/history state/add-entry (state/to-repl input)))
     (when (= code-type :input) 
       (read-eval-print
         repl-compiler-state state/current-ns
         code true
         (fn [{:keys [ns value error] :as ret}]
           ; Add result to history
           (when history?
             (swap! state/history state/add-entry
                    (if error
                      (state/to-repl-error error)
                      (do
                        (reset! state/current-ns ns)
                        (state/to-repl-result value)))))))))))

(defn insert-repl-intro! []
  (when (empty? @state/history)
    (swap! state/history 
           #(reduce state/add-entry % state/initial-history-messages))))
