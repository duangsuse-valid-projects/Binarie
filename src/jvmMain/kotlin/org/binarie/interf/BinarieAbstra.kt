package org.binarie.interf

import org.binarie.*

actual interface DataReader: ByteOrdered {
  fun readInt64():Int64 //JVM-only
  actual fun readNat8(): Nat8
  actual fun readNat16(): Nat16
  actual fun readChar16(): Char16
  actual fun readInt8(): Int8
  actual fun readInt16(): Int16
  actual fun readInt32(): Int32
  actual fun readRat32(): Rat32
  actual fun readRat64(): Rat64
}
actual interface DataWriter: ByteOrdered {
  fun writeInt64(x:Int64) //JVM-only
  actual fun writeNat8(x: Nat8)
  actual fun writeNat16(x: Nat16)
  actual fun writeChar16(x: Char16)
  actual fun writeInt8(x: Int8)
  actual fun writeInt16(x: Int16)
  actual fun writeInt32(x: Int32)
  actual fun writeRat32(x: Rat32)
  actual fun writeRat64(x: Rat64)
}
