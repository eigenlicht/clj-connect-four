(ns clj-connect-four.board)

(def player ["_", "X", "O"])

(def empty-boards
  "Empty bitboards for each player and game state (just 0s)."
  [0 0 0])

(def board-bits
  "All bits which are inside the bitboard."
  (vec (flatten (for [y (range 5 -1 -1)]
                  (for [x (range 0 43 7)]
                    (+ x y))))))

(defn get-y
  "Determines y-coordinate for given x-coordinate."
  [board x]
  (first (filter #(not (bit-test board (+ % (* x 7))))
                 (range 0 6))))

(defn bit-insert
  "Sets the bit of the given bitboard at position (y, x)."
  [board y x]
  (bit-set board (+ y (* x 7))))

(defn insert
  "Inserts symbol for given player (either 1 or 2) at specified x
  and sets according bit on his bitboard."
  [boards x player-num]
  (if (nil? (get-y (boards 0) x)) nil
    (let [y (get-y (boards 0) x)
          bitboard (bit-insert (boards 0) y x)
          bitboard-p1 (if (= player-num 1)
                        (bit-insert (boards 1) y x)
                        (boards 1))
          bitboard-p2 (if (= player-num 2)
                        (bit-insert (boards 2) y x)
                        (boards 2))]
      [bitboard bitboard-p1 bitboard-p2])))

(defn board-full? [boards]
  (empty? (filter #(not (bit-test (boards 0) %)) board-bits)))

(defn gen-game-state [boards]
  (for [y (range 5 -1 -1)]
    (vec (for [x (range 0 7)]
           (if (bit-test (boards 1) (+ y (* 7 x))) (player 1)
             (if (bit-test (boards 2) (+ y (* 7 x))) (player 2)
               (player 0)))))))

(defn print-board
  [boards]
  (println [1 2 3 4 5 6 7])
  (doseq [row (gen-game-state boards)]
    (println row)))