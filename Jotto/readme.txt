CS349 A4
Jotto
By Justin Hu 20465661

My program will:
- play a legal game of Jotto,  using the words in this dictionary 
- have 4 views directly attached to the model, and other views provide assistance to gameplaying
- implemented in Java using the Swing widget toolkit
- use an instance of JTable
- use nested layout managers to arrange the widgets 
- randomly choose a target word with specified level  from the list
- also allow player to set the target word of 5 letters(not responsible for checking the legitimacy of the word, tester should make sure the target word is in the dictionary)

Game Play:
- A word is chosen automatically upon starting the game
- Player can start a new game by choosing difficulty or set the target
- Player has at most 10 guesses
- Player can only guess a word that is in the dictionary and not repeating previous guesses of current game
- Each guess is listed in the History view with information of exact match and partial match
- The game ends when either the player quits, guesses the target, or runs out of guesses

Enhancements:
- Instruction panel contains a brief description of rules and how to play the game
- The Letter panel containd 26 letter buttons that can be used to keep track of possible letters
- A letter button will appear to be enabled if if has been guessed
- Player can click on one of the enabled letter button to change its color between black, green and red. (Suggested usage: black means unknow; click once to make it green indicating it is "must be in the target"; click again when green turing it to red indicating it is"can't be in the target"; click again when red to turn it back to black)
- Player can input a set of letters, and search in the dictionary all possible letters that match the given constrain.(i.e. search 'A_ _ _ Y' returns all words start with A and end with Y)
- Player can also make use of the search area to track the position of potential letters
