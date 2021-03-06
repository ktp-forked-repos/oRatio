# The oRatio Domain Definition Language (RDDL)

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

![Compilation Units](compilation_units.png "Compilation Units")

### Identifiers

The names of variables, constants, methods, predicates, as well as types and objects, are called *identifiers*.
A valid identifier for our domain description language is a sequence of one or more letters, digits, or underscore characters (`_`).
Spaces, punctuation marks, and symbols cannot be part of an identifier.
In addition, identifiers shall always begin either with a letter or with an underline character (`_`).

The domain description language uses a number of keywords to identify operations and data descriptions; therefore, identifiers created by a domain modeler cannot match these keywords.
The standard reserved keywords that cannot be used as identifiers are:
`bool`, `class`, `enum`, `fact`, `false`, `goal`, `new`, `or`, `predicate`, `real`, `return`, `string`, `this`, `true`, `typedef`, `void`

It is worth to notice that the domain description language is a "case sensitive" language.
This means that an identifier written in capital letters is not equivalent to another one with the same name but written in small letters.
For example, the variable names `MAX` and `max` will be considered as separate identifiers.
Here are some examples of identifiers:

```
i
MAX
max
first_name
_second_name
```

### Primitive types

The domain description language is a strongly-typed language therefore it requires every variable to be declared with its type before its first use.
The oRatio framework needs to have precise information about the type of the variable we want to define.
If we want to represent a number, for example, the framework needs to know that the declared variable represents a number and, furthermore, needs to know the specific type of the number (i.e., either an integer or a real).

The simplest way for declaring and instantiating a variable is through the syntax `<type>` `<id>` that declares a variable of type `<type>` and identifier `<id>`.
If it is needed to declare more than one variable of the same type, they can all be declared in a single statement by separating their identifiers with commas.
Once declared, the variables can be used within the rest of their scope in the program.
Unless explicitly specified, the variable will assume a default initial domain which is based on the type of the variable.
It is worth to note that, unlike an ordinary programming language, like Java or C++, rather than assuming a value, variables assume a domain of values, therefore the semantic is similar to the variables of a CSP (refere to [ac-lib](ac.md) for further details).

The domain description language provides a set of primitive types that allow us to define basic types of variables.
Primitive types for our domain description language are: `bool`, `int`, `real`, `string`, `typedef` and `enum`.

#### bool

The boolean type is the simplest type provided by the domain description language.
Booleans are used to represent boolean states (i.e., `true` and `false`).
Unless explicitly specified, a boolean variable will assume possible values within the set `{true, false}`.
This means that the allowed values of the variable is neither `true` nor `false` but it is decided by the solver according to the current constraints.
For example, in the limit case in which no constraint insists on the variable, the domain of the variable will be maintained equal to the set `{true, false}`.

#### int

The integer type is used to represent the set of integers, so to speak "without comma", positive and negative (e.g., 1, 2, 43, -89, 4324).
The internal representation format of integers (i.e., 16 bits, 32 bits, arbitrary-precision, etc.) is dependent on the implementation of the framework and is beyond the description of the language.
Unless explicitly specified, an integer variable will assume possible values within the bounds `[-inf, +inf]`.
Similar to the boolean variables, the allowed values of integer variables is decided by the solver according to the current constraints.

#### real

The real type is used to represent the set of reals, so to speak "with comma", positive and negative (e.g., 2.7, -3.14, 15.3).
Similarly to the integers, the internal representation format of reals (i.e., 16 bits, 32 bits, arbitrary-precision, etc.) is dependent on the implementation of the framework and is beyond the description of the language.
Unless explicitly specified, a real variable will assume possible values within the bounds `[-inf, +inf]`.
This means, again, that the allowed values of the variable is decided by the solver according to the current constraints.

#### string

In order to represent texts, the domain description language provides the `string` type.
Unless explicitly specified, a string variable will assume the empty string value (i.e., "").

#### typedef

The purpose of `typedef` is to assign alternative names to existing primitive types and possibly to redefine them
 This allows us, for example, to define a primitive type called `Angle` which might be a real whose allowed values are within the bounds `[0, 360]`.
