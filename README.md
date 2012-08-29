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

* Victor Allis master thesis: http://www.connectfour.net/Files/connect4.pdf
* Student paper with Java Code: http://www.ccs.neu.edu/home/eclip5e/classes/csu520/index.html
  - http://en.wikipedia.org/wiki/Minimax_theorem
  - http://en.wikipedia.org/wiki/Alpha-beta_pruning
* Theory of playing connect four: http://homepages.cwi.nl/~tromp/c4.html

Check field algorithm:

* http://stackoverflow.com/questions/7033165/algorithm-to-check-a-connect-four-field


Interface:
----------

Possible libraries:
* Noir: http://webnoir.org/
  - http://yogthos.net/blog/22
  - http://djhworld.github.com/2012/02/12/getting-started-with-clojurescript-and-noir.html
* Other:
  - http://www.vijaykiran.com/2012/01/11/web-application-development-with-clojure-part-1/
* Desktop application:
  - https://github.com/quil/quil
