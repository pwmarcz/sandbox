(ns sandbox.core
  (:gen-class)
  (:require [clojure.string :as s]))

(defn make-creature [id pos]
  {:type :goblin
   :id id
   :pos pos})

(defn world-creature-at [world pos]
  (some (fn [[id creature]]
          (when (= pos (:pos creature))
            creature))
        (:creatures world)))

(defn world-tile-at [world pos]
  (get-in (:map world) pos))

(defn make-map [w h]
  (assert (and (> w 1) (> h 1)))
  (let [full-line (repeat w :wall)
        empty-line (concat [:wall] (repeat (- w 2) :floor) [:wall])]
    (vec (concat [(vec full-line)]
                 (repeat (- h 2) (vec empty-line))
                 [(vec full-line)]))))

(defn make-world [w h]
  {:map (make-map w h)
   :width w
   :height h
   :creatures {42 (make-creature 42 [2 2])
               45 (make-creature 45 [3 5])}})

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

(def ^:dynamic *world* (make-world 10 10))
(println (render-world *world*))
