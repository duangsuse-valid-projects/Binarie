package org.binarie

/** Things with capacity to keep its state.
 * behavior when calling [mark] again before [reset] is not defined. */
interface MarkReset { fun mark() fun reset() }
expect interface Flushable { fun flush() }
expect interface Closeable { fun close() }

enum class ByteOrder { LittleEndian, BigEndian }
inline val ByteOrder.isLE: Boolean get() = this == ByteOrder.LittleEndian
interface ByteOrdered { var byteOrder: ByteOrder }
expect fun systemOrder(): ByteOrder

/** Things with determinable [size] */
interface Sized { val size: Cnt }
