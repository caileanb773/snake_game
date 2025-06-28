"Snake" was a game that came pre-installed on the Nokia 6110 series of cell-phones in the late 90's.
This is a clone of that game, written in Java using the Swing library.

![Screenshot 1](/src/asset/screenshot1.png)
![Screenshot 2](/src/asset/screenshot2.png)

-----

Playing:

Control the Snake using either the WASD keys or Up/Left/Down/Right arrow keys to turn the snake in the desired direction.
The snake will not stop moving unless it encounters a wall (red), or itself (white), either case resulting in the end of the game.
Score points by steering the snake into pieces of "food" (green), which will have the effect of growing the snake by one segment.
The more points you score, the longer the snake becomes, making it more challenging to avoid running into sections of the snake's tail.

-----

Autoplay:

The game comes with a very rudimentary (and greedy) pathfinding algorithm. To toggle this mode, press 'P'.
The snake will determine the direction of the food based on its x and y coordinates and change its direction accordingly.
Due to the simplicity of the current version of this algorithm, the snake will not take measures to avoid colliding with itself,
although in the future I plan to write versions of this algorithm using A* and Dijkstra's algorithm that can also be toggled.

------

Settings:

The game window size and game speed can both be changed by clicking the gear icon from the main menu.
The gameplay area is comprised of a grid of cells, each one with a default size (in pixels), along with
a fixed number of rows and columns. The amount of space the game takes up in the screen is determined by
multiplying the cell size by the number of columns/rows. Changing the game window size only changes the
cell size in pixels, which changes the number of pixels the game occupies on the screen.

Changing the game speed modifies the FPS (frames per second) of the game, which changes the calculation
that the internal timer uses. The timer runs on a delay which is determined by 1000 / FPS, the default
being 18. Higher FPS = more fluid (and faster) gameplay.