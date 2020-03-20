package de.paulwoitaschek.flowpref.android

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import de.paulwoitaschek.flowpref.android.internal.adapter.IntAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlowPrefTest {

  private val prefs: FlowPref

  init {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val sharedPrefs =
      context.getSharedPreferences(System.nanoTime().toString(), Context.MODE_PRIVATE)
    prefs = FlowPref(sharedPrefs)
  }

  @Test
  fun flowUpdated() {
    val pref = prefs.int("key", 42)
    val values = mutableListOf<Int>()
    val collectJob = GlobalScope.launch(Dispatchers.Main) {
      pref.flow.collect {
        values += it
      }
    }

    fun assert(vararg expected: Int) {
      assertThat(values).isEqualTo(expected.toList())
    }

    fun put(value: Int) {
      pref.setAndCommit(value)
    }

    assert(42)
    put(43)
    assert(42, 43)
    put(42)
    assert(42, 43, 42)

    collectJob.cancel()
  }

  @Test
  fun onlyChangedFlowTriggers() {
    val prefA = prefs.int("preA", 42)
    val valuesA = mutableListOf<Int>()
    val collectJobA = GlobalScope.launch(Dispatchers.Main) {
      prefA.flow.collect {
        valuesA += it
      }
    }

    val prefB = prefs.create("prefB", 42, IntAdapter)
    val valuesB = mutableListOf<Int>()
    val collectJobB = GlobalScope.launch(Dispatchers.Main) {
      prefB.flow.collect {
        valuesB += it
      }
    }

    fun assert(a: List<Int>, b: List<Int>) {
      assertThat(valuesA).isEqualTo(a)
      assertThat(valuesB).isEqualTo(b)
    }

    prefA.setAndCommit(1)
    assert(
      a = listOf(42, 1),
      b = listOf(42)
    )

    prefA.setAndCommit(2)
    assert(
      a = listOf(42, 1, 2),
      b = listOf(42)
    )

    prefB.setAndCommit(1)
    assert(
      a = listOf(42, 1, 2),
      b = listOf(42, 1)
    )

    collectJobA.cancel()
    collectJobB.cancel()
  }

  @Test
  fun delete() {
    val pref = prefs.int("key", 42)
    val values = mutableListOf<Int>()
    val collectJob = GlobalScope.launch(Dispatchers.Main) {
      pref.flow.collect {
        values += it
      }
    }

    fun assert(vararg expected: Int) {
      assertThat(values).isEqualTo(expected.toList())
    }

    fun put(value: Int) {
      pref.setAndCommit(value)
    }

    assert(42)
    put(43)
    assert(42, 43)
    pref.delete()
    assert(42, 43, 42)

    collectJob.cancel()
  }

  @Test
  fun prefAdapter() {
    data class Person(val name: String)

    val personAdapter = object : PrefAdapter<Person?> {
      override fun toString(value: Person?): String? {
        return value?.name
      }

      override fun fromString(string: String?): Person? {
        if (string == null) {
          return null
        } else {
          return Person(string)
        }
      }
    }
    val personPref = prefs.create("key", null, personAdapter)
    assertThat(personPref.value).isNull()
    val peter = Person("Peter")
    personPref.value = peter
    assertThat(personPref.value).isEqualTo(peter)
    personPref.value = null
    @Suppress("USELESS_CAST") // compiler bug?
    assertThat(personPref.value as Person?).isNull()
  }
}
