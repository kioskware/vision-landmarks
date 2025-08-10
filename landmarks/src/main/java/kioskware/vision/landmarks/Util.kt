package kioskware.vision.landmarks

import kioskware.vision.landmarks.`object`.Object
import kioskware.vision.landmarks.`object`.ObjectParam

fun List<Landmark>?.getByType(typeId: String): List<Landmark> {
    return this?.filter { it.typeId == typeId } ?: emptyList()
}

fun List<Landmark>?.getByTypePrefix(typeIdPrefix: String): List<Landmark> {
    return this?.filter { it.typeId.startsWith(typeIdPrefix) } ?: emptyList()
}

fun List<NormalizedLandmark>?.getByType(typeId: String): List<NormalizedLandmark> {
    return this?.filter { it.typeId == typeId } ?: emptyList()
}

fun List<NormalizedLandmark>?.getByTypePrefix(typeIdPrefix: String): List<NormalizedLandmark> {
    return this?.filter { it.typeId.startsWith(typeIdPrefix) } ?: emptyList()
}

fun List<Object>.getByType(typeId: String): List<Object> {
    return this.filter { it.typeId == typeId }
}

fun List<Object>.getByTypePrefix(typeIdPrefix: String): List<Object> {
    return this.filter { it.typeId.startsWith(typeIdPrefix) }
}

fun List<Object>.getByTrackingId(trackingId: String): Object? {
    return this.firstOrNull { it.trackingId == trackingId }
}

fun List<ObjectParam<*>>.getByType(typeId: String): ObjectParam<*>? {
    return this.firstOrNull { it.typeId == typeId }
}

fun List<ObjectParam<*>>.getByTypePrefix(typeIdPrefix: String): List<ObjectParam<*>> {
    return this.filter { it.typeId.startsWith(typeIdPrefix) }
}