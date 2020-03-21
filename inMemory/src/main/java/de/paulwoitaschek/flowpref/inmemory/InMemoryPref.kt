package de.paulwoitaschek.flowpref.inmemory

import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlin.reflect.KProperty


@Suppress("unused")
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class InMemoryPref<T>(private val default: T) : Pref<T>() {

  private val channel = ConflatedBroadcastChannel(default)

  override fun setAndCommit(value: T) {
    channel.offer(value)
  }

  override val flow: Flow<T> = channel.asFlow()

  override fun delete() {
    channel.offer(default)
  }

  override fun getValue(thisRef: Any, property: KProperty<*>): T {
    return channel.value
  }

  override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
    channel.offer(value)
  }
}
