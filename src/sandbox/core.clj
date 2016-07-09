(ns sandbox.core
  (:gen-class)
  (:use [sandbox.world])
  (:require [clojure.string :as s]))

(defn render-tile [tile]
  (case tile
    :wall \#
    :floor \.))

(defn render-creature [creature]
  (case (:type creature)
    :goblin \g))

(defn render-world-at-tile [world pos]
  (let [creature (world-creature-at world pos)
        tile (world-tile-at world pos)]
    (if creature
      (render-creature creature)
      (render-tile tile))))

(defn render-world [world]
  (s/join "\n"
          (for [y (range (:height world))]
            (s/join (for [x (range (:width world))]
                      (render-world-at-tile world [x y]))))))

(defn -main [& args]
  (let [world (make-world 10 10)]
    (println (render-world world))))

(def ^:dynamic *world*
  (-> (make-world 10 10)
      (world-add-creature (make-creature) [3 3])
      (world-add-creature (make-creature) [5 5])))

(println (render-world *world*))
