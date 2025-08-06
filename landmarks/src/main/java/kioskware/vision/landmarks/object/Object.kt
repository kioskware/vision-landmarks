package kioskware.vision.landmarks.`object`

import android.graphics.RectF
import kioskware.vision.landmarks.Landmark
import kioskware.vision.landmarks.isValidNormalized
import kioskware.vision.landmarks.toNormalized
import kioskware.vision.landmarks.toPixel

/**
 * Represents an object with a type ID, tracking ID, bounding box, landmarks, nested objects, and parameters.
 * This class is used to encapsulate information about detected objects in a scene,
 * including their spatial properties and associated landmarks.
 *
 * @param typeId The unique identifier for the type of object.
 * @param trackingId The unique identifier for tracking the object across frames.
 * @param bounding The bounding box of the object, represented as a [RectF].
 * @param landmarks A list of [Landmark]s associated with the object, representing key points of interest.
 * @param objects A list of nested [Object]s, allowing for hierarchical representation of objects within objects.
 * @param params A list of [ObjectParam]s that can hold additional parameters or metadata about the object.
 * @constructor Creates an instance of [Object] with the specified properties.
 */
open class Object(
    val typeId: String,
    val trackingId: String,
    val bounding: RectF,
    val landmarks: List<Landmark> = emptyList(),
    val objects: List<Object> = emptyList(),
    val params: List<ObjectParam<*>> = emptyList()
) {

    /**
     * Returns width of this object.
     * Equivalent to `bounding.width`.
     */
    val width: Float
        get() = bounding.width()

    /**
     * Returns height of this object.
     * Equivalent to `bounding.height`.
     */
    val height: Float
        get() = bounding.height()

}

/**
 * Converts this object from pixel coordinates to normalized relative coordinates [0,1].
 *
 * @param imageWidth The width of the image in pixels
 * @param imageHeight The height of the image in pixels
 * @param pixelDepthScale Optional scaling factor for z-coordinate conversion
 * @return NormalizedObject with coordinates in [0,1] range
 */
fun Object.toNormalized(
    imageWidth: Int,
    imageHeight: Int,
    pixelDepthScale: Float = imageWidth.toFloat()
): NormalizedObject {
    // Convert bounding box coordinates
    val normalizedBounding = RectF(
        this.bounding.left / imageWidth,
        this.bounding.top / imageHeight,
        this.bounding.right / imageWidth,
        this.bounding.bottom / imageHeight
    )

    // Convert landmarks
    val normalizedLandmarks = this.landmarks.map { landmark ->
        landmark.toNormalized(imageWidth, imageHeight, pixelDepthScale)
    }

    // Convert nested objects recursively
    val normalizedObjects = this.objects.map { nestedObj ->
        nestedObj.toNormalized(imageWidth, imageHeight, pixelDepthScale)
    }

    return NormalizedObject(
        typeId = this.typeId,
        trackingId = this.trackingId,
        bounding = normalizedBounding,
        normalizedLandmarks = normalizedLandmarks,
        normalizedObjects = normalizedObjects,
        params = this.params
    )
}

/**
 * Validates that all coordinates in a NormalizedObject are within valid bounds [0,1].
 *
 * @receiver The object to validate
 * @param checkZ Whether to also validate Z coordinates of landmarks
 * @return True if all coordinates are valid, false otherwise
 */
fun NormalizedObject.isValidNormalized(checkZ: Boolean = false): Boolean {
        // Check bounding box
        val boundingValid = bounding.left in 0.0f..1.0f &&
                bounding.top in 0.0f..1.0f &&
                bounding.right in 0.0f..1.0f &&
                bounding.bottom in 0.0f..1.0f

        if (!boundingValid) return false

        // Check landmarks using the correct property name
        val landmarksValid = normalizedLandmarks.all { landmark ->
            landmark.isValidNormalized(checkZ)
        }

        if (!landmarksValid) return false
        // Check nested objects recursively using the correct property name
        val objectsValid = normalizedObjects.all { obj ->
            obj.isValidNormalized(checkZ)
        }
        return objectsValid
    }

/**
 * Extension function for NormalizedObject to convert back to pixel coordinates.
 */
fun NormalizedObject.toPixel(
    imageWidth: Int,
    imageHeight: Int,
    pixelDepthScale: Float = imageWidth.toFloat()
): Object {
    // Convert bounding box coordinates
    val pixelBounding = RectF(
        this.bounding.left * imageWidth,
        this.bounding.top * imageHeight,
        this.bounding.right * imageWidth,
        this.bounding.bottom * imageHeight
    )

    // Convert landmarks using the correct property name
    val pixelLandmarks = this.normalizedLandmarks.map { normalizedLandmark ->
        normalizedLandmark.toPixel(imageWidth, imageHeight, pixelDepthScale)
    }

    // Convert nested objects recursively using the correct property name
    val pixelObjects = this.normalizedObjects.map { nestedObj ->
        nestedObj.toPixel(imageWidth, imageHeight, pixelDepthScale)
    }

    return Object(
        typeId = this.typeId,
        trackingId = this.trackingId,
        bounding = pixelBounding,
        landmarks = pixelLandmarks,
        objects = pixelObjects,
        params = this.params
    )
}
