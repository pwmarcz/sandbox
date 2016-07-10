(ns sandbox.world)

(defn make-creature [type]
  {:type type})

(defn world-add-creature [world creature pos]
  (let [id (:next-creature-id world)
        creature (assoc creature :pos pos :id id)]
    [(-> world
         (assoc-in [:creatures (:id creature)] creature)
         (update :next-creature-id inc))
     id]))

(defn world-creature-at [world pos]
  (some (fn [[id creature]]
          (when (= pos (:pos creature))
            creature))
        (:creatures world)))

(defn world-creature [world creature-id]
  (get-in world [:creatures creature-id]))

(defn world-tile-at [world [x y]]
  (get-in (:map world) [y x]))

(defn tile-walkable? [tile]
  (= tile :floor))

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

(defn pos+dir [[x y] dir]
  (case dir
    :nw [(dec x) (dec y)]
    :n [x (dec y)]
    :ne [(inc x) (dec y)]
    :e [(inc x) y]
    :se [(inc x) (inc y)]
    :s [x (inc y)]
    :sw [(dec x) (inc y)]
    :w [(dec x) y]))

(defmulti apply-event (fn [world event] (:type event)))

(defn apply-events [world events]
  (reduce apply-event world events))

(defmethod apply-event :move [world {:keys [creature-id dir]}]
  (update-in world [:creatures creature-id]
             (fn [creature]
               (update creature :pos #(pos+dir % dir)))))

(defmulti apply-command (fn [world command] (:type command)))

(defmethod apply-command :move [world {:keys [creature-id dir]}]
  (let [creature (get-in world [:creatures creature-id])
        new-pos (pos+dir (:pos creature) dir)]
    (if (and (tile-walkable? (world-tile-at world new-pos))
             (nil? (world-creature-at world new-pos)))
      [{:type :move :creature-id creature-id :dir dir}]
      [])))

(defmethod apply-command :wait [world _]
  [])
