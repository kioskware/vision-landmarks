package kioskware.vision.landmarks.common

/**
 * Marker interface indicating that a landmark or object uses relative coordinates.
 *
 * ## Coordinate System Specification
 *
 * This relative coordinate system provides a simple and universal reference frame for computer vision
 * landmarks and objects, ensuring compatibility with ML Kit and other vision frameworks.
 *
 * ### 2D Coordinate System (X, Y)
 *
 * **Origin and Scaling:**
 * - Origin `[0, 0]` is positioned at the top-left corner of the image
 * - Both width and height are normalized to span `[0, 1]`
 * - Maintains consistency with ML Kit Pose Detection output format
 *
 * **Coordinate Ranges:**
 * - **X-axis:** `[0, 1]` where `0` = left edge, `1` = right edge
 * - **Y-axis:** `[0, 1]` where `0` = top edge, `1` = bottom edge
 *
 * ### 3D Coordinate System (Z) - MLKit Compatible
 *
 * **Depth Representation:**
 * - Z-coordinate represents depth relative to the camera/image plane
 * - `z = 0`: Located at the camera plane (standard 2D detection plane)
 * - `z > 0`: Points extending away from the camera (into the scene)
 * - Typically normalized to `[0, 1]` range for consistency
 *
 * **Scaling:**
 * - Z-coordinates are proportionally scaled relative to image dimensions
 * - Preserves depth relationships across different image sizes
 *
 * ### Compatibility
 *
 * This coordinate system is designed to be fully compatible with:
 * - Google MLKit Pose Detection and Face Detection output
 * - Standard computer vision frameworks expecting normalized coordinates
 * - Resolution-independent landmark processing
 *
 * @see kioskware.vision.landmarks.scene.Scene
 * @see kioskware.vision.landmarks.object.Object
 */
interface Normalized {
    companion object {
        /** Relative coordinate range for both X and Y axes */
        const val COORDINATE_MIN = 0.0f
        const val COORDINATE_MAX = 1.0f
        /** Range span of the relative coordinate system */
        const val COORDINATE_RANGE = COORDINATE_MAX - COORDINATE_MIN
    }
}


/**
 * Utility class for converting between pixel coordinates and relative coordinates.
 *
 * This utility provides conversion methods for Point3D objects using a relative coordinate system
 * that is compatible with ML Kit and other computer vision frameworks.
 *
 * ## Coordinate System Overview
 * - **Relative coordinates:** [0,1] range for both X and Y axes
 * - **Origin:** Top-left corner at [0, 0]
 * - **X-axis:** [0, 1] where 0 = left edge, 1 = right edge
 * - **Y-axis:** [0, 1] where 0 = top edge, 1 = bottom edge
 * - **Z-axis:** Scaled proportionally, typically [0, 1] for depth measurements
 */
object CoordinateNormalizer {

    /**
     * Converts pixel coordinates to relative coordinates [0,1].
     *
     * @param pixelPoint The point in pixel coordinates where (0,0) is top-left corner
     * @param imageWidth The width of the image in pixels
     * @param imageHeight The height of the image in pixels
     * @param pixelDepthScale Optional scaling factor for z-coordinate conversion from pixel space.
     *                       Defaults to imageWidth for proportional scaling.
     * @return Point3D in relative coordinate system [0,1]
     */
    fun pixelToRelative(
        pixelPoint: Point3D,
        imageWidth: Int,
        imageHeight: Int,
        pixelDepthScale: Float = imageWidth.toFloat()
    ): Point3D {
        require(imageWidth > 0) { "Image width must be positive" }
        require(imageHeight > 0) { "Image height must be positive" }

        return Point3D(
            x = pixelPoint.x / imageWidth,
            y = pixelPoint.y / imageHeight,
            z = pixelPoint.z / pixelDepthScale
        )
    }

    /**
     * Converts relative coordinates [0,1] to pixel coordinates.
     *
     * @param relativePoint The point in relative coordinate system [0,1]
     * @param imageWidth The width of the image in pixels
     * @param imageHeight The height of the image in pixels
     * @param pixelDepthScale Optional scaling factor for z-coordinate conversion to pixel space.
     *                       Defaults to imageWidth for proportional scaling.
     * @return Point3D in pixel coordinate system where (0,0) is top-left corner
     */
    fun relativeToPixel(
        relativePoint: Point3D,
        imageWidth: Int,
        imageHeight: Int,
        pixelDepthScale: Float = imageWidth.toFloat()
    ): Point3D {
        require(imageWidth > 0) { "Image width must be positive" }
        require(imageHeight > 0) { "Image height must be positive" }

        return Point3D(
            x = relativePoint.x * imageWidth,
            y = relativePoint.y * imageHeight,
            z = relativePoint.z * pixelDepthScale
        )
    }

    /**
     * Calculates the aspect ratio for given image dimensions.
     *
     * @param imageWidth The width of the image in pixels
     * @param imageHeight The height of the image in pixels
     * @return The aspect ratio (height/width)
     */
    fun calculateAspectRatio(imageWidth: Int, imageHeight: Int): Float {
        require(imageWidth > 0) { "Image width must be positive" }
        require(imageHeight > 0) { "Image height must be positive" }
        return imageHeight.toFloat() / imageWidth.toFloat()
    }

    /**
     * Validates that a point is within relative coordinate bounds [0,1].
     *
     * @param point The point to validate
     * @param checkZ Whether to also validate the Z coordinate (default: false)
     * @return True if the point is within bounds, false otherwise
     */
    fun isValidRelativePoint(point: Point3D, checkZ: Boolean = false): Boolean {
        val validXY = point.x in 0.0f..1.0f && point.y in 0.0f..1.0f
        return if (checkZ) {
            validXY && point.z in 0.0f..1.0f
        } else {
            validXY
        }
    }

    /**
     * Clamps a point to relative coordinate bounds [0,1].
     *
     * @param point The point to clamp
     * @param clampZ Whether to also clamp the Z coordinate (default: false)
     * @return New Point3D with coordinates clamped to [0,1] range
     */
    fun clampToRelativeBounds(point: Point3D, clampZ: Boolean = false): Point3D {
        val clampedX = point.x.coerceIn(0.0f, 1.0f)
        val clampedY = point.y.coerceIn(0.0f, 1.0f)
        val clampedZ = if (clampZ) point.z.coerceIn(0.0f, 1.0f) else point.z

        return Point3D(clampedX, clampedY, clampedZ)
    }
}