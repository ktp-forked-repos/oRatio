# oRatio

[![Build Travis-CI Status](https://travis-ci.org/oRatioSolver/oRatio.svg?branch=master)](https://travis-ci.org/oRatioSolver/oRatio)

> Dum loquimur, fugerit invida aetas: carpe diem, quam minimum credula postero. (Orazio, Odi, I, 11, 7-8)

oRatio is an Integrated Logic and Constraint based solver which takes inspiration from both Logic Programming (LP) and Constraint Programming (CP).

## Building oRatio

The repository structure is compatible with the [NetBeans](https://netbeans.org/) editor folder structure, therefore, the easiest way for building oRatio is to clone the repository, open it with NetBeans and build the project. Nontheless, oRatio can be built regardless if NetBeans by using [Apache Ant](http://ant.apache.org/) simply going within the project folder and starting an `ant` building script.

## ac-lib

The constraint network is maintained by means of the [ac-lib](https://github.com/oRatioSolver/oRatio/blob/master/src/it/cnr/istc/ac/README) library which provides features for creating variables and for enforcing constraints among them.