In general, `typedef`s are utility constructs that allow the definition of more synthetic code.
Indeed, the same behavior can be achieved by defining primitive type variables and imposing constraint on it.

#### enum

When defining an enumerated type variable, it is assigned a set of constants called enumeration set.
The variable can assume any of the constants of the enumeration set.
Unless explicitly specified, an enum variable will assume possible values within the constants of the enumeration set.
This means that the allowed values of the variable is decided by the solver according to the current constraints, yet will contain some (or all) of the constants of the enumeration set.

### Creating primitive type instances

The following code snippet shows the deﬁnition of some primitive type variables:

```
//Primitives with (default) initial domains

int x0; // Declares an int variable x0 with initial domain [-inf +inf]
real x1; // Declares a real variable x1 with initial domain [-inf +inf]
bool x2; // Declares a bool variable x2 with initial domain {true, false}

// Enumerative custom type
enum Speed {"High", "Medium", "Low"};

// Declares a variable x3 with possible values {"High", "Medium", "Low"}
Speed x3;

// Custom type definitions
typedef int [0, 360] Angle;
Angle x4; // Declares an Angle (int) variable x7 with initial domain [0, 360]
```

Specifically, the first row defines an integer variable `x0` with initial domain `[-inf +inf]`.
Follows the definition of a real variable `x1` with initial domain `[-inf +inf]` and a boolean variable `x2` with initial domain `{true, false}`.
An enum type, called `Speed`, is defined for allowing the creation variables representing the speed as, for example, the `x3` variable, whose initial domain is the set `{High, Medium, Low}`.
Finally, a typedef called `Angle` is defined as an integer whose initial domain is within bounds `[0, 360]`.
The code snippet is closed with the definition of an `x4` variable of type `Angle`.
Since none of these variables is subject to any constraint, their domain, at the end of the execution of the code snippet, will remain untouched.

### Operators

Once variables and constants have been introduced, we can begin to operate with them by using *operators*.
We use operators to impose constraints on declared variables.
What follows is a complete list of operators.

#### Assignment operator (`=`)

The assignment operator assigns a value to a variable.
For example

```
x = 5;
y = [0, 20];
```

assigns the value `5` to the variable `x` and the domain `[0, 20]` to the variable `y`.
The assignment operation always takes place from right to left, and never the other way around.
For example

```
x = y;
```

assigns the value `y` to variable `x`.
The value of `x`, at the moment this statement is executed, is lost and replaced by `y`.

It is worth noticing that we are assigning `y` to `x` therefore, if `y` changes at a later moment, it will reflect on the value taken by `x` and the other way around.
Variables `x` and `y` will represent exactly the same object after this assignment statement is executed.

The assignment operator can be used, also, during variable declaration for assigning to variables an initial domain through the syntax `<type> <id> = <expr>`.

#### Arithmetic operators ( `+`, `-`, `*`, `/` )

Operations of addition, subtraction, multiplication and division correspond literally to their respective mathematical operators.
The semantic, however, is taken by interval arithmetic.
Specifically, arithmetic operations are defined as:

 * -[x<sub>0</sub>, x<sub>1</sub>] = [ - x<sub>1</sub>, - x<sub>0</sub>]
 * [x<sub>0</sub>, x<sub>1</sub>] + [y<sub>0</sub>, y<sub>1</sub>] = [x<sub>0</sub> + y<sub>0</sub>, x<sub>1</sub> + y<sub>1</sub>]
 * [x<sub>0</sub>, x<sub>1</sub>] - [y<sub>0</sub>, y<sub>1</sub>] = [x<sub>0</sub> - y<sub>0</sub>, x<sub>1</sub> - y<sub>1</sub>]
 * [x<sub>0</sub>, x<sub>1</sub>] * [y<sub>0</sub>, y<sub>1</sub>] = [min(x<sub>0</sub> * y<sub>0</sub>, x<sub>0</sub> * y<sub>1</sub>, x<sub>1</sub> * y<sub>0</sub>, x<sub>1</sub> * y<sub>1</sub>), max(x<sub>0</sub> * y<sub>0</sub>, x<sub>0</sub> * y<sub>1</sub>, x<sub>1</sub> * y<sub>0</sub>, x<sub>1</sub> * y<sub>1</sub>)]
 * [x<sub>0</sub>, x<sub>1</sub>] / [y<sub>0</sub>, y<sub>1</sub>] = [min(x<sub>0</sub> / y<sub>0</sub>, x<sub>0</sub> / y<sub>1</sub>, x<sub>1</sub> / y<sub>0</sub>, x<sub>1</sub> / y<sub>1</sub>), max(x<sub>0</sub> / y<sub>0</sub>, x<sub>0</sub> / y<sub>1</sub>, x<sub>1</sub> / y<sub>0</sub>, x<sub>1</sub> / y<sub>1</sub>)]

