# Unsafe Accessor

> [Legacy repository location](https://github.com/karlamoe/unsafe-accessor/tree/master)

Unsafe Accessor is a bridge to access Unsafe (`sun.misc.Unsafe` or `jdk.internal.misc.Unsafe`)

## Modules

> Maven group: `moe.karla.unsafe`

| Module                          | Description                                                                                                                                                                                               |
|---------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `unsafe-accessor-root`          | The main module for unsafe access.<br/>This module provides the minimal API for:<br/><li>`AccessibleObject.setAccessible`</li><li>`Unsafe.allocateObject`</li><li>`MethodHandles.Lookup.IMPL_LOOKUP`</li> |
| `unsafe-accessor-security`      | The module for controlling the direct access for unsafe api.                                                                                                                                              |
| `unsafe-accessor-unsafe`        | Provide the bridge for accessing `jdk.internal.misc.Unsafe` or `sun.misc.Unsafe`                                                                                                                          |
| `unsafe-accessor-module-editor` | The helper to calling `Module.addOpens`                                                                                                                                                                   |
| `unsafe-accessor-definer`       | The helper to defining classes with any classloader                                                                                                                                                       |

[![Unsafe Root version](https://img.shields.io/maven-central/v/moe.karla.unsafe/unsafe-accessor-root?label=unsafe-accessor-root)](https://central.sonatype.com/artifact/moe.karla.unsafe/unsafe-accessor-root)
[![Unsafe Bridge version](https://img.shields.io/maven-central/v/moe.karla.unsafe/unsafe-accessor-root?label=unsafe-accessor-unsafe)](https://central.sonatype.com/artifact/moe.karla.unsafe/unsafe-accessor-unsafe)

## Technical Information

### JLink & java module system

Unsafe accessor is compatible with the Java module system.
When you're packing your application with jlink, you may need define unsafe-accessor's module.

| Module name                      | Description                                                                                                                    |
|----------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| `moe.karla.unsafe.security`      | Required. The security layer of UnsafeAccessor                                                                                 |
| `moe.karla.unsafe.root`          | Required. The basic API of UnsafeAccessor                                                                                      |
| `jdk.unsupported`                | Suggested. You have to add `--add-opens java.base/java.base.invoke=moe.karla.unsafe.root` if you dont include this JDK module. |
| `moe.karla.unsafe.unsafe`        | Optional. This is a optional module that provide API view of jdk.internal.misc.Unsafe                                          |
| `moe.karla.unsafe.unsafe.j9`     | _Required_. This module is the implementation of Java9+ unsafe object api bridge.                                              |
| `moe.karla.unsafe.module.editor` | Optional. This is a optional module that provide a helper for calling `Module#addOpens`                                        |
| `moe.karla.unsafe.definer`       | Optional. This is a optional module that provide a helper for defining classes with any classloader                            |

> Note: If you are using Compose Multiplatform, you may include unsafe-accessor modules via compose configuration
> too. <br/>
> See https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-native-distribution.html#including-jdk-modules

### Android

> [!WARNING]
>
> NOT SUPPORTED ON ANDROID.
>
> Just like the name of the project, unsafe accessor is UNSAFE.
> It relies heavily on the high degree of freedom of the JVM platform,
> which may unsupported on Android platform.
>
> Integration of UnsafeAccessor into Android is untested and unsupported.


The unsafe accessor is highly dependent on
[`java.lang.invoke`](https://developer.android.com/reference/java/lang/invoke/package-summary)(API Level 26, Android 8)

The core of UnsafeAccessor is getting the private protected field `java.lang.invoke.MethodHandles$Lookup#IMPL_LOOKUP`,
which can be got via reflection (java 8).
