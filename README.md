# FlowPref

FlowPref is a library for making Preferences composable, typesafe and reactive (by exposing a kotlin Flow).

## Usage

```kotlin
// create a flow pref backed by shared preferences
val sharedPrefs = context.getSharedPreferences(System.nanoTime().toString(), Context.MODE_PRIVATE)
val flowPref = FlowPref(sharedPrefs)

val intPref = prefs.int("key", 42)

// write and get values
intPref.value = 5
println(intPref.value)

// flow
scope.launch {
  intPref.flow.collect { 
    println(it)
  }
}

// use it as a property
var myValue by intPref
myValue = 5
println(myValue)
```

## Setup

Add jitpack to your repositories:
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

And add the dependency:
```groovy
dependencies {
    compile 'com.github.PaulWoitaschek:FlowPref:x.y.z'
}
```
