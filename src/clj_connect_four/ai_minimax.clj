(ns clj-connect-four.ai-minimax
  (:require [clj-connect-four.board       :as board]
            [clj-connect-four.check-board :as check]))

;;; HEURISTIC FUNCTION ;;;

;; define score points for 2, 3 and 4 connected coins
(def CC4  1048576)
(def CC3  32)
(def CC2  4)

(def bits-to-check
  "All bits which are inside the bitboard."
  (vec (flatten (for [y (range 5 -1 -1)]
                  (for [x (range 0 43 7)]
                    (+ x y))))))

(defn bit-count
  "Checks how many bits are set on given bitboard."
  [bitboard]
  (map #(bit-test bitboard %) bits-to-check))
         
(defn get-score
  "Determines score of a bitboard manipulated by check-board-*."
  [check points]
  (* points
     (apply + (map #(count (filter true? (bit-count %))) check))))

(defn heuristic
  "Calculates the heuristic value of given bitboards for a player."
  [board player-num]
  (apply +
    (map (fn [[c p]] (get-score c p))
         [[(check/check-board-4 (board player-num)) CC4]
          [(check/check-board-3 (board player-num)) CC3]
          [(check/check-board-2 (board player-num)) CC2]])))

;;; MINIMAX ALGORITHM ;;;

(defn not-nil? [x]
  (not (nil? x)))

(defn minimax
  [board player-num x depth]
  (if (or (nil? board)
          (nil? (board/insert board x player-num))) nil
    (if (= depth 0)
      (heuristic (board/insert board x player-num) player-num)
      (- (heuristic (board/insert board x player-num) player-num)
         (apply max (filter not-nil?
                            (map #(minimax
                                   (board/insert board x player-num)
                                   (- 3 player-num)
                                   %
                                   (- depth 1))
                                 [0 1 2 3 4 5 6])))))))

(defn get-highest-index [coll]
  (apply max-key second
         (filter #(not-nil? (second %))
                 (map-indexed vector coll))))
  
(defn make-move
  [board player-num depth]
  (first (get-highest-index
          (map #(minimax board player-num % depth)
               [0 1 2 3 4 5 6]))))