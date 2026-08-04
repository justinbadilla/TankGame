<img width="800" height="450" alt="TankGIF" src="https://github.com/user-attachments/assets/6b766655-f699-4bf3-9504-7ca3a5a58109" />



# Tank Game

A two-player local multiplayer tank battle game built in Java using LibGDX and Tiled.

## Overview

Tank Game is a local multiplayer arena shooter where two players battle across destructible environments and multiple maps.

Players select their tanks, choose a map, and fight until one player runs out of lives.

## Features

- Two-player local multiplayer gameplay
- Tank selection menu
- Interactive map selection screen
- Dynamic camera system
- Breakable walls
- Health and lives system
- Respawn system
- Animated menu screens
- Screen transitions and HUD overlays
- Multiple maps created with Tiled

## Controls

### Player 1

- Move: W A S D
- Shoot: SPACE

### Player 2

- Move: Arrow Keys
- Shoot: ENTER

## Technologies

- Java
- LibGDX
- Tiled Map Editor
- Gradle
- Object-Oriented Programming

## Technical Highlights
This project was built with an emphasis on reusable object-oriented design and separation of game systems.

### Dynamic Camera System

Implemented a camera that tracks both players simultaneously and adjusts zoom level based on player distance.

### Collision System

Built a collision framework using reusable GameObject classes and rectangular hitboxes.

### Respawn System

Designed a spawn manager that selects safe spawn locations and maximizes distance from opponents.

### Asset Management

Centralized shared textures, sounds, and maps within the game lifecycle to reduce memory usage and improve maintainability.

## Project Structure

com.sfsu.tankgame
│
├── Main
├── Maps
├── ControlScheme
│
├── Screens
│   ├── MenuScreen
│   ├── TankScreen
│   ├── MapScreen
│   ├── GameScreen
│   ├── EndScreen
│   └── ScreenFade
│
├── gameobjects
│   ├── GameObject
│   ├── Tank
│   ├── Bullet
│   ├── Wall
│   └── BreakableWall
│
├── Systems
│   └── Respawn
│
└── HUD
    ├── HealthBar
    └── HowToPlayOverlay

## Screenshots


## Running the Game

### Option 1: Download the Latest Release
Download `TankGame-1.0.jar` from the releases section.

Run:
java -jar TankGame-1.0.jar


### Option 2: Run from Source
Clone the repository:

git clone https://github.com/justinbadilla/TankGame.git
cd TankGame

Run the desktop application:

./gradlew lwjgl3:run

### Requirements 
- Java 17+
- Gradle (optional, Gradle Wrapper included)

## Challenges and Lessons Learned

During development of Tank Game, I gained hands-on experience with game architecture, rendering systems, and asset management using LibGDX.

Key areas of growth included:

- Building a complete game using the LibGDX framework
- Designing reusable object-oriented systems for tanks, bullets, walls, and game screens
- Implementing collision detection and game object interactions
- Developing a dynamic camera system that follows and zooms based on player positions
- Managing assets such as textures, sounds, and maps through centralized ownership and disposal practices
- Integrating maps created with the Tiled Map Editor into gameplay
- Creating menu systems, screen transitions, and HUD components
- Debugging rendering, OpenGL, and screen lifecycle issues
- Organizing a multi-class Java project using clean separation of responsibilities

This project strengthened my understanding of game development, software architecture, and maintaining a larger codebase over time.

## Future Improvements

- AI-controlled opponents
- Overheating Tank
- Online multiplayer
- Additional maps
- Powerups
- Particle effects
- Sound settings menu

## Portfolio Goals

This project was developed to strengthen my understanding of game development, object-oriented design, asset management, and software architecture using Java and LibGDX.
