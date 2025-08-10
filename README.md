# csc413-tankgame


| Student Information |                |
|:-------------------:|----------------|
|  Student Name       |   Justin Badilla   |
|  Student Email      |   jbadilla@sfsu.edu   |


## Purpose of jar Folder 
The jar folder will store the built jar of your term project.

`NO SOURCE CODE SHOULD BE IN THIS FOLDER. DOING SO WILL CAUSE POINTS TO BE DEDUCTED

`THIS FOLDER CAN NOT BE DELETED OR MOVED`

# Required Information when Submitting Tank Game

## Version of Java Used: Java 21.0.7

## IDE used: Visual Studio Code and IntelliJ

## Steps to Import project into IDE:
    1. Clone/download project through GitHub
    2. Open IDE
    3. Open repository (the clone from GitHub)
    4. Wait for Gradle to sync

## Steps to Build Your Project:
    1. On the right side (of IntelliJ), open the Gradle tool
    2. Go to lwjgl3, to tasks, to build to jar.
    3. Double click jar to generate and build the jar file. 
    4. New jar file will be located tankgame/lwjgl3/build/libs
 
## Steps to run your Project:
    1. In this case, the build is not needed (the jar file is in the jar folder)
    2. Open Jar file:

        Using GUI:
        1. Open repository and go to /jar and right click jar file.
        2. Click "Open With" and choose java

        Using Terminal:
        1. Go to repository location via terminal. 
        2. Head into jar folderf
            "cd jar"
        3. java -jar TankGame-1.0.0.jar

## Controls to play your Game:

|               | Player 1 | Player 2 |
|---------------|----------|----------|
|  Forward      |    W    |     UP     |
|  Backward     |    S    |    DOWN    |
|  Rotate left  |    A    |    LEFT    |
|  Rotate Right |    D    |    RIGHT   |
|  Shoot        |  SPACE  |RETURN/ENTER|

<!-- You may add more controls if you need to. -->
* To cylce through different tank colors use A and D (for player one) and LEFT and RIGHT arrows (for player two)
* To cycle through maps in map screen, use LEFT and RIGHT arrow keys.
