# Binarie [![platform]](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.io/) [![version]](build.gradle)
[platform]: https://img.shields.io/badge/kotlin-multiplatform--1.3-orange?style=flat-square&logo=kotlin
[version]: https://img.shields.io/badge/version-1.0-informational?style=flat-square

> Combinator style binary structure read/write library for Kotlin(JVM/JS)

```kotlin
import org.binarie.Reader
import org.binarie.Writer

import org.binarie.helper.Tuple
import org.binarie.PatBuilder.struct
import org.binarie.PatBuilder.repeat
import org.binarie.PatBuilder.cond

import org.binarie.PatBuilder.bigEndian
import org.binarie.PatBuilder.littleEndian

import org.binarie.pat.*
```

## 📓 [Apache-2.0 Licensed](LICENSE)

## Related Link

+ [sdklite/SED (Java)](https://github.com/sdklite/sed/blob/master/src/main/java/com/sdklite/sed/)
+ [Syroot/BinaryData (blog, Chinese translated)](https://www.cnblogs.com/conmajia/p/a-more-powerful-binary-reader-writer.html)
+ [Syroot/BinaryData (C#, NuGet)](https://www.nuget.org/packages/Syroot.IO.BinaryData/)
