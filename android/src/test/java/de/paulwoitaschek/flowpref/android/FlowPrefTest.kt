package de.paulwoitaschek.flowpref.android

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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
    PrefTester(prefs.int("key", 42)).run {
      assert(42)
      put(43)
      assert(42, 43)
      put(42)
      assert(42, 43, 42)
      release()
    }
  }

  @Test
  fun onlyChangedFlowTriggers() {
    val testerA =
      PrefTester(prefs.int("preA", 42))
    val testerB =
      PrefTester(prefs.int("prefB", 42))


    fun assert(a: List<Int>, b: List<Int>) {
      testerA.assert(*a.toTypedArray())
      testerB.assert(*b.toTypedArray())
    }

    testerA.put(1)
    assert(
      a = listOf(42, 1),
      b = listOf(42)
    )

    testerA.put(2)
    assert(
      a = listOf(42, 1, 2),
      b = listOf(42)
    )

    testerB.put(1)
    assert(
      a = listOf(42, 1, 2),
      b = listOf(42, 1)
    )

    testerA.release()
    testerB.release()
  }

  @Test
  fun delete() {
    PrefTester(prefs.int("key", 42)).run {
      assert(42)
      put(43)
      assert(42, 43)
      delete()
      assert(42, 43, 42)
      release()
    }
  }

  @Test
  fun prefAdapter() {
    data class Person(val name: String)

    val personAdapter = object : PrefAdapter<Person?> {
      override fun toString(value: Person?): String {
        return value?.name ?: "null"
      }

      override fun fromString(string: String): Person? {
        return if (string == "null") {
          null
        } else {
          Person(string)
        }
      }
    }
    val personPref = prefs.create("key", null, personAdapter)

    PrefTester(personPref).run {
      val peter = Person("Peter")
      assert(null)
      put(peter)
      assert(null, peter)
      put(null)
      assert(null, peter, null)
      release()
    }
  }
}
