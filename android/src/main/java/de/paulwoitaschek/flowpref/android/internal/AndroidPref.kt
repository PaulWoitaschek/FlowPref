package de.paulwoitaschek.flowpref.android.internal

import android.content.SharedPreferences
import androidx.core.content.edit
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlin.reflect.KProperty

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
internal class AndroidPref<T>(
  private val prefs: SharedPreferences,
  private val adapter: InternalPrefAdapter<T>,
  val key: String,
  private val default: T
) : Pref<T>() {

  private val channel = ConflatedBroadcastChannel<T>()

  init {
    notifyChanged()
  }

  fun notifyChanged() {
    val value = if (prefs.contains(key)) {
      adapter.get(key, prefs)
    } else {
      default
    }
    channel.offer(value)
  }

  override val flow: Flow<T> = channel.asFlow()

  override fun getValue(thisRef: Any, property: KProperty<*>): T = channel.value

  override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
    adapter.set(key, prefs, value)
  }

  override fun setAndCommit(value: T) {
    adapter.set(key, prefs, value, commit = true)
  }

  override fun delete(commit: Boolean) {
    prefs.edit(commit = commit) { remove(key) }
  }
}
