(ns relog.post
  (:require [reagent.core :as r]
            [cljsjs.marked]))

(def samplePost "
  # Markdown test

  Some random test markdown. *Italics.*

  ```javascript
  function foo ()  {
    var bar = 'baz';
    return  {
     test: true;
    }
  }
  ```

  More text.

")

(defn Post []
  (fn []
    (.log js/console "rendering post")
    ;(.setOptions js/marked #js{ "hightlight" #(.-value
                                            ; (do (.log js/console "highlighting")
                                            ;    (.highlightAuto js/hljs %)))})
    [:div
     [:div {:dangerouslySetInnerHTML 
            {:__html (js/marked samplePost)} }]]))


