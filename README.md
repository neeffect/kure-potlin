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


