# Kotlin xSerializer

Extension for kotlin serialization, allowing to dynamically exclude some fields from the serialized output.
Comparing to `@Transient` annotation, this library allows to use custom predicate to filter properties, 
so you can skip properties based on their `SerialDescriptor`.

Perfect for filtering data based on access scopes, user roles, or any other criteria.

Developed by [kioskware.co](kioskware.co) - company delivering professional Android kiosk software and hardware. Based in Poland 🇵🇱

## Features

- ✅ Exclude properties from serialization with custom predicate
- ✅ Works with any serialization format supported by `kotlinx.serialization`
- ✅ Support for nested objects, lists, and maps
- ✅ Simple and veeery lightweight
- ✅ Works on JVM and Android (other platforms will be added in the future)

## Importing dependency

1. Add the following dependency to your `build.gradle.kts` file:

    ```kotlin
    dependencies {
        implementation("com.github.kioskware:kotlin-xserializer:1.8.0") // or the latest version
    }
    ```
   **Note:** Make sure `xSerializer` version is the same as kotlin-serialization version 
   you are using in your project.


2. Add jitpack.io repository to your `settings.gradle.kts` file:

    ```kotlin
    pluginManagement {
        repositories {
            maven("https://jitpack.io")
            gradlePluginPortal()
        }
    }
    dependencyResolutionManagement {
        repositories {
            maven("https://jitpack.io")
            mavenCentral()
        }
    }
    ```
   
3. Add kotlin serialization plugin to your `build.gradle.kts` file:

    ```kotlin
    plugins {
        kotlin("plugin.serialization") version "1.8.0" // or the latest version
    }
    ```

## Usage

1. Create `PropertyFilter` function:

    ```kotlin
    val filter : PropertyFilter = {
        propertyAnnotations.none { it is SkipSerialization }
    }
    ```

2. Create data class with `@Serializable` annotation and use `@SkipSerialization` annotation on properties you want to skip:

    ```kotlin
    @Serializable
    data class Address(
        val street: String,
        val city: String,
        @SkipSerialization
        val zipCode: String? = null
    )
    ```

3. Use `filteredSerializer()` method to create a serializer that applies the filter:

    ```kotlin
    val serializer = filteredSerializer<Address>(filter)
    ```
4. Serialize your data class using the filtered serializer:

    ```kotlin
    val address = Address("123 Main St", "Springfield", "12345")
    val json = Json.encodeToString(serializer, address)
    println(json) // Output: {"street":"123 Main St","city":"Springfield"}
    ```
   **Note:** `kotlinx-serialization-json` is not included in this library, you need to add it separately to your dependencies or use another serialization format.


## License

Copyright © 2025 Kioskware, Jan Rozenbajgier

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
