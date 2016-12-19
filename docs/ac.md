# ac-lib

This library provides features for maintaining an arc-consistency based constraint network.

### Creating a constraint network

Creating a constraint network is overly simple. I it sufficient to create an instance of the `Network` class.

```java
Network n = new Network();
```

### Creating new variables and constants

Variables can be created by invoking mothods of the `Network` instance. It is possible to create boolean variables, real variables and enumerative variables. Similarly, it is possible to create boolean constants, real constants and enumerative constants.

```java
Network n = new Network();

// a boolean variable
BoolVar bv = n.newBool();

// a boolean constant assuming value true
BoolConst bc = n.newBool(true);

// a real variable
ArithVar av = n.newReal();

// a real constant assuming value 0
ArithConst ac = n.newReal(0);
```