For example:

```
x = 5 + y;
```

assigns to the variable `x` the expression `5 + y`.
Suppose the domain of variable `y` is `[10, 20]`, the domain of variable `x` will be `[15, 25]` after the execution of the statement.

It is worth noticing that, similar to what happens for the simple assignment case, we are assigning the expression `5 + y` to `x` therefore, if `y` changes at a later moment, it will reflect on the value taken by `x` and the other way around.
Specifically, the variable `x` and the expression `5 + y` will represent exactly the same object after this assignment statement is executed.
As a consequence, if the domain of `y` becomes, for example, `[15, 20]`, the domain of `x` will become `[20, 25]`.
The value of `x`, at the moment this statement is executed, is lost and replaced by the expression `5 + y`.

#### Relational and comparison operators ( `==`, `!=`, `>`, `<`, `>=`, `<=` )

Two expressions can be compared using relational and equality operators, for example, to know if two values are equal or if one is greater than the other.
The result of such an operation is a boolean variable representing the validity of the relation.

Be careful!
The assignment operator (operator `=`, with one equal sign) is not the same as the equality comparison operator (operator `==`, with two equal signs); the first one (`=`) assigns the expression on the right-hand to the variable on its left, while the other (`==`) compares whether the values on both sides of the operator are equal.
Consider, for example, the following code snippet:

```
x = 5;       // assigns 5 to x
x = 7;       // assigns 7 to x
x == 7;      // compares x with 7 returning true
x == 5;      // compares x with 5 returning false
x == [6, 8]; // compares x with [6, 8] returning {true, false}
```

The first statement assigns value `5` to variable `x`.
The second statement assigns value `7` to variable `x`.
The third statement compares the variable `x` with the value `7` returning a boolean constant `true`.
The fourth statement compares the variable `x` with the value `5` returning a boolean constant `false`.
Finally, the fifth statement compares the variable `x` with the domain `[6, 8]` returning a boolean variable with domain `{true, false}`.

#### Logical operators ( `!`, `&`, `|` )

Logical operators return boolean variables representing the validity of the operator.
To begin, the operator `!` is the domain description language operator for the Boolean operation NOT.
It has only one operand, to its right, and inverts it, producing false if its operand is true, and true if its operand is false.
Basically, it returns the opposite Boolean value of evaluating its operand.
The logical operators `&` and `|` are used when evaluating two (or more) expressions to obtain a single relational result.
Specifically, the operator `&` corresponds to the Boolean logical operation AND, which yields true if both (all of) its operands are true, and false otherwise.
Finally, the operator `|` corresponds to the Boolean logical operation OR, which yields true if any of its operands is true, thus being false only when both (all of) its operands are false.

#### Assertions

An *assertion* is a statement asserting that a boolean expression must be `true`.
To assert a boolean expression it is enough to specify the boolean expression as a statement.
Notice that enforcing a boolean expression to be `true` (or, more generally, to `false`), can result in the updating of the values of the involved variables through constraint propagation.
Suppose, for example, we send the following unit to the solver:

```
int x = [0, 10];
int y = [10, 20];
bool x_eq_y = x == y;

// Assertion: x_eq_y must be equal to true
x_eq_y;
```

