README
-----------------------

To start this game, type on command line

	make run

and wait for game window to pop up.

Game Play:
* Enermies(bunnies) will spawn and move toward the left end of the window and try to attack your base
* Click on bunnies with mouse to expel them from your fields. (we don't kill bunnies, as mathNEWS will object) 
* Letting a bunny across the fence will deduct 1 health point from you
* You have 10 health points represented as carrots
* If your health points reach below or equal to 0, the game ends
* You can restart the game by click anywhere within the window rather than rerun the whole program
* After expelling certain number of bunnies, the game will move to a higher level, in which bunnies will move at a faster speed as they get angry being kicked out
* Some bunnies will have special abilities such as higher speed (blue bunny, 5% spawn, 2 clicks) or extra durability (red bunny, 5% spawn, 3 clicks)

Functionality:
+ Splash screen does not respon to resize event
+ During game playing,any window size with width < 700 or height < 300 will cause game to pause until window size agjusted
+ Baseline(fence) will scale with window size
+ Carrots and bunnies will change position/spawn position relative to window size
+ Responds to exposure
+ No fetal error when closing window with 'x' button
