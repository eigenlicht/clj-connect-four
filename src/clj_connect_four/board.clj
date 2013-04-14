(ns clj-connect-four.board)

(def player [" ", "X", "O"])

(def empty-board
  "Create vector board of 6x7 for game state and
   empty bitboards for each player (just 0s)."
  [(vec (repeat 6 (vec (repeat 7 (player 0))))), 0, 0])

(defn get-y
  "Determines y-coordinate for given x-coordinate."
  [board x]
  (first (filter #(= (get-in board [% x]) (player 0))
                 (range 5 -1 -1))))

(defn bit-insert
  "Sets the bit of the given bitboard at position (y, x)."
  [bitboard y x]
  (bit-set bitboard (+ (* x 7) y)))

(defn insert
  "Inserts symbol for given player (either 1 or 2) at specified x
  and sets according bit on his bitboard."
  [boards x player-num]
  (if (nil? (get-y (boards 0) x)) nil
  (let [y (get-y (boards 0) x)
        vec-board (assoc-in (boards 0) [y x] (player player-num))
        bitboard1 (if (= player-num 1)
                    (bit-insert (boards 1) (- 5 y) x)
                    (boards 1))
        bitboard2 (if (= player-num 2)
                    (bit-insert (boards 2) (- 5 y) x)
                    (boards 2))]
        [vec-board bitboard1 bitboard2])))

(defn print-board
  [board]
  (println [1 2 3 4 5 6 7])
  (doseq [row board] (println row)))

(defn bit-print-board
  [bitboard sym]
  (println [1 2 3 4 5 6 7])
  (doseq [bit-row (for [y (range 5 -1 -1)]
                    (for [x (range 0 43 7)]
                      (+ x y)))]
    (println (mapv #(if (bit-test bitboard %) sym "0") bit-row))))

(defn print-all-boards
  "For debugging: Print the vector and both bitboards."
  [boards]
  (print-board     (boards 0))            (println)
  (bit-print-board (boards 1) (player 1)) (println)
  (bit-print-board (boards 2) (player 2)) (println))