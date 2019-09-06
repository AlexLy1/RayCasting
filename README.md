# RayCasting

This repository contains a small program written in Java which builds up a basic 3D game world by using Ray-Casting algorithm. 
<br />
In this 3D game world, user's position and user's view are both stored using double values, so that user can move around and rotate the view in horizontal direction fluently in this 3D game world. 
<br />
<br />
The critical concept of Ray-Casting is that it uses 4 double vectors, x and y direction vectors, x and y plane vectors to calculate what pixels on the screen representing the right wall and left wall, then what pixels on the screen representing
the ceiling and the floor. After this, the program is able to convert wall images pixel by pixel into the corresponding group 
of pixels on the screen representing walls. <br />
Renderring floor and ceiling can use the same idea, but in this case, i just used backgroud color with different darkness to represent the floor and the ceiling.<br />

### The looks of the 3D game: <br />
![Game Look](https://github.com/AlexLy1/RayCasting/blob/master/gameLookPics/gameLook.png) <br />
![Game Look 2](https://github.com/AlexLy1/RayCasting/blob/master/gameLookPics/GameLook2.png)<br />
