(ns sandbox.core
  (:gen-class)
  (:use [sandbox.world])
  (:require [clojure.string :as string]
            [lanterna.screen :as s]))

(defn get-tile-glyph [tile]
  (case tile
    :wall [\# :black :bold]
    :floor [\. :white]))

(defn get-creature-glyph [creature]
  (case (:type creature)
    :goblin [\g :blue :bold]
    :human [\@ :white :bold]))

(defn get-glyph-at [world pos]
  (let [creature (world-creature-at world pos)
        tile (world-tile-at world pos)]
    (if creature
      (get-creature-glyph creature)
      (get-tile-glyph tile))))

(defn display-world [scr world player-id]
  (dotimes [y (:height world)]
    (dotimes [x (:width world)]
      (let [[char color & styles] (get-glyph-at world [x y])]
        (s/put-string scr x y (str char) {:fg color :styles styles}))))
  (let [player (world-creature world player-id)
        [x y] (:pos player)]
    (s/move-cursor scr x y))
  (s/redraw scr))

(defn parse-command [key]
  (case key
    \q {:type :quit}
    (\y \7) {:type :move, :dir :nw}
    (\u \9) {:type :move, :dir :ne}
    (\h \4) {:type :move, :dir :w}
    (\j \2) {:type :move, :dir :s}
    (\k \8) {:type :move, :dir :n}
    (\l \6) {:type :move, :dir :e}
    (\b \1) {:type :move, :dir :sw}
    (\n \3) {:type :move, :dir :se}
    (\. \5) {:type :wait}
    nil))

(defn play-game [scr world player-id]
  (loop [scr scr
         world world]
    (display-world scr world player-id)
    (let [key (s/get-key-blocking scr)
          cmd (parse-command key)]
      (cond
        (nil? cmd) (recur scr world)
        (= (:type cmd) :quit) nil
        :else (let [events (apply-command world (assoc cmd :creature-id player-id))]
                (recur scr (apply-events world events)))))))

(defn -main [& args]
  (let [scr (s/get-screen :text)
        world (make-world 20 10)
        [world _] (world-add-creature world
                                      (make-creature :goblin)
                                      [1 1])
        [world player-id] (world-add-creature world
                                              (make-creature :human)
                                              [2 2])]
    (s/start scr)
    (play-game scr world player-id)
    (s/stop scr)))
