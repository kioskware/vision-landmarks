package kioskware.vision.landmarks.scene

import android.graphics.RectF
import kioskware.vision.landmarks.Landmark
import kioskware.vision.landmarks.isValidNormalized
import kioskware.vision.landmarks.`object`.Object
import kioskware.vision.landmarks.`object`.ObjectParam
import kioskware.vision.landmarks.`object`.isValidNormalized
import kioskware.vision.landmarks.toNormalized
import kioskware.vision.landmarks.toPixel
import kioskware.vision.landmarks.`object`.toNormalized as objectToNormalized
import kioskware.vision.landmarks.`object`.toPixel as objectToPixel

/**
 * Constant for scene type id..
 */
val TypeIdScene = "@scene"

/**
 * Constant for scene tracking id.
 */
val TrackingIdScene = "#scene"

/**
 * Represents a scene in an image containing objects.
 * The scene is defined by its source object and its original dimensions.
 *
 * @param landmarks List of [Landmark]s detected in the scene.
 * @param objects List of [Object]s detected in the scene, each containing landmarks and their positions.
 * @param params List of additional parameters associated with the scene.
 * @param originalWidth The original width of the scene in pixels.
 * @param originalHeight The original height of the scene in pixels.
 * @param sourceProcessor The image processor that generated this scene.
 */
open class Scene internal constructor(
    landmarks: List<Landmark>,
    objects: List<Object>,
    params: List<ObjectParam<*>>,
    open val originalWidth: Int,
    open val originalHeight: Int,
    open val sourceProcessor: SceneImageProcessor
) : Object(
    typeId = TypeIdScene,
    trackingId = TrackingIdScene,
    landmarks = landmarks,
    objects = objects,
    params = params,
    bounding = RectF(
        0f,
        0f,
        originalWidth.toFloat(),
        originalHeight.toFloat()
    )
)

/**
 * Combines two scenes into one.
 * The scenes must have the same dimensions (originalWidth and originalHeight).
 *
 * @receiver The first scene to combine
 * @param other The second scene to combine
 * @return A new Scene containing landmarks and objects from both scenes
 * @throws IllegalArgumentException if the scenes have different dimensions
 */
operator fun Scene.plus(other: Scene): Scene {
    if(originalWidth != other.originalWidth || originalHeight != other.originalHeight) {
        throw IllegalArgumentException("Scenes must have the same dimensions to be combined")
    }
    return Scene(
        landmarks = this.landmarks + other.landmarks,
        objects = this.objects + other.objects,
        params = this.params + other.params,
        originalWidth = this.originalWidth,
        originalHeight = this.originalHeight,
        sourceProcessor = this.sourceProcessor
    )
}

/**
 * Converts this scene from pixel coordinates to normalized relative coordinates [0,1].
 *
 * @param pixelDepthScale Optional scaling factor for z-coordinate conversion
 * @return NormalizedScene with coordinates in [0,1] range
 */
fun Scene.toNormalized(
    pixelDepthScale: Float = originalWidth.toFloat()
): NormalizedScene {
    // Convert landmarks
    val normalizedLandmarks = this.landmarks.map { landmark ->
        landmark.toNormalized(originalWidth, originalHeight, pixelDepthScale)
    }

    // Convert objects
    val normalizedObjects = this.objects.map { obj ->
        obj.objectToNormalized(originalWidth, originalHeight, pixelDepthScale)
    }

    return NormalizedScene(
        normalizedLandmarks = normalizedLandmarks,
        normalizedObjects = normalizedObjects,
        params = this.params,
        originalWidth = this.originalWidth,
        originalHeight = this.originalHeight,
        sourceProcessor = this.sourceProcessor
    )
}

/**
 * Validates that all coordinates in a NormalizedScene are within valid bounds [0,1].
 *
 * @receiver The scene to validate
 * @param checkZ Whether to also validate Z coordinates of landmarks
 * @return True if all coordinates are valid, false otherwise
 */
fun NormalizedScene.isValidNormalized(checkZ: Boolean = false): Boolean {
    // Check landmarks
    val landmarksValid = normalizedLandmarks.all { landmark ->
        landmark.isValidNormalized(checkZ)
    }

    if (!landmarksValid) return false

    // Check objects
    val objectsValid = normalizedObjects.all { obj ->
        obj.isValidNormalized(checkZ)
    }

    return objectsValid
}

/**
 * Extension function for NormalizedScene to convert back to pixel coordinates.
 */
fun NormalizedScene.toPixel(
    pixelDepthScale: Float = originalWidth.toFloat()
): Scene {
    // Convert landmarks
    val pixelLandmarks = this.normalizedLandmarks.map { normalizedLandmark ->
        normalizedLandmark.toPixel(originalWidth, originalHeight, pixelDepthScale)
    }

    // Convert objects
    val pixelObjects = this.normalizedObjects.map { normalizedObj ->
        normalizedObj.objectToPixel(originalWidth, originalHeight, pixelDepthScale)
    }

    return Scene(
        landmarks = pixelLandmarks,
        objects = pixelObjects,
        params = this.params,
        originalWidth = this.originalWidth,
        originalHeight = this.originalHeight,
        sourceProcessor = this.sourceProcessor
    )
}
