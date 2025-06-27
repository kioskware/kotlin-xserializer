package kioskware.xserializer

import kotlinx.serialization.SerialInfo
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// Example usage with a custom annotation
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
annotation class Sensitive

@Serializable
data class Address(
    val street: String,
    @Sensitive val city: String?
)

@Serializable
data class User(
    val name: String,
    @Sensitive val age: Int?,
    val address: Address,
    //@Sensitive
    val passwords: List<Password>
)

@Serializable
data class Password(
    @Sensitive val value: String?,
    @Sensitive val isSensitive: Boolean?
)

private val jsonPretty: Json by lazy {
    Json {
        prettyPrint = true
        encodeDefaults = true
    }
}

fun main() {
    val user = User(
        "Alice",
        30,
        Address("123 Main St", "Springfield"),
        listOf(
            Password("password123", true),
            Password("secret", true)
        )
    )

    // Serialize with the filtering serializer
    val json = jsonPretty.encodeToString(
        filteredSerializer {
            !it.propertyAnnotations.any { it is Sensitive }
        },
        user
    )
    println(json) // Output: {"name":"Alice","address":{"street":"123 Main St"}}
}