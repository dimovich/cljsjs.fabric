(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[cljsjs/boot-cljsjs "0.7.1" :scope "test"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "1.7.19")
(def +version+ (str +lib-version+ "-1"))

(task-options!
 pom  {:project     'cljsjs/fabric
       :version     +version+
       :description "Fabric.js is a powerful and simple Javascript HTML5 canvas library"
       :url         "http://fabricjs.com"
       :scm         {:url "https://github.com/cljsjs/packages"}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask package []
  (comp
    (download :url (format "https://raw.githubusercontent.com/kangax/fabric.js/v%s/dist/fabric.js" +lib-version+))
    (download :url (format "https://raw.githubusercontent.com/kangax/fabric.js/v%s/dist/fabric.min.js" +lib-version+))
    (replace-content :in "fabric.min.js" :match #"(?m)^//# sourceMappingURL=.*$" :value "")
    (sift :move {#"^fabric.js"     "cljsjs/fabric/development/fabric.inc.js"
                 #"^fabric.min.js" "cljsjs/fabric/production/fabric.min.inc.js"})
    (sift :include #{#"^cljsjs"})
    (deps-cljs :name "cljsjs.fabric")
    (pom)
    (jar)
    (install)))

