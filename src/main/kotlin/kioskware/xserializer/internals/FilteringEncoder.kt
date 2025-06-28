package kioskware.xserializer.internals

import kioskware.xserializer.PropertyFilter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder

// Custom Encoder that applies the filter to structures
@ExperimentalSerializationApi
internal class FilteringEncoder(
    private val original: Encoder,
    private val filter: PropertyFilter,
    private val forceOptional: Boolean
) : Encoder by original {

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        val originalComposite = original.beginStructure(descriptor)
        return FilteringCompositeEncoder(originalComposite, filter, descriptor, forceOptional)
    }

    @ExperimentalSerializationApi
    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        val originalCollection = original.beginCollection(descriptor, collectionSize)
        return FilteringCompositeEncoder(originalCollection, filter, descriptor, forceOptional)
    }

    override fun encodeInline(descriptor: SerialDescriptor): Encoder {
        return FilteringEncoder(
            original.encodeInline(descriptor),
            filter,
            forceOptional
        )
    }

}