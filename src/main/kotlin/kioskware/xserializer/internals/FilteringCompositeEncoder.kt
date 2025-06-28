package kioskware.xserializer.internals

import kioskware.xserializer.PropertyFilter
import kioskware.xserializer.PropertySerialInfo
import kioskware.xserializer.XSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder

// Custom CompositeEncoder that filters properties based on PropertySerialInfo
@ExperimentalSerializationApi
internal class FilteringCompositeEncoder(
    private val original: CompositeEncoder,
    private val filter: PropertyFilter,
    private val descriptor: SerialDescriptor,
    private val forceOptional: Boolean
) : CompositeEncoder by original {

    // Helper function to check if a property should be encoded
    private fun shouldEncode(index: Int): Boolean {
        val elementDesc = descriptor.getElementDescriptor(index)
        val info = PropertySerialInfo(
            serialName = descriptor.getElementName(index),
            isNullable = elementDesc.isNullable,
            isOptional = descriptor.isElementOptional(index),
            propertyAnnotations = descriptor.getElementAnnotations(index),
            classAnnotations = descriptor.annotations,
            serialDescriptor = elementDesc
        )
        return filter(info).also {
            if (!it && !info.isOptional && forceOptional) {
                throw SerializationException("Filtered out property '${info.serialName}' must be optional.")
            }
        }
    }

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        if (shouldEncode(index)) {
            original.encodeStringElement(descriptor, index, value)
        }
    }

    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) {
        if (shouldEncode(index)) {
            original.encodeBooleanElement(descriptor, index, value)
        }
    }

    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
        if (shouldEncode(index)) {
            original.encodeByteElement(descriptor, index, value)
        }
    }

    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) {
        if (shouldEncode(index)) {
            original.encodeCharElement(descriptor, index, value)
        }
    }

    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) {
        if (shouldEncode(index)) {
            original.encodeShortElement(descriptor, index, value)
        }
    }

    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        if (shouldEncode(index)) {
            original.encodeIntElement(descriptor, index, value)
        }
    }

    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) {
        if (shouldEncode(index)) {
            original.encodeFloatElement(descriptor, index, value)
        }
    }

    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
        if (shouldEncode(index)) {
            original.encodeLongElement(descriptor, index, value)
        }
    }

    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) {
        if (shouldEncode(index)) {
            original.encodeDoubleElement(descriptor, index, value)
        }
    }

    override fun <T> encodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        if (shouldEncode(index)) {
            original.encodeSerializableElement(
                descriptor,
                index,
                XSerializer(
                    serializer as KSerializer<T>,
                    filter
                ),
                value
            )
        }
    }

    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?
    ) {
        if (shouldEncode(index)) {
            original.encodeNullableSerializableElement(
                descriptor,
                index,
                XSerializer(
                    serializer as KSerializer<T>,
                    filter
                ),
                value
            )
        }
    }

    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder {
        return FilteringEncoder(
            original.encodeInlineElement(descriptor, index),
            filter,
            forceOptional
        )
    }

}