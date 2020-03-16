package de.paulwoitaschek.flowpref.adapter


import android.content.SharedPreferences
import androidx.core.content.edit
import de.paulwoitaschek.flowpref.FlowPref
import de.paulwoitaschek.flowpref.Pref

class EnumAdapter<E : Enum<E>>(private val clazz: Class<E>) : PrefAdapter<E> {

  override fun get(key: String, prefs: SharedPreferences): E {
    val stringValue = prefs.getString(key, "")!!
    return java.lang.Enum.valueOf(clazz, stringValue)
  }

  override fun set(key: String, prefs: SharedPreferences, value: E, commit: Boolean) {
    prefs.edit(commit = commit) { putString(key, value.name) }
  }
}


fun <E : Enum<E>> FlowPref.enum(key: String, default: E, clazz: Class<E>): Pref<E> {
  return create(EnumAdapter(clazz), key, default)
}

@Suppress("unused")
inline fun <reified E : Enum<E>> FlowPref.enum(key: String, default: E): Pref<E> {
  return enum(key, default, E::class.java)
}
