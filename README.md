# Unsafe Accessor

> [Legacy repository location](https://github.com/karlamoe/unsafe-accessor/tree/master)

Unsafe Accessor is a bridge to access Unsafe (`sun.misc.Unsafe` or `jdk.internal.misc.Unsafe`)

## Modules

> Maven group: `moe.karla.unsafe`

| Module                     | Description                                                                                                                                                                                               |
|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `unsafe-accessor-root`     | The main module for unsafe access.<br/>This module provides the minimal API for:<br/><li>`AccessibleObject.setAccessible`</li><li>`Unsafe.allocateObject`</li><li>`MethodHandles.Lookup.IMPL_LOOKUP`</li> |
| `unsafe-accessor-security` | The module for controlling the direct access for unsafe api.                                                                                                                                              |
| `unsafe-accessor-unsafe`   | Provide the bridge for accessing `jdk.internal.misc.Unsafe` or `sun.misc.Unsafe`                                                                                                                          |

[![Unsafe Root version](https://img.shields.io/maven-central/v/moe.karla.unsafe/unsafe-accessor-root?label=unsafe-accessor-root)](https://central.sonatype.com/artifact/moe.karla.unsafe/unsafe-accessor-root)
[![Unsafe Bridge version](https://img.shields.io/maven-central/v/moe.karla.unsafe/unsafe-accessor-root?label=unsafe-accessor-unsafe)](https://central.sonatype.com/artifact/moe.karla.unsafe/unsafe-accessor-unsafe)


API Note:

For stably and avoid future JDK compatibility, `sun.misc.Unsafe` (module `jdk.unsupported`) is required at
runtime. <br/>
It means you must include module `jdk.unsupported` when using jlink.


