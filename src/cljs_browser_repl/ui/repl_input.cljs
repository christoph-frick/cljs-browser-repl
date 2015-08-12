(ns cljs-browser-repl.ui.repl-input
  (:require [clojure.string :as string]))

(defn resize [node]
  (set! (.. node -style -height) "auto")
  (set! (.. node -style -height) (str (.-scrollHeight node) "px")))

(defn enter?
  "Is an event the Enter key?"
  [e] (= (.-key e ) "Enter"))

(defn get-val [e] (.. e -target -value))

(defn enter-pressed!
  "When shift+enter adds a new line. When only enter if the input is valid it
  runs the callback function and clears value and triggers the resize. If the
  input is not valid i'll do as if it was a shift+enter"
  [e valid? code type send-input]
  (let [shift? (.-shiftKey e)]
    (when (and (not shift?) valid?)
      (send-input {:value code :type type}) ; FIXME
      (.preventDefault e)
      )))

(defn- repl-input-raw
  [{:keys [pre-label on-change on-valid-input valid-input? value]}]
  [:div.repl-input
   [:select.repl-input-type
    {:value (:type value)
     :on-change #(on-change (assoc value :type (keyword (get-val %))))}
    ;; FIXME have them as as records
    (map (fn [[k v]] [:option {:key k :value k} v]) 
         {:input "CLJS"
          :markdown "MD"
          :html "HTML"})]
   (when (= (:type value) :input)
     [:span.repl-input-pre pre-label])
   [:textarea.repl-input-input
    {:on-key-down (fn [e]
                    (when (enter? e)
                      (let [c (string/trim (get-val e))
                            t (:type value)
                            valid? (if (= t :input) (valid-input? c) true)] ; FIXME: there need to be some protocol around validating input per type
                        (enter-pressed! e valid? c t on-valid-input))))
     :on-change #(on-change (assoc value :value (get-val %)))
     :placeholder "Type clojurescript code here"
     :rows 1
     :value (:value value)
     }]])

(def repl-input
  (with-meta
    repl-input-raw
    {:component-did-update
     (fn [this old-argv]
       (let [input (.querySelector (.getDOMNode this) ".repl-input-input")]
         (resize input)))}))
