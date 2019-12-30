package org.binarie.interf

import org.binarie.*

interface ReadControl: MarkReset, Closeable {
  val position: Cnt; val estimate: Cnt
  fun skip(n: Cnt)
}
interface WriteControl: Flushable, Closeable

interface ByteReader {
  fun readByte(): Byte
  fun readTo(destination: Buffer, indices: IdxRange)
  fun readToFill(destination: Buffer)
}
interface ByteWriter {
  fun writeByte(b: Byte)
  fun writeFrom(source: Buffer, indices: IdxRange)
  fun writeAllFrom(source: Buffer)
}

expect interface DataReader: ByteOrdered {
  fun readNat8():Nat8 fun readNat16():Nat16
  //fun readNat32():Nat32 //JS-only
  fun readChar16():Char16
  fun readInt8():Int8 fun readInt16():Int16
  fun readInt32():Int32 //fun readInt64():Int64 //JVM-only
  fun readRat32():Rat32 fun readRat64():Rat64
}
expect interface DataWriter: ByteOrdered {
  fun writeNat8(x:Nat8) fun writeNat16(x:Nat16)
  //fun writeNat32(x:Nat32) //JS-only
  fun writeChar16(x:Char16)
  fun writeInt8(x:Int8) fun writeInt16(x:Int16)
  fun writeInt32(x:Int32) //fun writeInt64(x:Int64) //JVM-only
  fun writeRat32(x:Rat32) fun writeRat64(x:Rat64)
}

interface BasicReader: ByteReader, ReadControl
interface Reader: DataReader, BasicReader
interface BasicWriter: ByteWriter, WriteControl
//interface BufferWriter: BasicWriter { val buffer: Buffer } //JS uses ArrayBuffer
interface Writer: DataWriter, BasicWriter
