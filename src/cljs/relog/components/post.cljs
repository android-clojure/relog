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
    [:div {:class "Post"}
     [:div {:class "Post_content" :dangerouslySetInnerHTML 
            {:__html (js/marked samplePost)} }]]))


