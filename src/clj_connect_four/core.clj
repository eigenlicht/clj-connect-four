(ns clj-connect-four.core
  (:require [clj-connect-four.board        :as board]
            [clj-connect-four.check-board  :as check]
            [clj-connect-four.ai-minimax   :as ai-strong]
            [clojure.tools.cli :only [cli] :as c])
  (:gen-class))

(defn ai-move [ai boards player-num]
  (if (= ai "strong")
    (let [x (ai-strong/make-move boards player-num 3)]
      (println (+ x 1))
      x)))

(defn connect-four [ai player]
  "Game loop. Runs until one player has connected four."
  (loop [player-num 1, boards board/empty-board]
    (board/print-board (boards 0)) ; normal output
    ;(board/print-all-boards boards) ; debug output
    (println)
    (printf "Player %d's turn: " player-num)
    (flush)
    (let [x (if (or (= player-num player) (nil? ai))
              (dec (read))
              (ai-move ai boards player-num))
          new-boards (board/insert boards x player-num)
          has-won    (check/check-board (new-boards player-num))]
      (if (not= has-won 0)
        (comp (printf "\n\n\nPlayer %d has won!\n" player-num)
              (board/print-board (new-boards 0)))
        (recur (if (= player-num 1) 2 1) new-boards)))))

(defn -main
  [& args]
  (let [args (c/cli args
                    ["-n" "--no-ai" "Play without AI." :default false]
                    ["-a" "--ai" "AI to use. [easy|moderate|strong]" :default "strong"]
                    ["-p" "--player" "Specify which player you want to be. [1|2]"
                     :default 1 :parse-fn #(Integer. %)])
        flags (args 0)
        help (args 2)]
    (println flags)
    (if (or (not (empty? (args 1)))
            (and (not (nil? (flags :ai)))
                 (nil? (some #{(flags :ai)} ["strong"])))
            (nil? (some #{(flags :player)} [1 2])))
      (println help)
      (connect-four (flags :ai) (flags :player)))))