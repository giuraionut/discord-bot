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
On the lowest levels there are operations that must be solved first, because to solve the tree we start from bottom and work our way up.
* We check if a node is a number or operation. 
* If it is operation we check if all his childs are numbers. 
* If all his childs are numbers we solve the node and replace it with the result. 
* If one child is an operation we go deeper and check again.
