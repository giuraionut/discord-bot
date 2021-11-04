# Discord Bot
Discord bot made with JDA and Spring and MongoDB database.


## Features
### General
* Persists all the guilds that he joins ( all members from each guild too )

### Math
* Calculates Fibonacci sequence for given N
* Calculates GCD and LCM for multiple values
* Finds the longest substring from a given string
* Can parse and solve simple equations

### Arithmetic expression parser
* Can parse and solve simple expressions with the following operations:
    * addition
    * subtraction
    * division
    * multiplication
    * square root
    * power
* The expression is parsed into a tree structure:
```
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
