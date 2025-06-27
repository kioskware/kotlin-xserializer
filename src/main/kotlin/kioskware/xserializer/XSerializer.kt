package kioskware.xserializer

import kioskware.xserializer.internals.FilteringEncoder
import kotlinx.serialization.*
import kotlinx.serialization.encoding.Encoder

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

/**
 * Creates an instance of [XSerializer] for the specified [KSerializer] with the given [filter].
 *
 * @param original The original [KSerializer] to wrap.
 * @param filter The [PropertyFilter] to apply during serialization.
 * @return An instance of [XSerializer] wrapping the original serializer.
 */
fun <T> KSerializer<T>.filtered(
    filter: PropertyFilter
): XSerializer<T> = XSerializer(this, filter)
