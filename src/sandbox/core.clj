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
    :goblin \g
    :human \@))

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
  (let [world (make-world 10 10)
        [world goblin-id] (world-add-creature world
                                              (make-creature :goblin)
                                              [1 1])
        [world human-id] (world-add-creature world
                                              (make-creature :human)
                                              [2 2])
        events (apply-command world {:type :move, :creature-id human-id, :dir :n})
        world (apply-events world events)]
    (println events)
    (println (render-world world))))
