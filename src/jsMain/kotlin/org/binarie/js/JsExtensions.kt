package org.binarie.js

import org.binarie.pat.BinPattern
import org.binarie.Cnt
import org.binarie.Nat32
import org.binarie.interf.Reader
import org.binarie.interf.Writer

val nat32 = object: BinPattern<Nat32> {
  override fun read(reader: Reader): Nat32 = reader.readNat32()
  override fun write(writer: Writer, data: Nat32): Unit = writer.writeNat32(data)
  override val byteSize: Cnt = 32/8
}
