package kioskware.xserializer

// Data class to hold detailed property information as specified
data class PropertySerialInfo(
    val serialName: String,
    val isNullable: Boolean,
    val isOptional: Boolean,
    val propertyAnnotations: List<Annotation>, // parentDescriptor.getElementAnnotations(index)
    val classAnnotations: List<Annotation>     // descriptor.annotations
)