In the above example we are creating an integer variable `x` having domain `[0, 10]`, an integer variable `y` having domain `[10, 20]`, and a boolean variable `x_eq_y` having domain `{true, false}`.
With the execution of the assertion represented by the fourth statement, however, the value of the variable `x_eq_y` is constrained to be equal to `true`.
This, in turn, results in forcing the constraint `x == y` to be equal to `true` which, in turn, results in assigning to both variables `x` and `y` the value `10` (i.e., the only allowed value that makes the two variables equal).

What happens now if we provide an infeasible problem?
Suppose, for example, we provide the following code:

```
int x = [0, 10];
int y = [20, 30];
x == y;
```

The third statement *asserts* that the constraint `x == y` must be `true`, however the initial domains of the variables `x` and `y` do not allow the constraint to be satisfied.
When these situations occur, we say that we have an *inconsistency*.
The solver detects the inconsistency and returns `NO`.
The domains of the variables, after an inconsistency has been detected, are no more valid.

In conclusion, it is worth to notice that the combined use of operators allows to obtain quite complex behaviors.
As an example, consider the following code snippet:

```
// assert linear relations
x0 - x1 > x2 * 3;
x0 != x1;

// assert nonlinear relations
x0 == x2 * x3;

// assert conjunction of relations
x0 + x1 < 2 * x2 & x0 == x2 * x3 & x0 != x4;

// assert disjunction of relations
x0 < 10 | x0 > 100;
```

### Complex types

A *complex data type* (a.k.a. *composite data type* or *compound data type*) is any data type which can be constructed using the language's primitive data types and other complex types.
Roughly speaking, a complex type is a group of data elements grouped together under one name.
These data elements, known as members, can have different, either primitive or complex, types.

The best way for defining new types is by means of the `class` keyword.
It is, however, also possible to define complex types natively (refere [here](api.md) for more information on how to extend the solver capabilities).
Classes define data types and allow the creation of objects according to characteristics defined inside the class itself.
In addition, classes allow fields of any type as well as methods and constructors with any kind of arguments.
Classes can be declared in our domain description language using the following syntax:

```
class type_name {
  member_type1 member_name1;
  member_type2 member_name2;
  member_type3 member_name3;
  .
  .
}
```

In order to allow an initialization of the member variables of the type, classes can include a special function called its *constructor*, which is automatically called whenever a new object of the class is created.
This constructor function is declared just like a regular member function, but with a name that matches the class name and without any return type.
Furthermore, when a constructor is used to initialize other members, these other members can be initialized directly, without resorting to statements in its body.
This is done by inserting, before the constructor's body, a colon (`:`) and a list of initializations for class members.
All types have at least one constructor.
If a type does not explicitly declare any, the solver automatically provides a no-argument constructor, called the *default constructor*.

The following code, for example, defines a new data type (or class) called `Block` containing an `int` field named `id`.

```
class Block {

  int id;
  
  Block(int id) : id(id) {
  }
}

Block b0 = new Block(0);
Block b1 = new Block(1), b2 = new Block(2);
```

The declared type `Block` is then used for instantiating three objects (variables) called `b0`, `b1` and `b2`.
Note how, for creating a new instance of a complex type, the `new` operator is used.
Specifically, the `new` operator instantiates a class and, also, invokes the object constructor, returning a reference to the newly created object.
Notice that the reference returned by the `new` operator does not have, necessarily, to be assigned to a variable.
Indeed, it can also be used directly in an expression.

It is important to clearly differentiate between what is the type name (e.g., `Block`), and what is an object of this type (e.g., `b0`, `b1` and `b2`).
As can be noted by the above example, many objects (such as `b0`, `b1` and `b2`) can be declared from a single type (`Block`).

### Type inheritance

Inheritance allows us to define a class in terms of other classes.
When creating a class, instead of writing completely new fields and methods, the modeler can designate that the new class should inherit the members of existing classes.
Similarly to object oriented programming, we call the existing classes the *base* classes, while the new class is referred to as the *derived* class.
The idea of inheritance implements the *is-a* relationship.
For example, mammal IS-A animal, dog IS-A mammal hence dog IS-A animal as well and so on.
For example, through the code

```
class HeavyBlock : Block {

  real weight;
  
  HeavyBlock(int id, real weight) : Block(id), weight(weight) {
  }
}
```

