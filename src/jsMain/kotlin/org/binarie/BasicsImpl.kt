package org.binarie

import org.binarie.interf.Reader
import org.binarie.interf.Writer
import org.khronos.webgl.*

actual interface Flushable { actual fun flush() }
actual interface Closeable { actual fun close() }

actual fun systemOrder(): ByteOrder {
  val int16Union = ArrayBuffer(2)
  val int8StructView = Uint8Array(int16Union)
  val int16View = Uint16Array(int16Union)
  int8StructView[0] = 0xFF.toByte(); int8StructView[1] = 0x00.toByte()
  return when (int16View[0]) {
    0x00FF.toShort() -> ByteOrder.LittleEndian
    0xFF00.toShort() -> ByteOrder.BigEndian
    else -> error("unknown browser endian")
  }
}
val jsOrder: ByteOrder = ByteOrder.BigEndian

/** Reader for JavaScript by [DataView] */
class ArrayBufferReader(buffer: ArrayBuffer): Reader {
  private val view = DataView(buffer)
  private val bytes = Int8Array(buffer)
  override var byteOrder: ByteOrder = systemOrder()

  override var position: Cnt = 0
    private set
  override val estimate: Cnt
    get() = view.byteLength-position

  private var oldPosition: Cnt = (-1)
  override fun mark() { oldPosition = position }
  override fun reset() { position = oldPosition }
  override fun skip(n: Cnt) { position += n }
  override fun close() {}

  override fun readByte(): Byte = readInt8()
  override fun readTo(destination: Buffer, indices: IdxRange) {
    val subview = bytes.offset(position)
    for (index in indices) destination[index] = subview[index]
  }
  override fun readToFill(destination: Buffer) {
    readTo(destination, destination.indices)
  }

  override fun readInt8(): Int8 = readType(DataView::getInt8, Int8.SIZE_BYTES)
  override fun readInt16(): Int16 = readType(DataView::getInt16, Int16.SIZE_BYTES)
  override fun readInt32(): Int32 = readType(DataView::getInt32, Int32.SIZE_BYTES)
  override fun readNat8(): Nat8 = readType(DataView::getUint8, 8/8).toInt()
  override fun readNat16(): Nat16 = readType(DataView::getUint16, 16/8).toInt()
  override fun readNat32(): Nat32 = readType(DataView::getUint32, 32/8).toLong()
  override fun readChar16(): Char16 = readInt16().toChar()
  override fun readRat32(): Rat32 = readType(DataView::getFloat32, 32/8)
  override fun readRat64(): Rat64 = readType(DataView::getFloat64, 64/8)

  private inline fun <I> readType(read: DataView.(Cnt) -> I, size: Cnt): I
    = view.read(position).also { skip(size) }
  private inline fun <I> readType(read: DataView.(Cnt, Boolean) -> I, size: Cnt): I
    = view.read(position, byteOrder.isLE).also { skip(size) }
}

/** The one output [DataView] Writer used in JavaScript **/
class BufferWriter(buffer: ArrayBuffer): Writer {
  constructor(size: Cnt): this(ArrayBuffer(size))
  private val view = DataView(buffer)
  private val bytes = Int8Array(buffer)
  override var byteOrder: ByteOrder = jsOrder

  private var position: Cnt = 0
  override fun flush() {} override fun close() {}

  override fun writeByte(b: Byte) { bytes[position++] = b }
  override fun writeFrom(source: Buffer, indices: IdxRange) {
    bytes.set(source.sliceArray(indices).toTypedArray(), position)
    position += indices.size
  }
  override fun writeAllFrom(source: Buffer) {
    bytes.set(source.toTypedArray(), position)
    position += source.size
  }

  override fun writeInt8 (x:Int8) = writeType(DataView::setInt8, Int8.SIZE_BYTES, x)
  override fun writeInt16(x:Int16) = writeType(DataView::setInt16, Int16.SIZE_BYTES, x)
  override fun writeInt32(x:Int32) = writeType(DataView::setInt32, Int32.SIZE_BYTES, x)
  override fun writeNat8 (x:Nat8) = writeType(DataView::setUint8, 8/8, x.toByte())
  override fun writeNat16(x:Nat16) = writeType(DataView::setUint16, 16/8, x.toShort())
  override fun writeNat32(x:Nat32) = writeType(DataView::setUint32, 32/8, x.toInt())
  override fun writeChar16(x:Char16) = writeInt16(x.toShort())
  override fun writeRat32(x:Rat32) = writeType(DataView::setFloat32, 32/8, x)
  override fun writeRat64(x:Rat64) = writeType(DataView::setFloat64, 64/8, x)

  private inline fun <I> writeType(write: DataView.(Cnt, I) -> Unit, size: Cnt, data: I)
    { view.write(position, data).also { position += size } }
  private inline fun <I> writeType(write: DataView.(Cnt, I, Boolean) -> Unit, size: Cnt, data: I)
    { view.write(position, data, byteOrder.isLE).also { position += size } }
}

////
private fun Int8Array.offset(pos: Cnt): Int8Array = subarray(pos, length)
