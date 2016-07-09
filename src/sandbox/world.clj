(ns sandbox.world)

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
