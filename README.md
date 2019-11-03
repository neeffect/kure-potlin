# Kure Potlin

Guide to pure and clean code with Kotlin.

This should be a rule book about dos and don't in kotlin code - written in the pure way.
It is not for all kotlin code and projects, only for those that go or want to go into kotlin using the  *functional programming*  approach.  

## Motto

> The more you sweat for the compiler, the less you bleed on production.

<p style='text-align: right;'> One to many </p>


# Opinions

I think that as of 1560281088: 
 - for majority of programmers it is more challenging, slower to write pure functional code  - than imperative one
 - there are however those for which functional is easier (seeing `x = x + 1` is killing many brains),
 - for short projects (one person - 4 days, two persons one day) imperative, scripting, dynamically typed approach might be more efficient
 - the longer the project the more fp and type safety helps:
      - immutability reduces risks, needs less comments and communication
      - type systems extend documentations and tests 
      - there  are more abstractions in fp than in imperative code -  more opportunities for a safe code reuse


## Immutability
 - use `val`
 - do not use `var`
 - use `Vavr.io` - with kotlin goodies
 - use `data classes` with vals and `copy` operation (copy with change)
 
 
 
 ## Functions
  - use '=' to define functions
  - do not use `return` statement
  
 
 ## Data modelling
- use sealed classes (for ADT)
- use typealiases
 