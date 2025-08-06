package kioskware.vision.landmarks

import kioskware.vision.landmarks.common.CoordinateNormalizer
import kioskware.vision.landmarks.common.Point3D
import kioskware.vision.landmarks.common.toPixel
import kioskware.vision.landmarks.common.toRelative

/**
 * Represents a point in a pose detected by ML Kit Pose Detection.
 * @param typeId The unique identifier for the landmark type.
 * @param location The 3D location of the landmark in the image.
 * @param score The probability score of the landmark detection.
 * This value indicates the confidence of the detection, where 1.0 is a perfect score
 * and 0.0 indicates no confidence.
 */
open class Landmark(
    val typeId: String,
    val location: Point3D,
    val score: Float
)

/**
 * Validates that all coordinates in a NormalizedLandmark are within valid bounds [0,1].
 *
 * @receiver The landmark to validate
 * @param checkZ Whether to also validate the Z coordinate
 * @return True if all coordinates are valid, false otherwise
 */
fun NormalizedLandmark.isValidNormalized(checkZ: Boolean = false): Boolean {
    return CoordinateNormalizer.isValidRelativePoint(location, checkZ)
}

/**
 * Converts this landmark from pixel coordinates to normalized relative coordinates [0,1].
 *
 * @param imageWidth The width of the image in pixels
 * @param imageHeight The height of the image in pixels
 * @param pixelDepthScale Optional scaling factor for z-coordinate conversion
 * @return NormalizedLandmark with coordinates in [0,1] range
 */
fun Landmark.toNormalized(
    imageWidth: Int,
    imageHeight: Int,
    pixelDepthScale: Float = imageWidth.toFloat()
): NormalizedLandmark {
    val normalizedLocation = this.location.toRelative(imageWidth, imageHeight, pixelDepthScale)
    return NormalizedLandmark(this.typeId, normalizedLocation, this.score)
}

/**
 * Extension function for NormalizedLandmark to convert back to pixel coordinates.
 */
fun NormalizedLandmark.toPixel(
    imageWidth: Int,
    imageHeight: Int,
    pixelDepthScale: Float = imageWidth.toFloat()
): Landmark {
    val pixelLocation = this.location.toPixel(imageWidth, imageHeight, pixelDepthScale)
    return Landmark(this.typeId, pixelLocation, this.score)
}
