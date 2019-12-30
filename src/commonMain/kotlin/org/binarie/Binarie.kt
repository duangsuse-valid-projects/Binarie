package org.binarie

import org.binarie.interf.ByteReader
import org.binarie.interf.DataReader
import org.binarie.interf.DataWriter

infix fun Idx.untilSize(size: Cnt): IdxRange = this until (this+size) //0 until (0+10): 0..9
inline val IdxRange.size: Cnt get() = (last - first).inc() //0..0: (0-0)+1

fun <R> MarkReset.positional(op: Producer<R>): R = try { mark(); op() } finally { reset() }
fun ByteReader.takeBytes(count: Cnt): Buffer {
  val allocated = Buffer(count)
  readToFill(allocated)
  return allocated
}

abstract class BaseDataReader(private val srcOrder: ByteOrder): ByteOrdered, DataReader {
  protected val shouldSwap get() = byteOrder != srcOrder
  override var byteOrder: ByteOrder = srcOrder
}
abstract class BaseDataWriter(private val dstOrder: ByteOrder): ByteOrdered, DataWriter {
  protected val shouldSwap get() = byteOrder != dstOrder
  override var byteOrder: ByteOrder = dstOrder
}
