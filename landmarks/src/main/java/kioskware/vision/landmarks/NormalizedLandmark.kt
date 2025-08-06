package kioskware.vision.landmarks

import kioskware.vision.landmarks.common.Normalized
import kioskware.vision.landmarks.common.Point3D

/**
 * Represents a landmark with coordinates in relative [0,1] coordinate system.
 * This is a normalized version of [Landmark] that implements the [Normalized] interface.
 *
 * @param typeId The unique identifier for the landmark type.
 * @param location The 3D location of the landmark in relative coordinates [0,1].
 * @param score The probability score of the landmark detection.
 */
class NormalizedLandmark(
    typeId: String,
    location: Point3D,
    score: Float
) : Landmark(typeId, location, score), Normalized
