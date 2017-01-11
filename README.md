# oRatio

[![Build Travis-CI Status](https://travis-ci.org/oRatioSolver/oRatio.svg?branch=master)](https://travis-ci.org/oRatioSolver/oRatio)

> Dum loquimur, fugerit invida aetas: carpe diem, quam minimum credula postero. (Orazio, Odi, I, 11, 7-8)

oRatio is an Integrated Logic and Constraint based solver which takes inspiration from both Logic Programming (LP) and Constraint Programming (CP).

## Building oRatio

The repository structure is compatible with the [NetBeans](https://netbeans.org/) editor folder structure, therefore, the easiest way for building oRatio is to clone the repository, open it with NetBeans and build the project.
Nonetheless, oRatio can be built regardless if NetBeans by using [Apache Ant](http://ant.apache.org/) simply going within the project folder and starting an `ant` building script.
Precompiled builds will be available soon.

### Building Requirements

The unique requirement for building the oRatio framework is to have the [Java SE (JDK)](http://www.oracle.com/technetwork/java/javase/downloads/index.html) version 8 or greater.

## System Usage

The oRatio environment can be used either as a command line tool or as a library.

The default usage syntax is the following:

```
java -jar oRatio.jar [options] <input-files>
Options:
-show-planning-graph    Shows the generated planning graph
```

where `<input-files>` is a list of input files containing [RDDL](https://oratiosolver.github.io/oRatio/language) code snippets.

Using oRatio as a library is pretty simple. It is enough to create a `Solver` instance and interact with it.

For example, the following code creates a solver instance, reads an RDDL script which requires the creation of a new `real` variable called `a` and retrieves the newly created variable through the `Solver`.
At a later time, the solver reads the `/examples/test/test_sv_0.rddl` file from the file system and finds a solution.

```java
Solver s = new Solver();

s.read("real a;");

IArithItem a = s.get("a");

s.read(new File("/examples/test/test_sv_0.rddl"));

boolean solution = s.solve();

if(solution) {
    System.out.println("We have found a solution!!");
} else {
    System.out.println("The problem is unsolvable..");
}
```

More information about using oRatio as a library can be found [here](api.md).

## The oRatio Domain Description Language (RDDL)

The oRatio input language is called oRatio Domain Description Language (RDDL).
The basic core of the oRatio architecture provides an object-oriented virtual environment for the definition of objects and constraints among them.
Every object in the oRatio environment is an instance of a specific *type*.
oRatio distinguishes among *primitive types* (i.e., bools, ints, reals, enums and strings) and user defined *complex types* (e.g., robots, trucks, locations, etc.).
More information about the RDDL can be found [here](https://oratiosolver.github.io/oRatio/language).

## ac-lib

The constraint network is maintained by means of the [ac-lib](https://github.com/oRatioSolver/oRatio/blob/master/src/it/cnr/istc/ac/README.md) library which provides features for creating variables and for enforcing constraints among them.
