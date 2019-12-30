package org.binarie

import org.binarie.interf.BasicReader
import java.io.*
import java.io.Closeable
import java.io.Flushable
import java.nio.ByteOrder as NioByteOrder

actual interface Flushable: Flushable
actual interface Closeable: Closeable

actual fun systemOrder(): ByteOrder = when (NioByteOrder.nativeOrder()) {
  NioByteOrder.LITTLE_ENDIAN -> ByteOrder.LittleEndian
  NioByteOrder.BIG_ENDIAN -> ByteOrder.BigEndian
  else -> error(unknownVersion<NioByteOrder>())
}

fun InputStream.basicReader(readLimit: Cnt = DEFAULT_BUFFER_SIZE) = InputStreamBasicReader(this, readLimit)
class InputStreamBasicReader(private val s: InputStream, private val readLimit: Cnt): BasicReader, Closeable by s {
  override fun readByte(): Byte = s.read().toByte().also { ++position }
  override fun readTo(destination: Buffer, indices: IdxRange) {
    s.read(destination, indices.first, indices.size)
    position += indices.size
  }
  override fun readToFill(destination: Buffer) { readTo(destination, destination.indices) }
  override fun mark() { s.mark(readLimit); oldPosition = position }
  override fun reset() { s.reset(); position = oldPosition }
  override val estimate: Cnt
    get() = s.available()
  override var position: Cnt = 0
    private set
  private var oldPosition: Cnt = (-1)
  override tailrec fun skip(n: Cnt) {
    if (n == 0) return
    val skipped = s.skip(n.toLong())
    if (skipped == 0L) return
    else this.skip(n - skipped.toInt())
  }
}

////
private inline fun <reified K> unknownVersion(): String = "unknown version: "+K::class.toString()