we create a derived type `HeavyBlock` which *inherits* from the base type `Block`.
Since an `HeavyBlock` *is-a* `Block`, all instances of `HeavyBlock` will have a `weight` field of type `real` as well as an `id` field of type `int` which, we say, is inherited from base type `Block`.

Notice that a derived type must explicitly call a constructor of the base class from which inherits.
This explicit call, however, can be omitted in the case the base class has a default constructor.

A class may inherit from more than one class by simply specifying more base classes, separated by commas, in the list of a class's base classes (i.e., after the colon).
Unless the base classes have a default constructor, the derived class must explicitly call a constructor of each of the base classes.

### Existentially scoped variables

Existential quantification is a type of quantifier which can be interpreted as "there exists", "there is at least one", or "for some".
Specifically, the domain description language allows the modeler to retrieve a specific instance of a given type which meets certain requirements.
Creating an existentially scoped variable can be done, simply, by indicating the type of the possible instances and an identifier for representing the desired instance.
For example:

```
Block b;
```

searches for a block `b` among all the instances of type `Block`.
In other words, it creates an object variable, called `b`, whose allowed values are all the instances of type `Block`.
Notice that, since `HeavyBlock` is actually a `Block`, the allowed values for the variable `b` will include, also, all the instances of `HeavyBlock`.
It is worth to note that, in case no instances exist, the domain of the variable `b` will be empty and the solver will return `NO` (or, if possible, will backtrack).

The desired requirements are expressed by means of constraints.
Consider, for example, the following code:

```
Block b;
b.id <= 10;
```

In this case the assertion will limit the domain of `b` to all the instances of `Block` whose `id` is lower than or equal to `10`.

It is worth to note that it is possible to use comparison operators on existentially scoped variables.
For example

```
b != b1;
```

removes the instance represented by `b1` from the allowed values of the variable `b`.

## Predicates

Since the solver reasons in terms of first-order Horn clauses therefore each predicate is associated to a rule that must be complied with in order for the atom, unifying with the head of the rule, to be valid.
Consequently, the language has been designed to define both predicates and rules together.
Specifically, predicates (and their associated rules) are defined through the following syntax:

```
predicate <id> (<type> <id>, <type> <id>, ...) {
  <rule body>
}
```

in which is represented the identifier of the predicate, a typed list of arguments, and the body of the rule.
Suppose, for example, we are interested in representing the location of an agent, we might use the following rule:

```
predicate At (Location l) {
}
```

The arguments of the predicates are considered as existentially quantified variables within the body of the rule which might contain constraints on them and/or on other variables.
Specifically, the body of the rule contains a list of statement which is executed for each atomic formula that does not unify.
Suppose, for example, we want to express the fact that our agent cannot go on a location outside of the first quadrant of the Cartesian space, we might use the following rule:

```
predicate At (Location l) {
  l.x >= 0;
  l.y >= 0;
}
```

Predicates can be also defined within complex types.
Each predicate defined within a class has an implicit parameter called `scope` of the same type within which the predicate has been defined.
As an example, the following code defines a predicate `At` with an argument named `l` of type `Location` and an implicit argument named `scope` of type `Robot`.
The associated rule enforces that a robot cannot go on a location outside of the first quadrant of the Cartesian space.

```
class Robot {

  predicate At(Location l) {
    l.x >= 0;
    l.y >= 0;
  }
}
```

A similar result can be achieved by defining a predicate through

```
predicate At(Robot scope, Location l) {
  l.x >= 0;
  l.y >= 0;
}
```

Notice that the language does not allow the definition of predicates having the same identifier within the same class.

Finally, taking advantage of the similarity that a predicate (with its arguments) has a with complex type (with its members) the domain description language gives to the modeler the possibility to define inheritance among predicates, just as is the case of complex types.
Just note that the body of the base rules will be executed, in sequence, before the body of the derived rule.
The syntax for predicate inheritance is similar to the syntax used for type inheritance.

```
predicate LimitedAt() : At {
  l.x <= 10;
  l.y <= 10;
}
```

### Facts and goals

