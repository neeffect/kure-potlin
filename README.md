# kure-potlin

This is a small plugin to [detekt](https://github.com/detekt/detekt) tool.
The purpose is to report the potential use of impure language elements in
kotlin code.

## Detected rules

Rule | Detects | Enabled <br /> by default | Requires <br /> [type resolution](https://detekt.github.io/detekt/type-resolution.html)
--- | --- | --- | ---
LoopUsage | use of `for`, `while` | :white_check_mark: |
ReturnStatement | use of `return` statement | :white_check_mark: |
VariableUsage | use of `var` | :white_check_mark: |
ReturnUnit | use of function returning `Unit` type | :white_check_mark: | :ballot_box_with_check:
ClassDefinition | use of object-oriented `class` | |
AbstractClassDefinition | use of object-oriented `abstract class` | :white_check_mark: |
ThrowExpression | use of `throw` | :white_check_mark: |
MutableCollections | use of mutable collections | :white_check_mark: | :ballot_box_with_check:

## Usage

a) Use detect plugin in gradle build

```kotlin
plugins {
    id("io.gitlab.arturbosch.detekt").version("1.16.0")
}
```

b) add dependency

```kotlin
dependencies {
  detektPlugins("pl.setblack:kure-potlin:0.3.0")
}
```

c) optionally, you can reconfigure plugin by adding this to your custom `detekt.yaml`:
```yaml
impure:
  active: true
  LoopUsage:
    active: true
  ReturnStatement:
    active: true
  VariableUsage:
    active: true
  ReturnUnit:
    active: true
  ClassDefinition:
    active: false
  AbstractClassDefinition:
    active: false
  ThrowExpression:
    active: true
  MutableCollections:
    active: true
```

d) run `gradle detekt` or `gradle detektMain` to use rules which require [type resolution](https://detekt.github.io/detekt/type-resolution.html)

## License

Apache 2.0

[text](license)

## Examples

```kotlin
fun impure(y: Int): Int {
    var x = 1
    x = x + y
    return x
}
```

Function above will be reported as impure (uses `var` and `return`).

It can be rewritten to a pure version.

```kotlin
fun pure(y: Int): Int = y + 1 
```

## Suppressing

If you need to ignore/suppress issues reported by this plugin.
Just annotate function or class with:
`@Suppress("RULE")`
where RULE is just a [supported rule name](#Rules).

(This is a standard `detekt` feature).


##  Disclaimer

This plugin will not enforce 100% pure functional programing.
It only checks for some keywords. If you use some java libraries 
it will be easily possible to cheat. For instance: `AtomicReference` can be used as variable.
Use of mutable (java) objects will not be detected.
The existing java/kotlin ecosystem will also force you to write impure code 
occasionally.


## Releases
version 3.0:
- all rules configurable  (thanks  @krzykrucz) 
- class rule (thanks  @krzykrucz)
- abstract class rule (thanks  @krzykrucz)
- upgraded to dektekt 1.16
- compiled to jvm 1.8


version 2.1:
- detects `Unit` (thanks @MiSikora)
- detects mutable collections (thanks @krzykrucz)
- detects `throws` (thanks @krzykrucz)
    and all from version 1.3

version 1.3:
    detects only `var`, loops and `return`
