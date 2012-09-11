clj-connect-four
================

Connect Four AI written in Clojure.

Ideas
------

* Possible games are: human vs. human, AI vs. human, human vs. AI and AI vs. AI.
* During a game, one or both players can switch from human to AI and vica versa
* AI modes:
  - Easy: Tries to keep other player off from connecting four, otherwhise random
  - Moderate: tries to connect 4, block 4, connect 3, block 3, ...
  - Ultimate: Minimax algorithm (calculates next possible steps)
  

Useful links about conncet four AI programming:

* [Victor Allis master thesis](http://www.connectfour.net/Files/connect4.pdf)
* [Student paper with Java Code](http://www.ccs.neu.edu/home/eclip5e/classes/csu520/index.html)
  - http://en.wikipedia.org/wiki/Minimax_theorem
  - http://en.wikipedia.org/wiki/Alpha-beta_pruning
* Theory of playing connect four:
  - http://homepages.cwi.nl/~tromp/c4.html
  - http://homepages.cwi.nl/~tromp/c4/c4.html
  - http://en.wikibooks.org/wiki/Connect_four
* [Lecture on Problem Solving and Search](http://www.dbai.tuwien.ac.at/staff/musliu/ProblemSolvingAI/)

Check field algorithm:

* http://stackoverflow.com/questions/7033165/algorithm-to-check-a-connect-four-field


Interface
---------

Possible libraries:
* Noir: http://webnoir.org/
  - http://yogthos.net/blog/22
  - http://djhworld.github.com/2012/02/12/getting-started-with-clojurescript-and-noir.html
* Other:
  - http://www.vijaykiran.com/2012/01/11/web-application-development-with-clojure-part-1/
* Desktop application:
  - https://github.com/quil/quil

Checking algorithm
------------------
Another crucial part beside the AI is the algorithm used for checking if a player has won.
Currently I'm having three ideas in mind:

1.  Calculate all possible winning combinations and pull out those which are relevant for current turn

    [Draft of implementation](https://gist.github.com/3520562).

2.  [Bit-board solution](http://stackoverflow.com/questions/4261332/optimization-chance-for-following-bit-operations) (not sure whether that's a good idea in Clojure)

    [Draft of implementation](https://gist.github.com/3639220).

3.  Something with [core.logic](https://github.com/clojure/core.logic) (brings Prolog-like solution solving to Clojure)

    [Draft of implementation](https://gist.github.com/3702469).
