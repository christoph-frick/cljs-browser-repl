(ns cljs-browser-repl.ui.history-entry
  (:require [clojure.string :as string]
            [cljs-browser-repl.markdown :as md]))

(declare history-entry)

(defmulti render-history-entry
  (fn [e] (get e :type)))

(defmethod render-history-entry :input
  [{:keys [value]}]
  [:div.history-input value])

(defn- response-with-meta->entry [{:keys [value] :as entry}]
  (let [sub-type (:type (meta value))
        is-value-map? (= (type value) cljs.core/PersistentArrayMap)
        value-of-value? (not (nil? (:value value)))
        new-value (if (and is-value-map? value-of-value?)
                    (:value value) value)]
    (with-meta (assoc entry :type sub-type :value new-value) nil)))

(defmethod render-history-entry :response 
  [{:keys [value] :as entry}]
  (let [sub-type (:type (meta value))]
    [:div.history-response
     {:class (if sub-type "" "history-response-cljs")}
     (if sub-type
       [history-entry nil (response-with-meta->entry entry)]
       (println-str value))]))

(defmethod render-history-entry :error
  [{:keys [value]}]
  [:div.history-response-error (.. value -cause -message)])

(defmethod render-history-entry :html [{:keys [value]}]
  [:div.history-html
   {:dangerouslySetInnerHTML {:__html value}}])

(defmethod render-history-entry :markdown [{:keys [value]}]
  [:div.history-markdown
   {:dangerouslySetInnerHTML {:__html (md/render value)}}])

(defmethod render-history-entry :default [entry]
  [:pre.history-unknown (println-str entry)])

(defn history-entry [{:keys [on-click]} entry]
  [:div.history-entry
   {:on-click #(on-click entry)}
   (render-history-entry entry)])
