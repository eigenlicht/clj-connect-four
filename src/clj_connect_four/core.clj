(ns clj-connect-four.core
  (:require [clj-connect-four.board       :as board]
            [clj-connect-four.check-board :as check]
            [clj-connect-four.ai-minimax  :as ai-strong])
  (:gen-class))

(defn -main
  [& args]
  "Game loop. Runs until one player has connected four."
  (loop [player-num 1, boards board/empty-board]
    (board/print-board (boards 0)) ; normal output
    ;(board/print-all-boards boards) ; debug output
    (println)
    (printf "Player %d's turn: " player-num)
    (flush)
    (let [x (if (= player-num 1) (dec (read)) (ai-strong/make-move boards player-num 3))
;    (let [x (dec (read))
          new-boards (board/insert boards x player-num)
          has-won    (check/check-board (new-boards player-num))]
      (if (= player-num 2) (println (inc x)) nil)
      (if (not= has-won 0)
        (comp (printf "\n\n\nPlayer %d has won!\n" player-num)
              (board/print-board (new-boards 0)))
        (recur (if (= player-num 1) 2 1) new-boards)))))

;(-main)
