# Kure Potlin

Guide to pure and clean code with Kotlin.

This should be a rule book about dos and don't in kotlin code - written in the pure way.
It is not for all kotlin code and projects, only for those that go or want to go into kotlin using the  *functional programming*  approach.  

## Motto

> The more you sweat for the compiler, the less you bleed on production.

<p style='text-align: right;'> One to many </p>


# Opinions

I think that as of 1560281088: 
 - Opinion: for majority of programmers it is more challenging, slower to write pure functional code  - than imperative one
 - FACT:  there are however those for which functional is easier (seeing `x = x + 1` is killing many brains),
 - Opinion: for short projects (one person - 4 days, two persons one day) imperative, scripting, approach might be more efficient
 - Opinion: the longer the project the more fp and typesafety helps:
      - immutability reduces risks, needs less comments and communication
      - type systems extends docs and tests 
      - the ar emore abstraqctions in fp than in imperative code -  more opportunities for a safe code reuse
      



## Immutability
 - use `val`
 - do not use `var`
 - use `Vavr.io` - with kotlin goodies
 - use `data classes` with vals and `copy` for a `mutation` (copy with change)
 
 
 ## Functions
  - use '=' to define functions
  - do not use `return` statement
  
  