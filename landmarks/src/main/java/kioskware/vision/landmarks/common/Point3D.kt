package kioskware.vision.landmarks.common

/**
 * Represents a 3D point in space with x, y, and z coordinates.
 *
 * @property x The x-coordinate of the point.
 * @property y The y-coordinate of the point.
 * @property z The z-coordinate of the point.
 */
data class Point3D(
    val x: Float,
    val y: Float,
    val z: Float
)

/*
 * Extension functions for Point3D coordinate conversion.
 * These provide convenient methods for converting between pixel and relative coordinate systems.
 */

/**
 * Converts this point from pixel coordinates to relative coordinates [0,1].
 *
 * @param imageWidth The width of the image in pixels
 * @param imageHeight The height of the image in pixels
 * @param pixelDepthScale Optional scaling factor for z-coordinate conversion. Defaults to imageWidth.
 * @return New Point3D in relative coordinate system where (0,0) is top-left, (1,1) is bottom-right
 */
fun Point3D.toRelative(
    imageWidth: Int,
    imageHeight: Int,
    pixelDepthScale: Float = imageWidth.toFloat()
): Point3D {
    require(imageWidth > 0) { "Image width must be positive" }
    require(imageHeight > 0) { "Image height must be positive" }

    return Point3D(
        x = this.x / imageWidth,
        y = this.y / imageHeight,
        z = this.z / pixelDepthScale
    )
}

/**
 * Converts this point from relative coordinates [0,1] to pixel coordinates.
 *
 * @param imageWidth The width of the image in pixels
 * @param imageHeight The height of the image in pixels
 * @param pixelDepthScale Optional scaling factor for z-coordinate conversion. Defaults to imageWidth.
 * @return New Point3D in pixel coordinate system where (0,0) is top-left corner
 */
fun Point3D.toPixel(
    imageWidth: Int,
    imageHeight: Int,
    pixelDepthScale: Float = imageWidth.toFloat()
): Point3D {
    require(imageWidth > 0) { "Image width must be positive" }
    require(imageHeight > 0) { "Image height must be positive" }

    return Point3D(
        x = this.x * imageWidth,
        y = this.y * imageHeight,
        z = this.z * pixelDepthScale
    )
}
