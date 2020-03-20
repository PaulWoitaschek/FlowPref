package de.paulwoitaschek.flowpref.android.adapter

interface PrefAdapter<T> {

  fun serialize(value: T): String?
  fun parse(string: String?): T
}
