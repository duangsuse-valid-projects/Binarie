package org.binarie.pat

import org.binarie.*
import org.binarie.interf.Reader
import org.binarie.interf.Writer

val int8 = object: BinPattern<Int8> {
  override fun read(reader: Reader): Int8 = reader.readInt8()
  override fun write(writer: Writer, data: Int8): Unit = writer.writeInt8(data)
  override val byteSize: Cnt = Int8.SIZE_BYTES
}
val int16 = object: BinPattern<Int16> {
  override fun read(reader: Reader): Int16 = reader.readInt16()
  override fun write(writer: Writer, data: Int16): Unit = writer.writeInt16(data)
  override val byteSize: Cnt = Int16.SIZE_BYTES
}
val int32 = object: BinPattern<Int32> {
  override fun read(reader: Reader): Int32 = reader.readInt32()
  override fun write(writer: Writer, data: Int32): Unit = writer.writeInt32(data)
  override val byteSize: Cnt = Int32.SIZE_BYTES
}
val char16 = object: BinPattern<Char16> {
  override fun read(reader: Reader): Char16 = reader.readChar16()
  override fun write(writer: Writer, data: Char16): Unit = writer.writeChar16(data)
  override val byteSize: Cnt = Char16.SIZE_BYTES
}
val rat32 = object: BinPattern<Rat32> {
  override fun read(reader: Reader): Rat32 = reader.readRat32()
  override fun write(writer: Writer, data: Rat32): Unit = writer.writeRat32(data)
  override val byteSize: Cnt = 32/8
}
val rat64 = object: BinPattern<Rat64> {
  override fun read(reader: Reader): Rat64 = reader.readRat64()
  override fun write(writer: Writer, data: Rat64): Unit = writer.writeRat64(data)
  override val byteSize: Cnt = 64/8
}

val nat8 = object: BinPattern<Nat8> {
  override fun read(reader: Reader): Nat8 = reader.readNat8()
  override fun write(writer: Writer, data: Nat8): Unit = writer.writeNat8(data)
  override val byteSize: Cnt = 8/8
}
val nat16 = object: BinPattern<Nat16> {
  override fun read(reader: Reader): Nat16 = reader.readNat16()
  override fun write(writer: Writer, data: Nat16): Unit = writer.writeNat16(data)
  override val byteSize: Cnt = 16/8
}
