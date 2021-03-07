# kure-potlin

This is a small plugin to [detekt](https://github.com/detekt/detekt) tool.
The purpose is to report the potential use of impure language elements in
kotlin code.

So far it simply detects:

- use of `var`
- use of `for`, `while`
- use of  `return` statement


## Usage

a) Use detect plugin in gradle build

```kotlin
plugins {
    id("io.gitlab.arturbosch.detekt").version("1.15.0")
}
```

b) add dependency
```kotlin
 dependencies {
        detektPlugins("pl.setblack:kure-potlin:0.1.3")
}
```

## Examples
```
fun impure (y:Int):Int {
                    var  x = 1
                    x = x + y
                    return x
                }
```

Function above will be reported as impure (uses `var` and `return`).

It can be rewritten to a pure version.
```
fun pure (y:Int ):Int = y + 1 
```


## Suppressing

If you need to ignore/suppress issues reported by this plugin.
Just annotate function or class with:
`@Suppress("ImpureCode")`

(This is a standard `detekt` feature).


##  Disclaimer

This plugin will not enforce 100% pure functional programing.
It only checks for some keywords. If you use some java libraries 
it will be easily possible to cheat. For instance: `AtomicReference` can be used as variable.
Use of mutable (java) objects will not be detected.
The existing java/kotlin ecosystem will also force you to write impure code 
occasionally.
