package org.binarie.pat

import org.binarie.ActionOn
import org.binarie.ByteOrder
import org.binarie.Cnt
import org.binarie.Idx
import org.binarie.helper.Tuple
import org.binarie.interf.Reader
import org.binarie.interf.Writer

/** A simple binary stream pattern can be _represented_ by [ST] */
interface BinPattern<ST> {
  fun read(reader: Reader): ST
  fun write(writer: Writer, data: ST)
  /** _statically_ known size of this substructure */
  val byteSize: Cnt?
}

/** Sequential binary pattern like C `struct` */
class Seq<T>(private val allocator: Allocator<T>, private vararg val items: BinPattern<T>): BinPattern<Tuple<T>> {
  private val size = items.size
  override fun read(reader: Reader): Tuple<T> {
    val result = allocator(size)
    for ((index, item) in items.withIndex()) result[index] = item.read(reader)
    return result
  }
  override fun write(writer: Writer, data: Tuple<T>) {
    for ((index, item) in items.withIndex()) item.write(writer, data[index])
  }
  /** byteSize can be solved when all [items] has statically known size */
  override val byteSize: Cnt?
    get() = items.mapIfAllNotNull { it.byteSize }?.sum()
}

/** Repeat of one substructure [item], with size depending on actual data stream */
class Repeat<T>(private val item: BinPattern<T>, private val readSize: ActionOn<Reader, Cnt>): BinPattern<Array<T>> {
  @Suppress("UNCHECKED_CAST")
  override fun read(reader: Reader): Array<T> {
    val result = arrayOfNulls<Any?>(reader.readSize())
    for (index in result.indices) result[index] = item.read(reader)
    return result as Array<T>
  }
  override fun write(writer: Writer, data: Array<T>) {
    for (element in data) item.write(writer, element)
  }
  override val byteSize: Cnt? = null
}

/** Conditional sub-patterns like C `union` can be decided depending on actual data stream with [flag] */
class Cond<T>(private val flag: BinPattern<Idx>, private vararg val conditions: BinPattern<T>): BinPattern<Pair<Idx, T>> {
  override fun read(reader: Reader): Pair<Idx, T> {
    val caseNo = flag.read(reader)
    return Pair(caseNo, conditions[caseNo].read(reader))
  }
  override fun write(writer: Writer, data: Pair<Cnt, T>) {
    val (caseNo, state) = data
    flag.write(writer, caseNo)
    conditions[caseNo].write(writer, state)
  }
  /** byteSize can be unified when all [conditions] has same statically known size */
  override val byteSize: Cnt?
    get() = conditions.mapIfAllNotNull { it.byteSize }?.toSet()?.singleOrNull()
}

/** Add argument/return listen for [BinPattern] read/write */
abstract class PrePost<T>(private val item: BinPattern<T>): BinPattern<T> {
  override fun read(reader: Reader): T {
    onReadPre(reader)
    return item.read(reader).also { onReadPost(reader, it) }
  }
  override fun write(writer: Writer, data: T) {
    onWritePre(writer, data)
    item.write(writer, data).also { onWritePost(writer) }
  }
  open fun onReadPre(reader: Reader) {}
  open fun onReadPost(reader: Reader, result: T) {}
  open fun onWritePre(writer: Writer, data: T) {}
  open fun onWritePost(writer: Writer) {}
  override val byteSize: Cnt? get() = item.byteSize
}

/** Read/write child [item] in byte order of [newEndian], then restore old order. */
open class EndianSwitch<T>(item: BinPattern<T>, private val newEndian: ByteOrder): PrePost<T>(item) {
  /** Old [ByteOrder] of reader/writer instance */
  private lateinit var oldByteOrder: ByteOrder
  override fun onReadPre(reader: Reader) {
    oldByteOrder = reader.byteOrder
    reader.byteOrder = newEndian
  }
  override fun onReadPost(reader: Reader, result: T) {
    reader.byteOrder = oldByteOrder
  }
  override fun onWritePre(writer: Writer, data: T) {
    oldByteOrder = writer.byteOrder
    writer.byteOrder = newEndian
  }
  override fun onWritePost(writer: Writer) {
    writer.byteOrder = oldByteOrder
  }
  class BigEndian<T>(item: BinPattern<T>): EndianSwitch<T>(item, ByteOrder.BigEndian)
  class LittleEndian<T>(item: BinPattern<T>): EndianSwitch<T>(item, ByteOrder.LittleEndian)
}

////
private fun <T, R: Any> Array<T>.mapIfAllNotNull(op: (T) -> R?): List<R>? = mapNotNull(op).takeIf { it.size == this.size }
typealias Allocator<R> = (Cnt) -> Tuple<R>
