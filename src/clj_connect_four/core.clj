(ns clj-connect-four.core
  (:require [clj-connect-four.board        :as board]
            [clj-connect-four.check-board  :as check]
            [clj-connect-four.ai-minimax   :as ai]
            [clojure.tools.cli :only [cli] :as c])
  (:gen-class))

(defn ai-move [ai boards player-num]
  (let [steps (case ai
                "uber"        5
                "very-strong" 4
                "strong"      3
                "moderate"    2
                "easy"        1
                "very-easy"   0)
        x (ai/make-move boards player-num steps)]
      (println (+ x 1))
      x))

(defn connect-four [players]
  "Game loop. Runs until one player has connected four."
  (loop [player-num 1, boards board/empty-boards]
    (board/print-board boards) ; normal output
    (println)
    (printf "Player %d's turn: " player-num)
    (flush)
    (let [x (if (= (players (dec player-num)) "human")
              (dec (read))
              (ai-move (players (dec player-num)) boards player-num))
          new-boards (board/insert boards x player-num)
          has-won    (check/check-board (new-boards player-num))]
      (if (not= has-won 0)
        (comp (printf "\n\n\nPlayer %d has won!\n" player-num)
              (board/print-board new-boards))
        (if (board/board-full? new-boards)
          (comp (printf "\n\n\nGame is a draw!\n")
                (board/print-board new-boards))
          (recur (if (= player-num 1) 2 1) new-boards))))))

(defn is-valid-player [p]
  (some #{p} ["human" "very-easy" "easy" "moderate"
              "strong" "very-strong" "uber"]))

(defn -main
  [& args]
  (let [args (c/cli args
                    ["-1" "--player-1" "Player 1: [<AI-type>|human]"
                     :default "human"]
                    ["-2" "--player-2" "Player 2: [<AI-type>|human]"
                     :default "strong"]
                    ["-h" "--help" "Show this message."])
        flags (args 0)
        help (apply str (args 2)
                    "\n\nPossible AI types: [very-easy|easy|moderate|strong|very-strong|uber]\n")]
    (if (and (not (contains? flags :help))
             (empty? (args 1))
             (is-valid-player (flags :player-1))
             (is-valid-player (flags :player-2)))
      (connect-four [(flags :player-1) (flags :player-2)])
      (println help))))