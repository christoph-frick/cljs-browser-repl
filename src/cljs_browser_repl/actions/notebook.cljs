(ns cljs-browser-repl.actions.notebook
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-browser-repl.net.gist :as gist]
            [cljs.core.async :refer [<!]]
            [cljs.pprint :refer [pprint]]
            [cljs-browser-repl.state :as state]
            ))

(defn from-gist! [id]
  (go
    (let [gist (<! (gist/get! id))
          commands (gist/get-commands gist)]
      (reset! state/history (mapv state/to-repl commands)))))
