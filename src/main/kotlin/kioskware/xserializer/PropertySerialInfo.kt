package kioskware.xserializer

import kotlinx.serialization.descriptors.SerialDescriptor

/**
 * Data class representing serialization information for a property.
 *
 * @property serialName The name of the property as it will be serialized.
 * @property isNullable Indicates whether the property can be null.
 * @property isOptional Indicates whether the property is optional in the serialization context.
 * @property propertyAnnotations A list of annotations applied to the property itself.
 * @property classAnnotations A list of annotations applied to the class of the property type.
 * @property serialDescriptor The [SerialDescriptor] for the property, providing metadata about its type and structure.
 */
data class PropertySerialInfo(
    val serialName: String,
    val isNullable: Boolean,
    val isOptional: Boolean,
    val propertyAnnotations: List<Annotation>, // parentDescriptor.getElementAnnotations(index)
    val classAnnotations: List<Annotation>,     // descriptor.annotations
    val serialDescriptor: SerialDescriptor // parentDescriptor.getElementDescriptor(index)
) {

    /**
     * A lazy list of all annotations, combining both property and class annotations
     * for easier access.
     */
    val annotations: List<Annotation> by lazy {
        propertyAnnotations + classAnnotations
    }

}