Once defined predicates and rules, we can now show how to describe facts and goals for our problems.
Still following the object-oriented paradigm, we consider facts and goals as objects and, as such, we will create them through the `new` operator.
However, since, unlike other languages, we have no means to distinguish facts and goals, we have to distinguish them explicitly through the keywords `fact` and `goal` as a prefix of the identifier.
Suppose, for example, we want to express the fact that our agent is at some location, we might use the following statement:

```
fact at_0 = new At();
```

We can now address the arguments of the fact just as if they are members of the object represented by `at_0`.
So we can, for example, put constraints on the coordinates of the locations.

```
at_0.l.x <= 10;
```

It is worth to note that creating a fact or a goal will, unless specified, create existentially scoped variables for each of its arguments.
As a result, the same rule valid for existentially scoped variables, stating that at least one instance of the type must have been previously created, applies also to the creation of facts and goals.
Executing the previous example, without previously creating instances of locations, would result in an inconsistent problem and, therefore, the solver would have returned `NO`.

A convenient method to avoid the creation of existentially scoped variables is to explicitly state the allowed values for an argument.
This can be achieved by making use of the syntax `<id>:<expr>`, where `<id>` represents the identifier of an argument and `<expr>` is an expression, within the parenthesis of the new fact or goal.
For example

```
Location l0 = new Location();
fact at_0 = new At(l:l0);
```

creates a new location `l0` and a new fact `at_0` whose parameter `l` assumes the value `l0`;

In order to better understand, let us consider a complete example:

```
// we define a Location class
class Location {

  real x;
  real y;
  
  Location(real x, real y) {
    this.x = x;
    this.y = y;
  }
}

// we define a predicate At
predicate At(Location l) {
  l.x >= 0;
  l.y >= 0;
}

// we create three instances of location
Location l0 = new Location(0, 0);
Location l1 = new Location(1, 1);
Location l2 = new Location(2, 2);

// we create an At fact
fact at_0 = new At();

// we add some constraints on the fact
at_0.l.x <= 1;
at_0.l != l0;

// we create an At goal specifying its l parameter
goal at_1 = new At(l:l2);
```

It is worth to note that both facts and goals can be created within the body of a rule.
This allows us to define subgoals for the reasoning process.
Suppose, for example, we want to express the fact that "All men are mortal", we might use the following rule:

```
predicate Mortal (Thing x) {
  goal m = new Man(x:x);
}
```

The creation of scoped formulas (i.e., formulas with a `scope` parameter), either facts or goals, follows a similar syntax.
However it is required that the scope is explicitly specified.
This can be achieved through the following syntax:

```
[fact | goal] <id> = new <qualified_id>.<id>(<id>:<expr>, <id>:<expr>, ...);
```

where `<qualified_id>` identifies the scope.
The following code, for example, creates two robots and two facts having a different scope.

```
Robot r0 = new Robot();
Robot r1 = new Robot();

fact f0 = new r0.At(l:l0);
fact f1 = new r1.At(l:l1);
```

Note that the `scope` argument of the fact `f0` is constituted by an object variable whose domain contains the sole robot `r0`.
Similarly, the `scope` argument of the fact `f1` is constituted by an object variable whose domain contains the sole robot `r1`.

It is worth to notice that nothing prevents to create facts and goals having existentially scoped variables as `scope` argument.
As an example

```
Robot r0 = new Robot();
Robot r1 = new Robot();

// we create the existential variable
Robot r;

goal g = new r.At(l:l0);
```

creates a goal `g` whose scope has, as allowed values, the two robots `r0` and `r1` (i.e., its allowed values is constituted by the set `{r0, r1}`).
This syntax allows the possibility to leave to the solver the responsibility to decide which robot is at location `l0`.

## The Extended Backus-Naur form

This section presents the complete grammar of the language in its Extended Backus-Naur form.

