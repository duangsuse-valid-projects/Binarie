package org.binarie.helper

import org.binarie.Cnt
import org.binarie.Idx
import org.binarie.Sized
import kotlin.reflect.KProperty

/** Dynamic tuple using [Array] of `Any?` as storage. */
abstract class Tuple<E>(final override val size: Cnt): Sized {
  private val storage = arrayOfNulls<Any?>(size)
  @Suppress("UNCHECKED_CAST")
  operator fun get(index: Idx): E = storage[index] as E
  operator fun set(index: Idx, value: E) { storage[index] = value }

  protected class Index<T: Any>(private val index: Idx) {
    operator fun getValue(self: Tuple<T>, _p: KProperty<*>): T = self[index]
    operator fun setValue(self: Tuple<in T>, _p: KProperty<*>, value: T) { self[index] = value }
  }
  protected fun <T: Any> index(no: Idx): Index<T> = Index(no)
  fun toString(limit: Cnt): String = "Tuple(${storage.joinToString("|", limit = limit)})"
}
operator fun <E> Tuple<E>.component1() = this[0]
operator fun <E> Tuple<E>.component2() = this[1]
operator fun <E> Tuple<E>.component3() = this[2]
operator fun <E> Tuple<E>.component4() = this[3]
