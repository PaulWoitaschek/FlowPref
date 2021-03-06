# DEPRECATED

I recommend to use [Jetpack Datastore](https://developer.android.com/topic/libraries/architecture/datastore) instead.

# FlowPref ![](https://github.com/PaulWoitaschek/FlowPref/workflows/CI/badge.svg)

FlowPref is a library for making Preferences composable, typesafe and reactive (by exposing a kotlin Flow).

## Usage

```kotlin
// create a flow pref backed by shared preferences
val sharedPrefs = context.getSharedPreferences("prefName", Context.MODE_PRIVATE)
// ensure this is a singleton across process
val prefs = AndroidPreferences(sharedPrefs)

val intPref = prefs.int("prefKey", 42)

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
    // android implementation
    implementation 'com.github.PaulWoitaschek.FlowPref:android:x.y.z'
    
    // core only, useful for consuming kotlin-only modules
    implementation 'com.github.PaulWoitaschek.FlowPref:core:x.y.z'

    // an in-memory implementation, useful for testing. Pure kotlin
    implementation 'com.github.PaulWoitaschek.FlowPref:in-memory:x.y.z'
}
```
