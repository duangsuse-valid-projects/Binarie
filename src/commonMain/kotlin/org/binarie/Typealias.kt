package org.binarie

typealias Cnt = Int
typealias Idx = Int
typealias IdxRange = IntRange
typealias Producer<R> = () -> R
typealias ActionOn<T, R> = T.() -> R

//since unsigned types are still an experimental feature,
//and signed Byte, Int, ... will not handle unsigned data
//safely, longer signed integers is used to store unsigned one.
typealias Nat8 = Int
typealias Nat16 = Int
typealias Nat32 = Long //JS-only

typealias Int8 = Byte
typealias Int16 = Short
typealias Int32 = Int
typealias Int64 = Long //JVM-only

typealias Char16 = Char
typealias Buffer = ByteArray

typealias Rat32 = Float
typealias Rat64 = Double
