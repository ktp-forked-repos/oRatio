# The oRatio Domain Definition Language

This section describes the domain description language used, within the oRatio framework, to represent physical domains.
Compared to previous versions of the language (e.g., [Cesta and Oddi, 1996a](https://www.researchgate.net/publication/228818262_DDL_1_A_formal_description_of_a_constraint_representation_language_for_physical_domains)), the current proposal introduces a pure object-oriented approach to the definition of domain models and problem definitions.
It has been decided to move to an object-oriented approach mostly to allow an higher decomposition of the domain model, resulting in an increase of modularity and a consequent reduction of the the overall complexity at design phase.
Furthermore, thanks to the object-oriented approach, UML modeling features can be naturally exploited to enhance the design phase.
In addition, aspects related to first order logic are further made explicit, allowing a uniform representation of planning and scheduling concepts.
Finally, although the language is based on a multi-sorted first order logic core, from which the object-oriented approach comes, it has been designed for allowing extensibility.
As already mentioned, the scope concept represents the junction element between the logic based core and specialized resolution algorithms.

## An Object-Oriented Language

The basic core of the oRatio architecture provides an object-oriented virtual environment for the definition of objects and constraints among them.
Every object in the oRatio environment is an instance of a specific *type*.
oRatio distinguishes among *primitive types* (i.e., bools, ints, reals, enums and strings) and user defined *complex types* (e.g., robots, trucks, locations, etc.).
Within oRatio, a problem is composed of different *compilation units* (i.e., several files) that can possibly interact each other.
Each compilation unit can contain several declarations of different types and/or statements.
Such units are given to the solver in different sorted *groups* (i.e., a list of lists of files).

Furthermore, these groups can be sent to solver at different times so as to provide plan adaptation features.
Although not strictly required, it is common practice to separate the type declarations from the statements in different units (e.g., a first unit for type declarations and a second unit for statements).
Furthermore, type declarations can be spread on different units so as to improve model decomposition.
In this regard, we tried to facilitate the definition of the domains by internally implementing forward declaration.
Specifically, types, methods and predicates can be used before of being declared, under the obvious assumption that these types (methods and predicates) are defined sooner or later within the same group of units within which the type (method or predicate) is used (or, alternatively, in a group previously sent to the solver).
This same argument does not apply to statements which, on the contrary, are executed sequentially.
Hence it is not possible, for example, to use a variable which has not yet been declared.

To sum up, the suggested methodology, as summarized in the following image, consists in providing to the solver a set of compilation units containing the definition of types, methods and predicates, so as to inform the solver of the domain model on which it will have to reason.
At a later time, a new compilation unit is provided to the solver containing the statements relative to the declaration of the instances, the facts and the goals.
At this point, if a solution to the proposed problem exists, the solver will be able to find it and will return `true`, if not, it and will return `false`.
Finally, if needed, the solution can be adapted, several times, by providing further compilation units.

![alt text](compilation_units.png "Compilation Units")