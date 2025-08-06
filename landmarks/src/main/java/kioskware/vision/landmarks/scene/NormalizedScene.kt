package kioskware.vision.landmarks.scene

import kioskware.vision.landmarks.NormalizedLandmark
import kioskware.vision.landmarks.common.Normalized
import kioskware.vision.landmarks.`object`.NormalizedObject
import kioskware.vision.landmarks.`object`.ObjectParam

/**
 * Represents a scene with coordinates in relative [0,1] coordinate system.
 * This is a normalized version of [Scene] that implements the [Normalized] interface.
 *
 * All bounding box coordinates and landmark locations are in relative coordinates [0,1]
 * where (0,0) represents the top-left corner and (1,1) represents the bottom-right corner.
 */
class NormalizedScene(
    val normalizedLandmarks: List<NormalizedLandmark> = emptyList(),
    val normalizedObjects: List<NormalizedObject> = emptyList(),
    params: List<ObjectParam<*>> = emptyList(),
    override val originalWidth: Int,
    override val originalHeight: Int,
    override val sourceProcessor: SceneImageProcessor
) : Scene(
    landmarks = normalizedLandmarks, // Cast is safe since NormalizedLandmark extends Landmark
    objects = normalizedObjects,     // Cast is safe since NormalizedObject extends Object
    params = params,
    originalWidth = originalWidth,
    originalHeight = originalHeight,
    sourceProcessor = sourceProcessor
), Normalized
