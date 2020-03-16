package de.paulwoitaschek.flowpref

import kotlinx.coroutines.flow.Flow
import kotlin.properties.ReadWriteProperty

abstract class Pref<T> : ReadWriteProperty<Any, T> {

  @Suppress("LeakingThis")
  var value: T by this

  abstract fun setAndCommit(value: T)

  abstract val flow: Flow<T>

  abstract fun delete()
}
