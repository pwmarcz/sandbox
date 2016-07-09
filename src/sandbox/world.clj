(ns sandbox.world)

(defn make-creature []
  {:type :goblin})

(defn world-add-creature [world creature pos]
  (let [id (:next-creature-id world)
        creature (assoc creature :pos pos :id id)]
    (-> world
        (assoc-in [:creatures (:id creature)] creature)
        (update :next-creature-id inc))))

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
   :creatures {}
   :next-creature-id 0})
