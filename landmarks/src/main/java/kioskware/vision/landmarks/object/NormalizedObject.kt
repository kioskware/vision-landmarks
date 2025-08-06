package kioskware.vision.landmarks.`object`

import android.graphics.RectF
import kioskware.vision.landmarks.NormalizedLandmark
import kioskware.vision.landmarks.`object`.ObjectParam
import kioskware.vision.landmarks.common.Normalized

/**
 * Represents an object with coordinates in relative [0,1] coordinate system.
 * This is a normalized version of [Object] that implements the [Normalized] interface.
 *
 * All bounding box coordinates and landmark locations are in relative coordinates [0,1]
 * where (0,0) represents the top-left corner and (1,1) represents the bottom-right corner.
 */
open class NormalizedObject(
    typeId: String,
    trackingId: String,
    bounding: RectF,
    val normalizedLandmarks: List<NormalizedLandmark> = emptyList(),
    val normalizedObjects: List<NormalizedObject> = emptyList(),
    params: List<ObjectParam<*>> = emptyList()
) : Object(
    typeId = typeId,
    trackingId = trackingId,
    bounding = bounding,
    landmarks = normalizedLandmarks, // Cast is safe since NormalizedLandmark extends Landmark
    objects = normalizedObjects,     // Cast is safe since NormalizedObject extends Object
    params = params
), Normalized
