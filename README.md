# kure-potlin

This is a small plugin to [detekt](https://github.com/detekt/detekt) tool. The purpose is to report the potential use of
impure language elements in kotlin code.

## Detected rules

Rule | Detects | Properties <br /> with defaults | Requires <br /> [type resolution](https://detekt.github.io/detekt/type-resolution.html)
--- | --- | --- | ---
LoopDefinition | use of `for`, `while` | `active`: true |
ReturnStatement | use of `return` statement | `active`: true |
VariableDefinition | use of `var` | `active`: true |
ReturnUnit | use of function returning `Unit`, `Nothing`, `Void`; unless annotated with an annotation which name is specified in `ignoredAnnotations` | `active`: true<br />`ignoreFunctionType`: false<br />`ignoredAnnotations`: []<br />`ignoreDsl`: false | :ballot_box_with_check:
ClassDefinition | use of object-oriented `class` | `active`: false |
AbstractClassDefinition | use of object-oriented `abstract class` | `active`: false |
ThrowExpression | use of `throw` | `active`: true |
MutableCollections | use of mutable collections | `active`: true | :ballot_box_with_check:
BranchStatement | use of `if` or `when` as statement | `active`: true |
MissingElse | use of `if` statement without `else` (which also makes it a statement covered by `BranchStatement`) | `active`: false |

## Usage

a) Use detect plugin in gradle build (.kts syntax). In case of problems refer
to [detekt](https://detekt.github.io/detekt/gradle.html) documentation.

```kotlin
repositories {
    jcenter()
}

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.18.1")
}
```
(see [Releases](#Releases) for versions supporting older detekt)

b) add dependency

```kotlin
dependencies {
    detektPlugins("pl.setblack:kure-potlin:0.6.0")
}
```


c) optionally, you can reconfigure plugin by adding this to your custom `detekt.yaml`:

```yaml
impure:
  active: true
  LoopDefinition:
    active: true
  ReturnStatement:
    active: true
  VariableDefinition:
    active: true
  ReturnUnit:
    active: true
    ignoreFunctionType: false
    ignoredAnnotations: [ ]
    ignoreDsl: false
  ClassDefinition:
    active: false
  AbstractClassDefinition:
    active: false
  ThrowExpression:
    active: true
  MutableCollections:
    active: true
  BranchStatement:
    active: true
  MissingElse:
    active: false
```

d) run `gradle detekt` or `gradle detektMain` to use rules which
require [type resolution](https://detekt.github.io/detekt/type-resolution.html)

## License

Apache 2.0

[text](LICENSE)

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

If you need to ignore/suppress issues reported by this plugin. Just annotate function or class with:
`@Suppress("RULE")`
where RULE is just a [supported rule name](#Detected rules).

Example: 
```kotlin
@Suppress("ReturnStatement")
fun add(a:Int, b:Int) {
    val x  = a+b
    return x
}
```

(This is a standard `detekt` feature).

## Disclaimer

This plugin will not enforce 100% pure functional programing. It only checks for some keywords. If you use some java
libraries it will be easily possible to cheat. For instance: `AtomicReference` can be used as variable. Use of mutable (
java) objects will not be detected. The existing java/kotlin ecosystem will also force you to write impure code
occasionally.

## Releases

version 0.6.0: (detekt 0.18.1)

version 0.5.0: (detekt 0.16.0)
 - improved Unit rule (thanks @krzykrucz)

version 0.4.0:

- improved `Unit` detection, detects also `void`, `Nothing` statements (thanks @krzykrucz)
- introduced `BranchStatement`
- introduced optional `MissingElse` rule

version 0.3.0:

- all rules configurable  (thanks @krzykrucz)
- class rule (thanks @krzykrucz)
- abstract class rule (thanks @krzykrucz)
- upgraded to dektekt 1.16
- compiled to jvm 1.8

version 0.2.1:

- detects `Unit` (thanks @MiSikora)
- detects mutable collections (thanks @krzykrucz)
- detects `throws` (thanks @krzykrucz)
  and all from version 1.3

version 0.1.3:
detects only `var`, loops and `return`
