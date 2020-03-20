package de.paulwoitaschek.flowpref.android

interface PrefAdapter<T> {

  fun serialize(value: T): String?
  fun parse(string: String?): T
}
