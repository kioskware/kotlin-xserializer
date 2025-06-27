package kioskware.xserializer

/**
 * Type alias for a function that filters properties based on their serial information.
 * This function takes a [PropertySerialInfo] object and returns a Boolean indicating
 * whether the property should be included (true) or excluded (false) in serialization.
 */
typealias PropertyFilter = (PropertySerialInfo) -> Boolean