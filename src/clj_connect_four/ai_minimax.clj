(ns clj-connect-four.ai-minimax
  (:require [clj-connect-four.board       :as board]
            [clj-connect-four.check-board :as check]))

;;; HEURISTIC FUNCTIONS ;;;

;; consider connected coins
; define score points for 2, 3 and 4 connected coins
(def CC4  1048576)
(def CC3  32)
(def CC2  4)

(defn bit-count
  "Checks how many bits are set on given bitboard."
  [board]
  (map #(bit-test board %) board/board-bits))
         
(defn get-score
  "Determines score of a bitboard manipulated by check-board-*."
  [check points]
  (* points
     (apply + (map #(count (filter true? (bit-count %))) check))))

(defn heuristic
  "Calculates the main heuristic value of given bitboards for a player."
  [boards player-num]
  (apply +
    (map (fn [[c p]] (get-score c p))
         [[(check/check-board-4 (boards player-num)) CC4]
          [(check/check-board-3 (boards player-num)) CC3]
          [(check/check-board-2 (boards player-num)) CC2]
          [(check/check-board-4 (boards (- 3 player-num))) (- 1 CC4)]
          [(check/check-board-3 (boards (- 3 player-num))) (- 1 CC3)]
          [(check/check-board-2 (boards (- 3 player-num))) (- 1 CC2)]])))

;; consider possible winning combinations
;; (only when first heuristic returns equal values)
(defn get-diags
  "Generates diagonals of given starting position using given step-f."
  [step-fn start-pos]
  (for [pos start-pos]
    (take 4 (iterate step-fn pos))))

(def win-combos
  "All 69 possible winning combinations."
  (let [rows (for [y (range 6), j (range 4)]
               (for [i (range 4)]
                 [y (+ i j)]))
        columns (for [x (range 7), j (range 3)]
                  (for [i (range 4)]
                    [(+ i j) x]))
        diagonals (concat
                   ; descending diagonals \
                   (get-diags (partial mapv inc)
                              (for [y (range 3), x (range 4)]
                                [y x]))
                   ; ascending diagonals /
                   (get-diags (fn [[y x]] [(inc y) (dec x)])
                              (for [y (range 3), x (range 3 7)]
                                [y x])))]
    (concat rows columns diagonals)))

(defn filter-current-move [y x coll]
  "Filter win-combos for coords including given [y x]."
  (if (nil? y)
    (some #{[0 x]} coll)
    (some #{[(inc y) x]} coll)))

(defn filter-open-combos [player-num boards coll]
  "Filter for combos which are still open."
  (some (fn [[y x]] (or (not (bit-test (boards 0) (+ y (* 7 x))))
                        (bit-test (boards player-num) (+ y (* 7 x)))))
        coll))

(defn heuristic2
  "Calculate second heuristic value."
  [boards player-num x]
  (count (filter
          #(and
            (filter-current-move (board/get-y (boards 0) x) x %)
            (filter-open-combos player-num boards %))
          win-combos)))

;;; MINIMAX ALGORITHM ;;;

(defn not-nil? [x]
  (not (nil? x)))

(defn get-max [coll]
  (if (empty? coll) 0
    (apply max coll)))

(defn minimax
  "Minimax algorithm using only main heuristic."
  [boards player-num x depth]
  (if (or (nil? boards)
          (nil? (board/insert boards x player-num))) nil
    (if (= depth 0)
      (heuristic (board/insert boards x player-num) player-num)
      (- (heuristic (board/insert boards x player-num) player-num)
         (get-max (filter not-nil?
                          (map #(minimax
                                 (board/insert boards x player-num)
                                 (- 3 player-num)
                                 %
                                 (- depth 1))
                               [0 1 2 3 4 5 6])))))))

(defn get-highest-index [coll]
  (apply max-key second
         (filter #(not-nil? (second %)) coll)))

(defn make-move
  "Generate next move using minimax
  and second heuristic if needed."
  [boards player-num depth]
  (let [heuristics (map #(minimax boards player-num % depth)
                        [0 1 2 3 4 5 6])
        highest (get-highest-index (map-indexed vector heuristics))]
    (println heuristics)
    (if (> (count (filter #{(second highest)} heuristics)) 1)
      ; equal values from first heuristics - look at second
      (first (get-highest-index
              (map #(vector (first %) (heuristic2 boards player-num (first %)))
                   ; only consider the highest and equal values
                   (filter #(= (second highest) (second %))
                           (map-indexed vector heuristics)))))
      (first highest))))
