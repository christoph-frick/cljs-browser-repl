(ns cljs-browser-repl.ui.repl-input
  (:require [clojure.string :as string]))

(defn resize [node]
  (set! (.. node -style -height) "auto")
  (set! (.. node -style -height) (str (.-scrollHeight node) "px")))

(defn enter?
  "Is an event the Enter key?"
  [e] (= (.-key e ) "Enter"))

(defn shift? 
  [e]
  (boolean (.-shiftKey e)))

(defn ctrl? 
  [e]
  (boolean (.-ctrlKey e)))

(defn submit-input-keys?
  [e]
  (every? true? ((juxt ctrl? enter?) e)))

(defn get-val [e] (.. e -target -value))

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
                    (when (submit-input-keys? e)
                      (let [v (string/trim (get-val e))
                            t (:type value)
                            valid? (if (= t :input) (valid-input? v) true) ; FIXME: there need to be some protocol around validating input per type 
                            send-value {:value v :type t} ; FIXME
                            ]
                        (when valid? (on-valid-input send-value)))))
     :on-change #(on-change (assoc value :value (get-val %)))
     :placeholder "Type code here and press CTRL-Enter to submit"
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
