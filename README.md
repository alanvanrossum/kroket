# TI2806 - Contextproject
## Computer Games context
### Team Kroket

Welcome to the main Github repository of Team Kroket. Team Kroket is a group of five Computer Science majors at Delft University of Technology, The Netherlands. 
This is the our main repository for Contextproject 2016. Our goal was to create a small and fun multiplayer videogame, for the Oculus Rift and several other multimedia devices. In order to play this game, you need one Oculus Rift headset and two Android powered devices. Android 4 and later are supported. The machine operating the Oculus Rift headset should be running Microsoft Windows. Oculus Runtime SDK 0.8.0.0 and SteamVR are required. 
No other platforms are currently supported.

## Introduction

### W...what am I looking at?

Escaparade! A videogame by Team Kroket!

When we were signing up for this project, we had to pick a name for our team. We picked a random food, a kroket. We didn't think the name Team Kroket would matter much and figured it would be temporary, but the name showed up everywhere in official documentation and we decided to stick by it.

The title of our game is "Escaparade". The name is a contraction between the words "Escape" and "Parade". Initially, this was a temporary title. Again, we deciced to stick by it.

This repository contains the VR-side of the game (to be used with the Oculus Rift). The game engine used is the jMonkeyEngine platform (Java powered), as per course-requirement. 

Our Android client and dedicated (standalone) gamehost are located on different repositories for Continous Integration purposes. Please follow the links below:
- [Android client](https://github.com/alanvanrossum/kroketapp/) 
- [Gamehost/server](https://github.com/alanvanrossum/krokethost/)


We think our silly game will be a fun experience for everyone. We hope you enjoy playing Escaparade!


[![Kroket game studios](http://i.imgur.com/hQs8FMT.png)](https://github.com/alanvanrossum/kroket)

| Component        | Build status  | 
| ----------------- |:-------------:|
| [Virtual Reality client](https://github.com/alanvanrossum/kroket/) 	| [![Build Status](https://api.travis-ci.org/alanvanrossum/kroket.svg?branch=master)](https://travis-ci.org/alanvanrossum/kroket)|
| [Android client](https://github.com/alanvanrossum/kroketapp/)    | [![Build Status](https://api.travis-ci.org/alanvanrossum/kroketapp.svg?branch=master)](https://travis-ci.org/alanvanrossum/kroketapp)|
| [Gamehost/server](https://github.com/alanvanrossum/krokethost/)  | [![Build Status](https://api.travis-ci.org/alanvanrossum/krokethost.svg?branch=master)](https://travis-ci.org/alanvanrossum/krokethost)|

### How to play

You'll need:
- 1 Oculus Rift connected to a computer
- 2 friends
- 2 Android Smartphones with the EscapeApp installed
- To get ready to laugh

#### Step 1: Installing the server
Install and run the EscapeHost application. The EscapeHost will listen (by defaulft) on port TCP/1234. If you are behind a NAT device, please configure it to forward incoming connections.
The server (EscapeHost) will automatically deal with all clients connecting and start the game when everyone is ready.

#### Step 2: Installing the VR client
Connect your Oculus Rift to a computer and install EscapeVR on this machine. This can be the same computer you're running the EscapeHost on.

#### Step 3: Installing the Mobile client
Install EscapeApp to at least two Android mobile devices. 

#### Step 4: Connect the clients to the server
Run EscapeVR and make sure it connects to the EscapeHost. 
On both Android devices, run the EscapeApp and make sure both are connected to the EscapeHost.

#### Step 5: Start the game
The EscapeHost will start the game when everyone is connected and ready to play.

Enjoy your game!

# Disclaimer

Material used for educational purposes only. No copyright infringement intended.


# Documentation

## Development documents

### Week 1

- Top 3 game concepts 
- Sprint Backlog 1  

[Week 1 documents](https://github.com/alanvanrossum/kroket/tree/master/doc/deliverablesweek1)

### Week 2

- Product Vision and Planning
- Sprint Retrospective 1 
- Architecture design 
- Sprint Backlog 2  

[Week 2 documents](https://github.com/alanvanrossum/kroket/tree/master/doc/deliverablesweek2)

### Week 3

- Architecture design
- Final Product Planning
- Final Product Vision
- Sprint backlog 3
- Sprint retrospective 2
- Design document

[Week 3 documents](https://github.com/alanvanrossum/kroket/tree/master/doc/deliverablesweek3)

### Week 4

- Architecture design
- Sprint backlog 4
- Sprint retrospective 3

[Week 4 documents](https://github.com/alanvanrossum/kroket/tree/master/doc/deliverablesweek4)

### Week 5

- Architecture design
- Sprint backlog 5
- Sprint retrospective 4

[Week 5 documents](https://github.com/alanvanrossum/kroket/tree/master/doc/deliverablesweek5)

### Week 6

- Architecture design
- Sprint backlog 6
- Sprint retrospective 5

[Week 6 documents](https://github.com/alanvanrossum/kroket/tree/master/doc/deliverablesweek6)

### Week 7

- Test Document 
- Architecture design
- Sprint backlog 7
- Sprint retrospective 6

[Week 7 documents](https://github.com/alanvanrossum/kroket/tree/master/doc/deliverablesweek7)

### Week 8

- Test Document 
- Architecture design
- Sprint backlog 8
- Sprint retrospective 7

[Week 8 documents](https://github.com/alanvanrossum/kroket/tree/master/doc/deliverablesweek8)

### Week 9

- Test Document 
- Final Report (Draft)
- Architecture design (Final)
- Sprint retrospective 8

[Week 9 documents](https://github.com/alanvanrossum/kroket/tree/master/doc/deliverablesweek9)


