# Hexaboard

A simple library to make no flicker scoreboards.

## Features
- Async animated scoreboards
- Easy and simple boards to handle them as you want
- Board limitations (line and title length) determined by player protocol (Using ClientStats)
## Tested versions:
- 1.8
- 1.17
## Limitations
### 1.8 - 1.12
- Title length: 32 characters
- Line length: 40 characters (Using scores)
- Line length: 30-32 characters (Using teams)
### 1.13 - 1.17
- Title length: 128 characters
- Line length: 40 characters (Using scores)
- Line length: 126-128 characters (Using teams)
### 1.18 +
- Title length: 128 characters
- Line length: 32767 characters (Using scores, not tested yet)
- Line length: 126-128 characters (Using teams)
## Minimal Requirements
- Java 8 or higher
- ClientStats (To get the player protocols, optional)
## Examples 
See the examples module 
