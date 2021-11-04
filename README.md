# Discord Bot
Discord bot made with [JDA](https://github.com/DV8FromTheWorld/JDA), Spring and MongoDB database.


## Features
---
### General

* `Persists` all the guilds that he joins ( all members from each guild too ) into a **MongoDB** database.

* Help command is `organized per categories`.
![help_command image](https://github.com/giuraionut/discord-bot/blob/main/github_resources/help_command.png)
---
### Math
* Calculates `Fibonacci sequence` for given N
* Calculates `GCD` and `LCM` for multiple values
* Finds the `longest substring` from a given string
* Can parse and solve `simple equations`

### Arithmetic expression parser
* Can parse and solve simple expressions basic operations: `addition, subtraction, division, multiplication, sqrt, power`.
* The expression is parsed into a tree structure:
```java
12*3+15/3-sqrt9+3^2
+
├── *
│   ├── 12
│   └── 3
├── -
│   ├── /
│   │   ├── 15
│   │   └── 3
│   └── sqrt
│       └── 9
└── ^
    ├── 3
    └── 2

Sol:
47.0
```
On the `lowest levels` there are operations that `must be solved first`, because to solve the tree we start from bottom and work our way up.
* We check if a node is a number or operation. 
* If it is operation we check if all his childs are numbers. 
* If all his childs are numbers we solve the node and replace it with the result. 
* If one child is an operation we go deeper and check again.
---
### Music Bot
The bot can play music from YouTube. To achieve this feature, we use [lavaplayer](https://github.com/sedmelluq/lavaplayer) which can basically do everything for us.

We can provide an **URL** and `lavaplayer` will automatically fetch all the data needed from `youtube` to play the track.
We can also provide a **string** and `lavaplayer` look it up and return a result that matches our **string**.

You can find more information about `lavaplayer` [here](https://github.com/sedmelluq/lavaplayer).

Currently, the music player supports the basic operations such as **play, pause, stop, skip** etc.

A few features:
* Will ony play music if you are in the same `voice channel` as the bot.
* If use the `play` command while the bot plays another track, it will add your request to a `queue` and will play them in order.
* Very detailed feedback such as **time left, song duration, song name, channel name**, etc.