```
<compilation_unit>          ::= (<type_declaration> | <method_declaration> | <predicate_declaration> | <statement>)*

<type_declaration>          ::= <typedef_declaration>
                              | <enum_declaration>
                              | <class_declaration>

<typedef_declaration>       ::= 'typedef' <primitive_type> <expr> <ID> ';'

<enum_declaration>          ::= 'enum' <ID> <enum_constants> ('|' <enum_constants>)* ';'

<enum_constants>            ::= '{' StringLiteral (',' StringLiteral)* '}'
                              | <type>

<class_declaration>         ::= 'class' <ID> (':' <type_list>)? '{' <member>* '}';

<member>                    ::= <field_declaration>
                              | <method_declaration>
                              | <constructor_declaration>
                              | <predicate_declaration>
                              | <type_declaration>

<field_declaration>         ::= <type> <variable_dec> (',' <variable_dec>)* ';'

<variable_dec>              ::= <ID> ('=' <expr>)?

<method_declaration>        ::= 'void' <ID> '(' <typed_list>? ')' '{' <block> '}'
                              | <type> <ID> '(' <typed_list>? ')' '{' <block> '}'

<constructor_declaration>   ::= <ID> '(' <typed_list>? ')' (':' <initializer_element> (',' <initializer_element>)*)? '{' <block> '}'

<initializer_element>       ::= <ID> '(' <expr_list>? ')'

<predicate_declaration>     ::= 'predicate' <ID> '(' <typed_list>? ')' (':' <type_list>)? '{' <block> '}'

<statement>                 ::= <assignment_statement>
                              | <local_variable_statement>
                              | <expression_statement>
                              | <disjunction_statement>
                              | <formula_statement>
                              | <return_statement>
                              | '{' block '}'

<block>                     ::= <statement>*

<assignment_statement>      ::= (<qualified_id> '.')? <ID> '=' <expr> ';'

<local_variable_statement>  ::= <type> <variable_dec> (',' <variable_dec>)* ';'

<expression_statement>      ::= <expr> ';'

<disjunction_statement>     ::= <conjunction> ('or' <conjunction>)+

<conjunction>               ::= '{' <block> '}' ('[' <expr> ']')?

<formula_statement>         ::= ('goal' | 'fact') <ID> '=' 'new' (<qualified_id> '.')? <ID> '(' <assignment_list>? ')' ';'

<return_statement>          ::= 'return' <expr> ';'

<assignment_list>           ::= <assignment> (',' <assignment>)*

<assignment>                ::= <ID> ':' <expr>

<expr>                      ::= <literal>
                              | '(' <expr> ')'
                              | <expr> ('*' <expr>)+
                              | <expr> '/' <expr>
                              | <expr> ('+' <expr>)+
                              | <expr> ('-' <expr>)+
                              | '+' <expr>
                              | '-' <expr>
                              | '!' <expr>
                              | qualified_id
                              | (<qualified_id> '.')? <ID> '(' <expr_list?> ')'
                              | '(' <type> ')' <expr>
                              | '[' <expr> ',' <expr> ']'
                              | 'new' <type> '(' <expr_list>? ')'
                              | <expr> '==' <expr>
                              | <expr> '>=' <expr>
                              | <expr> '<=' <expr>
                              | <expr> '>' <expr>
                              | <expr> '<' <expr>
                              | <expr> '!=' <expr>
                              | <expr> '->' <expr>
                              | <expr> ('|' <expr>)+
                              | <expr> ('&' <expr>)+
                              | <expr> ('^' <expr>)+

<expr_list>                 ::= <expr> (',' <expr>)*

<literal>                   ::= <NumericLiteral>
                              | <StringLiteral>
                              | 'true'
                              | 'false'

<qualified_id>              ::= ('this' | <ID>) ('.' <ID>)*;

<type>                      ::= <class_type>
                              | <primitive_type>

<class_type>                ::= <ID> ('.' <ID>)*

<primitive_type>            ::= 'real'
                              | 'bool'
                              | 'string'

<type_list>                 ::= <type> (',' <type>)*

<typed_list>                ::= <type> <ID> (',' <type> <ID>)*

<ID>                        ::= ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*

<NumericLiteral>            ::= [0-9]+ ('.' [0-9]+)?
                              | '.' [0-9]+

<StringLiteral>             ::= '"' (ESC|.)*? '"'

<ESC>                       ::= '\\"' | '\\\\'
```