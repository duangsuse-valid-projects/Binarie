package org.binarie.jvm

import org.binarie.MarkReset
import org.binarie.pat.BinPattern
import org.binarie.Cnt
import org.binarie.Int64
import org.binarie.interf.Reader
import org.binarie.interf.Writer

fun MarkReset.positionalTask(): AutoCloseable = object: AutoCloseable {
  init { this@positionalTask.mark() }
  override fun close() { this@positionalTask.reset() }
}

val int64 = object: BinPattern<Int64> {
  override fun read(reader: Reader): Int64 = reader.readInt64()
  override fun write(writer: Writer, data: Int64): Unit = writer.writeInt64(data)
  override val byteSize: Cnt = Int64.SIZE_BYTES
}
