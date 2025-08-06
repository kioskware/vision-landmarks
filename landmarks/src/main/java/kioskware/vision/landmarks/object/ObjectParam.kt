package kioskware.vision.landmarks.`object`

/**
 * Represents a parameter for an object in the context of landmark detection.
 * @param typeId The unique identifier for the type of object.
 * @param value The value associated with the object, which can be of any type.
 * @param score The confidence score for the detected object, defaulting to 1.0f.
 * Typically the value should be between 0.0f and 1.0f, where 1.0f indicates a perfect score.
 */
data class ObjectParam<T>(
    val typeId: String,
    val value: T,
    val score: Float = 1.0f
)