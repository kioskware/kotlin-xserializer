package kioskware.xserializer

import kioskware.xserializer.internals.FilteringEncoder
import kotlinx.serialization.*
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

/**
 * A serializer that filters properties based on a provided [PropertyFilter].
 * This serializer wraps an existing [KSerializer] and applies the filter during serialization.
 *
 * @param original The original serializer to wrap.
 * @param filter The [PropertyFilter] that determines which properties to include or exclude during serialization.
 */
class XSerializer<T>(
    private val original: KSerializer<T>,
    private val filter: PropertyFilter
) : KSerializer<T> by original {
    @ExperimentalSerializationApi
    override fun serialize(encoder: Encoder, value: T) {
        original.serialize(
            FilteringEncoder(encoder, filter),
            value
        )
    }
}

/**
 * Creates an instance of [XSerializer] for the specified type [T] with the given [filter].
 *
 * @param filter The [PropertyFilter] to apply during serialization.
 * @return An instance of [XSerializer] for the specified type [T].
 */
inline fun <reified T> filteredSerializer(
    noinline filter: PropertyFilter
): XSerializer<T> = XSerializer(serializer(), filter)


@SerialInfo
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class SkipSerialization

@Serializable
internal data class Address(
    val street: String,
    val city: String,
    @SkipSerialization
    val zipCode: String? = null
)

internal fun main() {

    val address = Address("123 Main St", "Springfield", "12345")

    val serializer = filteredSerializer<Address> {
        !propertyAnnotations.any { it is SkipSerialization }
    }

    val serialized = Json.encodeToString(
        serializer,
        address
    )

    println("Serialized Address: $serialized")

    val deserialized = Json.decodeFromString(
        serializer,
        serialized
    )

    println("Deserialized Address: $deserialized")

}

